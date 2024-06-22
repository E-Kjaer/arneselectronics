package OMS.Database;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static final String url = "jdbc:postgresql://oms-stock-14311.8nj.gcp-europe-west1.cockroachlabs.cloud:26257/defaultdb?sslmode=verify-full";
    private static final String username = "lindandersen";
    private static final String password = "BAHV_SXO70tGEtyhAYHdDQ";
    private static PGSimpleDataSource connectionSource = null;

    protected static void setupDatasource() throws SQLException {

        try {
            if (connectionSource.getConnection() != null) {
                return;
            }
        } catch (NullPointerException e) {
            System.out.println("First time creating connection to database");
        }

        // Set the log level to ERROR for the PostgreSQL JDBC driver
        Logger logger = Logger.getLogger("org.postgresql");
        logger.setLevel(Level.SEVERE); // Setting log level to ERROR
        connectionSource = new PGSimpleDataSource();
        connectionSource.setSslmode("require");
        connectionSource.setUrl(url);
        connectionSource.setUser(username);
        connectionSource.setPassword(password);
    }

    protected static ResultSet query(String sql) throws SQLException {
        ResultSet rslt;
        setupDatasource();

        Connection con = connectionSource.getConnection();
        System.out.println(sql + "\n");

        Statement statement = con.createStatement();
        rslt = statement.executeQuery(sql);

        return rslt;
    }

    protected static int queryUpdate(String sql) throws SQLException {
        int rslt = 0;
        setupDatasource();

        Connection con = connectionSource.getConnection();
            System.out.println(sql);

            Statement statement = con.createStatement();
            rslt = statement.executeUpdate(sql);
            return rslt;
    }

    public static PGSimpleDataSource getConnectionSource() {
        return connectionSource;
    }



    //Able to return standard values not ResultSet
    public static Object queryReturnTest(String sql, Object... params) throws SQLException {
        setupDatasource();
        Object returnValue = null;
        try (Connection con = connectionSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        returnValue = resultSet.getObject(i);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            throw e;
        }
        return returnValue;
    }
    //Able to return ResultSet but not standard values
    public static ResultSet queryReturnListTest(String sql, Object... params) throws SQLException {
        ResultSet resultSet = null;
        setupDatasource();
        try (Connection con = connectionSource.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return resultSet;
    }
    //Creates and Updates things
    protected static int queryCreateAndUpdate(String sql, Object... params) throws SQLException {
        setupDatasource();
        int rslt = 0;
        try (Connection con = connectionSource.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            rslt = statement.executeUpdate();
            System.out.println(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return rslt;
    }

}
