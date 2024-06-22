package PIM.Domain.Test;

import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Data.Crud.Create;
import PIM.Data.Crud.Retrieve;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import PIM.Domain.Product;
import PIM.Data.Communicator;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

class CreateTest {
    private Communicator communicator = new Communicator();
    private Create create;
    private Retrieve retrieve;
    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        communicator = Communicator.connect();
        create = new Create();
        retrieve = new Retrieve();
        database = new Database();

        database.clearDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.clearDatabase();
    }

    @Test
    public void testAddProductToDatabase() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);

        // Create a sample product
        Product product = new Product(1, "MacBook", "Description", "1234567890123", 10.99, false, category, brand);

        Product nonExistentProduct = retrieve.getProductByID(1);
        assertNull(nonExistentProduct);

        create.addProductToDatabase(product);

        // Retrieve the product by its name from the database
        Product retrievedProduct = retrieve.getProductByName("MacBook");

        // Assert that the retrieved product is not null
        assertNotNull(retrievedProduct);

        assertEquals(1, retrievedProduct.getId());
        assertEquals("MacBook", retrievedProduct.getName());
        assertEquals("Description", retrievedProduct.getDescription());
        assertEquals("1234567890123", retrievedProduct.getEan());
        assertEquals(10.99, retrievedProduct.getPrice());
        assertFalse(retrievedProduct.isHidden_status());
        assertEquals(1, retrievedProduct.getCategory().getId());
        assertEquals(1, retrievedProduct.getBrand().getId());
    }

    @Test
    public void testAddCategoryToDatabase() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");

        Category nonExistentCategory = retrieve.getCategoryByID(1);
        assertNull(nonExistentCategory);

        create.addCategoryToDatabase(category);
        Category retrievedCategory = retrieve.getCategoryByID(1);

        assertNotNull(retrievedCategory);
        assertEquals("TV", retrievedCategory.getName());
        assertEquals("TV Description", retrievedCategory.getDescription());
    }

    @Test
    public void testAddBrandToDatabase() throws SQLException {
        Brand brand = new Brand(1, "Apple");

        Brand nonExistentBrand = retrieve.getBrandByID(1);
        assertNull(nonExistentBrand);

        create.addBrandToDatabase(brand);
        Brand retrievedBrand = retrieve.getBrandByID(1);

        assertNotNull(retrievedBrand);
        assertEquals("Apple", retrievedBrand.getName());
    }

    @Test
    public void testAddSeveralProductToDatabase() throws SQLException {
        String baseName = "Nyt";
        String baseEAN = "123";

        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);

        for (int i = 2; i <= 6; i++) {
            String productName = baseName + "t".repeat(i); // Concatenate 't' to the base name
            String productEAN = baseEAN + i;
            Product product = new Product(i, productName, "Description", productEAN, 10.99, false, category, brand);

            Product nonExistentProduct = retrieve.getProductByID(i);
            assertNull(nonExistentProduct, "Product does exist");

            create.addProductToDatabase(product);

            // Retrieve the product by its name from the database
            Product retrievedProduct = retrieve.getProductByName(productName);

            // Assert that the retrieved product is not null
            assertNotNull(retrievedProduct, "Product does not exist");

            assertEquals(i, retrievedProduct.getId(), "ID does not match");
            assertEquals(productName, retrievedProduct.getName());
            assertEquals("Description", retrievedProduct.getDescription());
            assertEquals(productEAN, retrievedProduct.getEan());
            assertEquals(10.99, retrievedProduct.getPrice());
            assertFalse(retrievedProduct.isHidden_status());
            assertEquals(category.getId(), retrievedProduct.getCategory().getId());
            assertEquals(brand.getId(), retrievedProduct.getBrand().getId());
        }
    }
}