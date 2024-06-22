package OMS.Database;

import OMS.Domain.Order;

import java.net.URISyntaxException;
import java.sql.SQLException;

public class DatabaseMain {
    public static void main(String[] args) throws SQLException, URISyntaxException {
        dropTables();
        initializeTables();
        OrderDBManager.insertDummyStock();
        //OrderDBManager.updateStatus();
    }

    private static void initializeTables() throws SQLException {
        ShippingDBManager.createShippingTable();
        OrderDBManager.createOrders();
        OrderDBManager.createOrderLines();
        OrderDBManager.createStock();
    }

    private static void dropTables() throws SQLException {
        OrderDBManager.dropTables();
        ShippingDBManager.dropTable_Shipping();
    }
}
