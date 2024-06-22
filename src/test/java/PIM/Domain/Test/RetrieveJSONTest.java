package PIM.Domain.Test;

import PIM.Data.Communicator;
import PIM.Data.Crud.Create;
import PIM.Data.Crud.RetrieveJSON;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class RetrieveJSONTest {
    private Communicator communicator = new Communicator();
    private Create create;
    private RetrieveJSON retrieveJSON;
    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        communicator = Communicator.connect();
        create = new Create();
        retrieveJSON = new RetrieveJSON();
        database = new Database();

        database.clearDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.clearDatabase();
    }

    @Test
    public void getProductsJsonTest() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product1 = new Product(1, "MacBook", "MacBookDescription", "1235", 10000.99, false, category, brand);
        create.addProductToDatabase(product1);
        Product product2 = new Product(2, "IPhone", "IPhone Description", "1234", 3000.99, true, category, brand);
        create.addProductToDatabase(product2);

        String jsonOutput = retrieveJSON.getProductsJson();

        assertNotNull(jsonOutput);

        assertTrue(jsonOutput.contains("MacBook"), "JSON should contain MacBook");
        assertTrue(jsonOutput.contains("IPhone"), "JSON should contain IPhone");
    }

    @Test
    public void getProductByIdJsonTest() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product1 = new Product(1, "MacBook", "MacBookDescription", "1235", 10000.99, false, category, brand);
        create.addProductToDatabase(product1);

        String jsonOutput = retrieveJSON.getProductByIdJson(1);

        assertNotNull(jsonOutput);

        assertTrue(jsonOutput.contains("MacBook"), "JSON should contain MacBook");
    }

    @Test
    public void getCategoriesJsonTest() throws SQLException {
        Category category1 = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category1);
        Category category2 = new Category(2, "Phone", "Phone Description");
        create.addCategoryToDatabase(category2);
        Category category3 = new Category(3, "Computer", "Computer Description");
        create.addCategoryToDatabase(category3);

        String jsonOutput = retrieveJSON.getCategoriesJson();

        assertNotNull(jsonOutput);

        assertTrue(jsonOutput.contains("TV"), "JSON should contain TV");
        assertTrue(jsonOutput.contains("Phone"), "JSON should contain Phone");
        assertTrue(jsonOutput.contains("Computer"), "JSON should contain Computer");
    }

    @Test
    public void getSubcategoriesByCategoryIdJsonTest() throws SQLException {
        Category category1 = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category1);
        Category category2 = new Category(2, "Sound", "Sound Description", category1);
        create.addCategoryToDatabase(category2);
        Category category3 = new Category(3, "Remote", "Remote Description", category1);
        create.addCategoryToDatabase(category3);

        String jsonOutput = retrieveJSON.getSubcategoriesByCategoryIdJson(1);

        assertNotNull(jsonOutput);

        assertTrue(jsonOutput.contains("Sound"), "JSON should contain Sound");
        assertTrue(jsonOutput.contains("Remote"), "JSON should contain Remote");
    }

    @Test
    public void getBrandsJsonTest() throws SQLException {
        Brand brand1 = new Brand(1, "Apple");
        create.addBrandToDatabase(brand1);
        Brand brand2 = new Brand(2, "Samsung");
        create.addBrandToDatabase(brand2);
        Brand brand3 = new Brand(3, "Lenovo");
        create.addBrandToDatabase(brand3);

        String jsonOutput = retrieveJSON.getBrandsJson();

        assertNotNull(jsonOutput);

        assertTrue(jsonOutput.contains("Apple"), "JSON should contain Apple");
        assertTrue(jsonOutput.contains("Samsung"), "JSON should contain Samsung");
        assertTrue(jsonOutput.contains("Lenovo"), "JSON should contain Lenovo");
    }

    @Test
    public void getProductsByCategoryJsonTest() throws SQLException {
        Category category1 = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category1);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product1 = new Product(1, "MacBook", "MacBookDescription", "1234", 10000.99, false, category1, brand);
        create.addProductToDatabase(product1);
        Product product2 = new Product(2, "IPhone", "IPhone Description", "1235", 3000.99, true, category1, brand);
        create.addProductToDatabase(product2);
        Product product3 = new Product(3, "Computer", "Computer Description", "1236", 6000.99, true, category1, brand);
        create.addProductToDatabase(product3);

        String jsonOutput = retrieveJSON.getProductsByCategoryJson(1);

        assertNotNull(jsonOutput);

        assertTrue(jsonOutput.contains("MacBook"), "JSON should contain MacBook");
        assertTrue(jsonOutput.contains("IPhone"), "JSON should contain IPhone");
        assertTrue(jsonOutput.contains("Computer"), "JSON should contain Computer");
    }

    @Test
    public void getProductByNameJsonTest() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product1 = new Product(1, "MacBook", "MacBookDescription", "1235", 10000.99, false, category, brand);
        create.addProductToDatabase(product1);

        String jsonOutput = retrieveJSON.getProductByNameJson("MacBook");

        assertNotNull(jsonOutput);

        assertTrue(jsonOutput.contains("MacBook"), "JSON should contain MacBook");
    }
}
