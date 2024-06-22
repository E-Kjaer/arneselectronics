package PIM.Domain.Test;

import PIM.Data.Communicator;

import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public void clearDatabase() throws SQLException {
        try (Statement stmt = Communicator.connection.createStatement()) {
            // Disable foreign key constraints
            stmt.execute("SET session_replication_role = 'replica';");

            // Clear the tables
            stmt.executeUpdate("DELETE FROM product;");
            stmt.executeUpdate("DELETE FROM category;");
            stmt.executeUpdate("DELETE FROM brand;");
            stmt.executeUpdate("DELETE FROM specification;");

            // Re-enable foreign key constraints
            stmt.execute("SET session_replication_role = 'origin';");
        }
    }
}
