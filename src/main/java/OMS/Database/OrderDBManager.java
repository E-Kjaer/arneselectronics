package OMS.Database;

import OMS.Domain.Order;
import OMS.Domain.Product;
import SHOP.domain.order.Shipping;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import OMS.Domain.SendMail;
import OMS.Domain.Status;

import javax.mail.MessagingException;
import javax.xml.transform.Result;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static OMS.Database.DBConnection.*;

public class OrderDBManager {
    static void createStock() throws SQLException {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS stock (\n");
        sb.append("  id INTEGER PRIMARY KEY NOT NULL,\n");
        sb.append("  name VARCHAR(255) NOT NULL,\n");
        sb.append("  stock_price DECIMAL(10,2) NOT NULL,\n");
        sb.append("  amount INTEGER NOT NULL)");

        String query = sb.toString();
        DBConnection.queryUpdate(query);
    }

    static void createOrders() throws SQLException {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS orders (\n");
        sb.append("  id SERIAL PRIMARY KEY NOT NULL,\n");
        sb.append("  payment_method VARCHAR(255) NOT NULL,\n");
        sb.append("  total_price DECIMAL(10,2) NOT NULL,\n");
        sb.append("  date TIMESTAMP not null,\n");
        sb.append("  status VARCHAR(255) NOT NULL,\n");
        sb.append("  shipping_id INTEGER NOT NULL REFERENCES shipping(id));");

        String query = sb.toString();
        DBConnection.queryUpdate(query);
    }

    static void createOrderLines() throws SQLException {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS orderLines (\n");
        sb.append("  id SERIAL PRIMARY KEY NOT NULL,\n");
        sb.append("  order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,\n");
        sb.append("  product_id INTEGER NOT NULL,\n");
        sb.append("  amount INTEGER NOT NULL,\n");
        sb.append("  buy_price DECIMAL(10,2) NOT NULL)");

        String query = sb.toString();
        DBConnection.queryUpdate(query);
    }

    static void dropTables() throws SQLException {
        String sb = """
                DROP TABLE IF EXISTS stock;
                DROP TABLE IF EXISTS orderLines;
                DROP TABLE IF EXISTS orders;
                """;

        DBConnection.queryUpdate(sb);
    }

    public static void addOrder(Order order) throws SQLException {
        DBConnection.setupDatasource();
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBConnection.getConnectionSource().getConnection();
            connection.setAutoCommit(false);

            savepoint = connection.setSavepoint("Before commit order");

            //First make shipping, and get shippingId
            //

            BigInteger shippingId;
            Shipping shipping = order.getShipping();

            shippingId = ShippingDBManager.createShippingGetId(shipping, order.getOrderDate());

            System.out.println("Added shipping with id: " + shippingId);

            BigInteger orderId;

            StringBuilder sb = new StringBuilder("INSERT INTO orders (payment_method, total_price, date, status, shipping_id)\n");
            sb.append(String.format(Locale.US, "VALUES ( '%s', %.2f, '%s', '%s', %d)\n",
                    order.getMethod(), order.getTotalPrice(), order.getOrderDate().toString(), order.getStatus(), shippingId));
            sb.append("RETURNING id, date;");

            try (ResultSet rs = query(sb.toString())) {
                rs.next();
                orderId = rs.getBigDecimal("id").toBigInteger();
            }

            System.out.println("Added order with id: " + orderId);

            for (Product product : order.getProducts()) {
                if (!updateStock(product)) {
                    System.out.println("Rolling back transaction due to stock limit reached.");
                    connection.rollback(savepoint);
                    throw new SQLException();
                }
                addOrderLines(product, orderId);
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    // Rollback the transaction in case of an exception
                    connection.rollback(savepoint);
                    System.out.println("Rolling back transaction due to exception.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw new SQLException();

        }finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    private static boolean updateStock(Product product) throws SQLException {
        String newAmount = "SELECT amount FROM stock WHERE id = " + product.getId();

        ResultSet rsFirst = DBConnection.query(newAmount);
        rsFirst.next();
        int amount = rsFirst.getInt("amount");
        rsFirst.close();
        if (amount - product.getAmount() < 0) {
            System.out.println("WARNING: cant update stock, product id: " + product.getId() + " inventory too low");
            return false;
        }

        int rs = DBConnection.queryUpdate(String.format("UPDATE stock SET amount = amount - %d WHERE id = %d", product.getAmount(), product.getId()));

        if(rs == 0) {
            System.out.println("WARNING: cant update stock, product id: " + product.getId() + " not found");
            return false;
        } else {
            System.out.println("\tUpdated stock amount: -" + product.getAmount() + " with id: " + product.getId());
            return true;
        }
    }

    private static void addOrderLines(Product product, BigInteger order_id) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO orderLines (order_id, product_id, amount, buy_price)\n");
        sb.append(String.format(Locale.US,"VALUES (%d,%d,%d, %10.2f) RETURNING id", order_id, product.getId(), product.getAmount(), product.getPrice()));
        ResultSet rs = queryReturnListTest(sb.toString());
        rs.next();
        long orderLines_id = rs.getLong("id");
        System.out.println("\tAdded product to orderLines with id: " + orderLines_id);
        rs.close();
    }

    //Why boolean?
    private static boolean removeProductFromStock(Product product) throws SQLException {
        String sql = "SELECT amount FROM stock WHERE id = ?";
        ResultSet rsFirst = queryReturnListTest(sql,product.getId());
        if (rsFirst.next()){
            int amount = rsFirst.getInt("amount");
            rsFirst.close();
            if (amount - product.getAmount() < 0) {
                System.out.println("WARNING: cant update stock, product id: " + product.getId() + " inventory too low");
                throw new SQLException();
            }else {
                String sql_ = "UPDATE stock SET amount = amount - ? WHERE id = ?";
                queryCreateAndUpdate(sql_, product.getAmount(), product.getId());
                System.out.println("\tUpdated stock amount: -" + product.getAmount() + " with id: " + product.getId());
                return true;
            }
        }else{
            rsFirst.close();
            System.out.println("WARNING: product id: \" + product.getId() + \" not found in stock\"");
            throw new SQLException("Product not found in stock.");
        }
    }

    public static void addProductToStock(Product product) throws SQLException {
        String sql = "INSERT INTO stock (id, name, stock_price, amount)\n" +
                String.format(Locale.US,"VALUES (%d,'%s', %10.2f, %d)\n",
                        product.getId(), product.getName(), product.getPrice(), product.getAmount()) +
                "ON CONFLICT (id) DO UPDATE SET\n" +
                "amount = stock.amount + EXCLUDED.amount";
        queryCreateAndUpdate(sql);
    }

    public static BigInteger returnOrderId(BigInteger id) throws SQLException {
        String sql = "SELECT id FROM orders WHERE shipping_id = ?;";
        Object resultFromQuery = queryReturnTest(sql, id);
        Long resultLong = (Long) resultFromQuery;
        BigInteger result = BigInteger.valueOf(resultLong);
        return result;
    }

    public static List<Long> getLowStockProducts(int lessThanOrEqAmount) throws SQLException {
        List<Long> result = new ArrayList<>();
        String sql = ("SELECT id FROM stock WHERE amount <= ?;\n");
        ResultSet rs = queryReturnListTest(sql,lessThanOrEqAmount);
        while (rs.next()) {
            result.add(rs.getLong("id"));
        }

        System.out.println("Got " + result.size() + " products below threshold of " + lessThanOrEqAmount);
        rs.close();

        return result;
    }

    public static void updateStatus() {
        String sql = "SELECT * FROM orders WHERE status = 'PENDING' OR status = 'PROCESSED'";
        ResultSet rs = null;
        try {
            rs = queryReturnListTest(sql);
            if (rs != null) {
                while (rs.next()) {
                    //Assign a random value to the variables each time the while loop runs "This is to make it look like that the orders gets shipped to the person at some random date but not to random"
                    Random random = new Random();
                    int pendingToProcessed = random.nextInt(2)+1; // Generates a number between 1 and 2
                    int processedToShipped = random.nextInt(3)+1; // Generates a number between 1 and 3

                    //Get data
                    BigInteger orderId = BigInteger.valueOf(rs.getLong("id"));
                    Timestamp timestamp = rs.getTimestamp("date");
                    String status = rs.getString("status");

                    //Get Dates
                    Date processedDate = new Date(timestamp.getTime());
                    Calendar processedCalendar = Calendar.getInstance();
                    processedCalendar.setTime(processedDate);
                    processedCalendar.add(Calendar.DAY_OF_MONTH, processedToShipped);
                    Date expectedProcessedDate = setTimeToZero(processedCalendar.getTime());

                    Date pendingDate = new Date(timestamp.getTime());
                    Calendar pendingCalendar = Calendar.getInstance();
                    pendingCalendar.setTime(pendingDate);
                    pendingCalendar.add(Calendar.DAY_OF_MONTH, pendingToProcessed);
                    Date expectedPendingDate = setTimeToZero(pendingCalendar.getTime());

                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("CEST"));
                    Calendar todaysDate = Calendar.getInstance();
                    String currentDateTime = todaysDate.getTime().toString();
                    Date todayDate = setTimeToZero(sdf.parse(currentDateTime));

                    if (todayDate.after(expectedProcessedDate) || todayDate.equals(expectedProcessedDate) && Status.PROCESSED.toString().equals(status)) {
                        System.out.println("Order_Status: Order is overdue changing to SHIPPED\n");
                        String queryUpdateStatus = String.format("UPDATE orders SET status = 'SHIPPED' WHERE id = %d", orderId);
                        queryCreateAndUpdate(queryUpdateStatus);
                        System.out.println("Updated order status to SHIPPED\n" + status);

                        String queryGetEmail = "Select email from shipping Where id = (select shipping_id from orders where id = " + orderId + ");";
                        ResultSet rs2 = queryReturnListTest(queryGetEmail);
                        if (rs2.next()) {
                            String email = rs2.getString("email");
                            System.out.println(email + "\n\n");
                            SendMail.sendMail(email);
                            System.out.println("Email sent\n");
                        } else {
                            System.out.println("No email found");
                        }
                        rs2.close();
                    }
                    else if (todayDate.after(expectedPendingDate) || todayDate.equals(expectedPendingDate) && Status.PENDING.toString().equals(status)) {
                        System.out.println("Order_Status: Order is overdue changing to PROCESSED\n");
                        String queryUpdateStatus = String.format("UPDATE orders SET status = 'PROCESSED' WHERE id = %d", orderId);
                        queryCreateAndUpdate(queryUpdateStatus);
                        System.out.println("Updated order status to PROCESSED\n" + status);

                    }
                    else {
                        System.out.println("Order_Status: Order is due\n");

                    }
                }
                rs.close();
            }
        } catch (SQLException | ParseException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    private static Date setTimeToZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static ArrayList<Order> getOrders() {
        ArrayList<Order> orderList = new ArrayList<>();
        try (ResultSet rsOrders = queryReturnListTest("SELECT * FROM orders")) {
            System.out.println("Executing query: SELECT * FROM orders");
            while (rsOrders.next()) {
                System.out.println("Processing order with ID: " + rsOrders.getLong("id"));
                orderList.add(new Order(BigInteger.valueOf(rsOrders.getLong("id")),
                        rsOrders.getDouble("total_price"),
                        rsOrders.getTimestamp("date"),
                        Status.valueOf(rsOrders.getString("status"))));
            }
            System.out.println("Total orders retrieved: " + orderList.size());
            return orderList;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Product> getProducts()  {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, SUM(amount) AS total_amount, buy_price " +
                "FROM orderlines GROUP BY product_id, buy_price";
        try (ResultSet rsOrderLines = DBConnection.query(sql)) {
            while (rsOrderLines.next()) {
                long productId = rsOrderLines.getLong("product_id");
                int totalAmount = rsOrderLines.getInt("total_amount");
                double buyPrice = rsOrderLines.getDouble("buy_price");
                products.add(new Product(productId, totalAmount, buyPrice));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return products;
    }

    //Dummy
    public static void insertDummyStock() throws SQLException {
        try {
            //System.out.println(OrderDBManager.class.getClassLoader().getResourceAsStream("OMS/Database/DummyStock.json"));
            //InputStream dummyStock = OrderDBManager.class.getClassLoader().getResourceAsStream("/OMS/Database/DummyStock.json");
            InputStream dummyStock = new FileInputStream("src/main/resources/OMS/Database/DummyStock.json");

            if (dummyStock == null) {
                System.out.println("dummy is null");
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();

            Locale.setDefault(Locale.US);

            List<Product> products = objectMapper.readValue(dummyStock, typeFactory.constructCollectionType(List.class, Product.class));

            for (Product product : products) {
                OrderDBManager.addProductToStock(product);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Integer> getStockCount(String[] productId) {
        Map<String, Integer> idToAmountMap = new HashMap<>();

        for (String id : productId) {
            int amount = 0;
            String sql = String.format("SELECT amount FROM stock where id = %d;", Integer.parseInt(id));
            try (ResultSet rs = query(sql)){
                rs.next();
                amount = rs.getInt("amount");
                idToAmountMap.put(id, amount);
            } catch (SQLException e) {
                throw new RuntimeException(e + "\nUnable to get stockcount for productId: " + id);
            }
        }

        return idToAmountMap;
    }

    public static String getProductNames(long id1) throws SQLException {
        ArrayList<Object> mixedList = new ArrayList<>();
        String sql = "SELECT name FROM stock WHERE id IN (?)";
        String name = null;
        try (ResultSet resultSet = queryReturnListTest(sql, id1)) {
            while (resultSet.next()) {
                name = resultSet.getString("name");
            }
        }
        return name;
    }
}
