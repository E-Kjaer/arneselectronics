package PIM.Domain.Test;

import PIM.Data.Communicator;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Data.Crud.Create;
import PIM.Data.Crud.Retrieve;
import PIM.Domain.ExportImport.Import;
import PIM.Domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ImportTest {
    private Communicator communicator = new Communicator();
    private Import importer;
    private ObjectMapper objectMapper;
    private Retrieve retrieve;
    private Create create;
    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        communicator = Communicator.connect();
        importer = new Import();
        objectMapper = new ObjectMapper();
        retrieve = new Retrieve();
        create = new Create();
        database = new Database();

        database.clearDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.clearDatabase();
    }

    @Test
    void importJSONTest() throws IOException {
        String jsonContent = importer.importJSONfile("src/test/resources/products.json");

        assertNotNull(jsonContent, "Ressource products.json not found");

        assertNotNull(jsonContent, "JSON content should not be null");
        assertTrue(jsonContent.contains("Black TV"), "JSON should contain Black TV");
        assertTrue(jsonContent.contains("Silver TV"), "JSON should contain Silver TV");
    }

    @Test
    void deserializeProductsTest() throws IOException {
        String json = "[{\"id\" : 1, \"name\" : \"Black TV\", \"description\" : \"Black TV Description\", \"ean\" : \"1234\", \"price\" : 2000.0, \"hidden_status\" : true, \"category\" : {\"id\" : 1, \"name\" : \"TV\", \"description\" : \"TV Description\"}, \"brand\" : {\"id\" : 1, \"name\" : \"Samsung\"}}]";

        List<Product> products = importer.deserializeProducts(json);

        assertNotNull(products, "Deserialized products list should not be null");
        assertEquals(1, products.size(), "Should contain one product");

        Product product = products.getFirst();

        assertEquals("Black TV", product.getName(), "Product name should be Black TV");
        assertEquals("1234", product.getEan(), "Product ean should be 1234");
    }

    @Test
    void ensureBrandExistsTest() throws SQLException {
        Brand brand = new Brand(1, "Apple");

        Brand nonExistentBrand = retrieve.getBrandByID(1);
        assertNull(nonExistentBrand);

        importer.ensureBrandExits(brand);

        Brand retrievedBrand = retrieve.getBrandByID(1);
        assertNotNull(retrievedBrand, "Brand should exist after ensureBrandExists");
        assertEquals("Apple", retrievedBrand.getName(), "Brand name should be Apple");

        // Ensure no duplicate is added
        importer.ensureBrandExits(brand);
        List<Brand> brands = retrieve.getBrands();
        assertEquals(1, brands.size(), "Should only exist one brand in the database");
    }

    @Test
    void ensureCategoryExistsTest() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");

        Category nonExistentCategory = retrieve.getCategoryByID(1);
        assertNull(nonExistentCategory);

        importer.ensureCategoryExists(category);

        Category retrievedCategory = retrieve.getCategoryByID(1);
        assertNotNull(retrievedCategory, "Category should exist after ensureCategoryExists");
        assertEquals("TV", retrievedCategory.getName(), "Category name should be TV");

        // Ensure no duplicate is added
        importer.ensureCategoryExists(category);
        List<Category> categories = retrieve.getCategories();
        assertEquals(1, categories.size(), "Should only exist one category in the database");
    }

    @Test
    void importProductsTest() throws IOException, SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Samsung");
        create.addBrandToDatabase(brand);

        String jsonContent = importer.importJSONfile("src/test/resources/products.json");

        assertNotNull(jsonContent, "products.json not found");

        importer.importProducts(jsonContent);

        List<Product> products = retrieve.getProducts();

        assertNotNull(products);
        assertEquals(2, products.size());

        Product firstProduct = products.getFirst();
        assertEquals(1, firstProduct.getId());
        assertEquals("Black TV", firstProduct.getName());
        assertEquals("Black TV Description", firstProduct.getDescription());
        assertEquals("1234", firstProduct.getEan());
        assertEquals(2000.0, firstProduct.getPrice());
        assertTrue(firstProduct.isHidden_status());
        assertEquals(1, firstProduct.getCategory().getId());
        assertEquals(1, firstProduct.getBrand().getId());

        Product secondProducts = products.get(1);
        assertEquals(2, secondProducts.getId());
        assertEquals("Silver TV", secondProducts.getName());
        assertEquals("Silver TV Description", secondProducts.getDescription());
        assertEquals("1235", secondProducts.getEan());
        assertEquals(3000.0, secondProducts.getPrice());
        assertFalse(secondProducts.isHidden_status());
        assertEquals(1, secondProducts.getCategory().getId());
        assertEquals(1, secondProducts.getBrand().getId());
    }
}
