package OMS.Database;

import SHOP.domain.order.Shipping;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static OMS.Database.DBConnection.*;

public class ShippingDBManager {
    public static void main(String[] args) throws SQLException {
        initializeTables();
    }

    private static void initializeTables() throws SQLException {
        dropTable_Shipping();
        createShippingTable();
    }

    protected static void dropTable_Shipping() throws SQLException {
        String query = "drop table if exists shipping;\n";
        queryUpdate(query);
    }

    static void createShippingTable() throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE SHIPPING(\n");
        sql.append("id serial PRIMARY KEY NOT NULL,\n");
        sql.append("name VARCHAR(255) NOT NULL,\n");
        sql.append("address VARCHAR(255) NOT NULL,\n");
        sql.append("phoneNumber VARCHAR(8) NOT NULL,\n");
        sql.append("method VARCHAR(255) NOT NULL,\n");
        sql.append("email VARCHAR(355) NOT NULL,\n");
        sql.append("createdOn TIMESTAMP NOT NULL);");

        //Run the sql statements
        String query = sql.toString();
        queryUpdate(query);
    }

    public static void createShipping(Shipping shipping, Timestamp date) throws SQLException {
            StringBuilder sb = new StringBuilder("INSERT INTO SHIPPING (name, address, phoneNumber, email, method, createdOn)\n");
            sb.append(String.format("VALUES ('%s','%s',%d, '%s', '%s', '%s')\n",
                shipping.getName(), shipping.getAddress(), shipping.getPhoneNumber(), shipping.getEmail(), shipping.getMethod(), date));

            queryCreateAndUpdate(sb.toString());
    }

    public static BigInteger createShippingGetId(Shipping shipping, Timestamp date) {
        StringBuilder sb = new StringBuilder("INSERT INTO SHIPPING (name, address, phoneNumber, email, method, createdOn)\n");
        sb.append(String.format("VALUES ('%s','%s',%d, '%s', '%s', '%s')\n",
                shipping.getName(), shipping.getAddress(), shipping.getPhoneNumber(), shipping.getEmail(), shipping.getMethod(), date));
        sb.append("RETURNING id;");

        try (ResultSet rs = query(sb.toString())) {
            rs.next();
            return rs.getBigDecimal("id").toBigInteger();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
