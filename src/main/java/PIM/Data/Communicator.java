package PIM.Data;

import java.sql.*;

public class Communicator {

    public static Connection connection = null;

    public static void main(String[] args) {
        connect();
    }

    public static Communicator connect() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/pim_database",
                    "postgres",
                    "postgres");

            System.out.print("Connection established");
            return new Communicator(); // Return an instance of Communicator
        } catch (SQLException e) {
            throw new RuntimeException("Connection not established: " + e);
        }
    }
}
