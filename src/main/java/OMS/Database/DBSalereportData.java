package OMS.Database;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import static OMS.Database.DBConnection.*;

public class DBSalereportData {
    //Does not work as intended "It gathers all the data since the server began, resulting in an all-time report instead of a monthly report"
    //To fix this, it requires to change some of the code and data structure
    public static double getProfit(Timestamp timestamp) throws SQLException {
        YearMonth yearMonth = YearMonth.from(timestamp.toLocalDateTime());
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        String sql = """
                SELECT (total_VALUE - income) AS Profit FROM (SELECT SUM(stock_price * amount) AS total_VALUE\s
                FROM stock) AS stock_value,(SELECT SUM(total_price) AS income\s
                FROM orders WHERE date >= ? AND date <= ?) AS order_income ;""";
        Object resultFromQuery = queryReturnTest(sql, startOfMonth , endOfMonth);
        if (resultFromQuery != null && resultFromQuery instanceof BigDecimal) {
            BigDecimal result = (BigDecimal) resultFromQuery;
            return result.doubleValue();
        } else {
            return 0.0;
        }
    }


    //Works as intended
    public static double getRevenue(Timestamp timestamp) throws SQLException {
        YearMonth yearMonth = YearMonth.from(timestamp.toLocalDateTime());
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        String sql = "SELECT SUM(total_price) AS income FROM orders WHERE date >= ? AND date <= ?";
        Object resultFromQuery = queryReturnTest(sql, startOfMonth, endOfMonth);
        if (resultFromQuery != null && resultFromQuery instanceof BigDecimal) {
            BigDecimal result = (BigDecimal) resultFromQuery;
            return result.doubleValue();
        } else {
            return 0.0;
        }
    }
    public static ArrayList<Double> getRevenuePerDay(Timestamp timestamp) throws SQLException {
        ArrayList<Double> newUserCounts = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(timestamp.toLocalDateTime());
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate startOfMonth = yearMonth.atDay(1);
        for (int i = 0; i < daysInMonth; i++) {
            newUserCounts.add(0.0); // Initialize with zero counts for each day
        }
        String sql = "SELECT EXTRACT(DAY FROM date), SUM(total_price) " +
                "FROM orders " +
                "WHERE EXTRACT(YEAR FROM date) = ? AND EXTRACT(MONTH FROM date) = ? " +
                "GROUP BY EXTRACT(DAY FROM date);";
        ResultSet resultFromQuery = queryReturnListTest(sql, startOfMonth.getYear(), startOfMonth.getMonthValue());
        while (resultFromQuery.next()) {
            // Retrieve the day of the month from the first column of the current row
            int dayOfMonth = resultFromQuery.getInt(1);
            // Retrieve the count of new users from the second column of the current row
            double count = resultFromQuery.getDouble(2);
            newUserCounts.set(dayOfMonth - 1, count); // Update count for the specific day
        }
        return newUserCounts;
    }
    public static double getAvgAmountPerOrder(Timestamp timestamp) throws SQLException {
        YearMonth yearMonth = YearMonth.from(timestamp.toLocalDateTime());
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        String sql = "SELECT AVG(total_price) FROM orders WHERE date >= ? AND date <= ?;";
        Object resultFromQuery = queryReturnTest(sql, startOfMonth, endOfMonth);
        if (resultFromQuery != null && resultFromQuery instanceof BigDecimal) {
            BigDecimal result = (BigDecimal) resultFromQuery;
            return result.doubleValue();
        } else {
            return 0.0;
        }
    }
    public static ArrayList<Double> getAvgAmountPerOrderPerDay(Timestamp timestamp) throws SQLException {
        ArrayList<Double> newUserCounts = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(timestamp.toLocalDateTime());
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate startOfMonth = yearMonth.atDay(1);
        for (int i = 0; i < daysInMonth; i++) {
            newUserCounts.add(0.0); // Initialize with zero counts for each day
        }
        String sql = "SELECT EXTRACT(DAY FROM date), AVG(total_price) " +
                "FROM orders " +
                "WHERE EXTRACT(YEAR FROM date) = ? AND EXTRACT(MONTH FROM date) = ? " +
                "GROUP BY EXTRACT(DAY FROM date);";
        ResultSet resultFromQuery = queryReturnListTest(sql, startOfMonth.getYear(), startOfMonth.getMonthValue());
        while (resultFromQuery.next()) {
            // Retrieve the day of the month from the first column of the current row
            int dayOfMonth = resultFromQuery.getInt(1);
            // Retrieve the count of new users from the second column of the current row
            double count = resultFromQuery.getDouble(2);
            newUserCounts.set(dayOfMonth - 1, count); // Update count for the specific day
        }
        return newUserCounts;
    }
    public static ArrayList<Integer> getBestSellers() throws SQLException {
        ArrayList<Integer> bestSellersCounts = new ArrayList<>();
        String sql = "SELECT product_id, SUM(amount) AS total_amount_sold FROM orderlines " +
                "GROUP BY product_id ORDER BY total_amount_sold DESC LIMIT 3 ";
        try (ResultSet resultSet = queryReturnListTest(sql)) {
            while (resultSet.next()) {
                int productID = resultSet.getInt("product_id");
                int totalAmountSold = resultSet.getInt("total_amount_sold");
                bestSellersCounts.add(productID);
                bestSellersCounts.add(totalAmountSold);
            }
        }
        return bestSellersCounts;
    }
    public static ArrayList<Object> getProductNamesAndPrices(int id1, int id2, int id3) throws SQLException {
        ArrayList<Object> mixedList = new ArrayList<>();
        String sql = "SELECT name, stock_price FROM stock WHERE id IN (?, ?, ?)";
        try (ResultSet resultSet = queryReturnListTest(sql, id1, id2, id3)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("stock_price");
                mixedList.add(name);
                mixedList.add(price);
            }
        }
        return mixedList;
    }
    public static int getNewUser(Timestamp timestamp) throws SQLException {
        YearMonth yearMonth = YearMonth.from(timestamp.toLocalDateTime());
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        String sql = "SELECT COUNT(email) FROM SHIPPING WHERE createdon >= ? AND createdon <= ?;";
        Object resultFromQuery = queryReturnTest(sql, startOfMonth, endOfMonth);
        Long result = (Long) resultFromQuery;
        return result.intValue();
    }
    public static ArrayList<Integer> getNewUserPerDay(Timestamp timestamp) throws SQLException {
        ArrayList<Integer> newUserCounts = new ArrayList<>();
        YearMonth yearMonthObject = YearMonth.from(timestamp.toLocalDateTime());
        int daysInMonth = yearMonthObject.lengthOfMonth();
        LocalDate firstDayOfMonth = yearMonthObject.atDay(1);
        for (int i = 0; i < daysInMonth; i++) {
            newUserCounts.add(0); // Initialize with zero counts for each day
        }

        String sql = "SELECT EXTRACT(DAY FROM createdOn), COUNT(email) " +
                "FROM SHIPPING " +
                "WHERE EXTRACT(YEAR FROM createdOn) = ? AND EXTRACT(MONTH FROM createdOn) = ? " +
                "GROUP BY EXTRACT(DAY FROM createdOn);";
        ResultSet resultFromQuery = queryReturnListTest(sql, firstDayOfMonth.getYear(), firstDayOfMonth.getMonthValue());
        while (resultFromQuery.next()) {
            // Retrieve the day of the month from the first column of the current row
            int dayOfMonth = resultFromQuery.getInt(1);
            // Retrieve the count of new users from the second column of the current row
            int count = resultFromQuery.getInt(2);
            newUserCounts.set(dayOfMonth - 1, count); // Update count for the specific day
        }
        return newUserCounts;
    }




}
