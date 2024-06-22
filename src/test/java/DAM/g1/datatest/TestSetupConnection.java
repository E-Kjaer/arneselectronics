package DAM.g1.datatest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import DAM.g1.data.SetupConnection;

public class TestSetupConnection {
    
    @Test
    public void testGetConnectionNotNull(){
        SetupConnection setupConnection = new SetupConnection("C:\\Users\\nicol\\OneDrive\\Skrivebord\\999 semester\\dam\\damsystem\\src\\main\\resources\\db-config.properties");
        Connection connection = setupConnection.getConnection();
        assertNotNull(connection);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
