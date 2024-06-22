package PIM.Domain.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import PIM.Data.Communicator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommunicatorTest {

    private static Connection connection;

    @BeforeAll
    public static void setUp() throws SQLException {
        // Register the driver and establish the connection
        DriverManager.registerDriver(new org.postgresql.Driver());
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/pim_database",
                "postgres",
                "Cre47uat");
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        // Close the connection after all tests are done
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testMain() {
        // Capture the console output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the main method
        Communicator.main(new String[]{});

        // Check the console output
        String expectedOutput = "Connection established";
        assertEquals(expectedOutput, outContent.toString());

        // Restore the original System.out
        System.setOut(originalOut);
    }

    @Test
    public void testConnect() throws SQLException {
        // Call the connect method and check if it returns a Communicator instance
        Communicator communicator = Communicator.connect();
        assertNotNull(communicator);
    }

}