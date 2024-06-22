package PIM.Domain.Test;

import PIM.Data.Communicator;
import PIM.Data.Crud.Create;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Domain.ExportImport.Export;
import PIM.Domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ExportTest {
    private Communicator communicator = new Communicator();
    private Database database;
    private Export exporter;
    private Create create;

    @BeforeEach
    public void setUp() throws SQLException {
        communicator = Communicator.connect();
        exporter = new Export();
        create = new Create();
        database = new Database();

        database.clearDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.clearDatabase();
    }

    @Test
    public void saveProductListTest() throws IOException, SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product = new Product(1, "MacBook", "Description", "1234567890123", 10.99, false, category, brand);
        create.addProductToDatabase(product);

        String testFile = "products.json";

        exporter.saveProductList(testFile);

        String content = Files.readString(Paths.get(testFile));
        assertNotNull(content, "The file should contain some products");
        assertTrue(content.contains("MacBook"), "The file should contain name MacBook");
        assertTrue(content.contains("1234567890123"), "The file should contain EAN 1234567890123");
        System.out.println(content);
    }
}

