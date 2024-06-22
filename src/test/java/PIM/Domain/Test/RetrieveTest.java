package PIM.Domain.Test;

import PIM.Data.Communicator;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Data.Crud.Create;
import PIM.Data.Crud.Retrieve;
import PIM.Domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RetrieveTest {
    private Create create;
    private Retrieve retrieve;
    private Database database;
    Communicator communicator = new Communicator();

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
    public void getProducts() throws SQLException {
        String baseName = "Nyt";
        String baseEAN = "123";

        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);

        List<Product> productList = retrieve.getProducts();
        assertTrue(productList.isEmpty(), "Product list is not empty");

        for (int i = 1; i <= 5; i++) {
            String productName = baseName + "t".repeat(i); // Concatenate 't' to the base name
            String productEAN = baseEAN + i;
            Product product = new Product(i, productName, "Description", productEAN, 10.99, false, category, brand);

            create.addProductToDatabase(product);
        }

        productList = retrieve.getProducts();

        assertFalse(productList.isEmpty());
        assertEquals(5, productList.size());

        for (int i = 0; i < 5; i++) {
            String expectedName = baseName + "t".repeat(i + 1);
            String expectedEAN = baseEAN + (i + 1);

            Product product = productList.get(i);
            assertEquals(expectedName, product.getName());
            assertEquals(expectedEAN, product.getEan());
            assertEquals("Description", product.getDescription());
            assertEquals(10.99, product.getPrice());
            assertFalse(product.isHidden_status());
            assertEquals(category.getId(), product.getCategory().getId());
            assertEquals(brand.getId(), product.getBrand().getId());
        }
    }

    @Test
    void getProductByID() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product = new Product(1, "Test name", "Description", "1234", 10.99, false, category, brand);

        Product retrievedProduct = retrieve.getProductByID(1);
        assertNull(retrievedProduct);

        create.addProductToDatabase(product);

        retrievedProduct = retrieve.getProductByID(1);
        assertNotNull(retrievedProduct);

        assertEquals(product.getId(), retrievedProduct.getId());
        assertEquals(product.getName(), retrievedProduct.getName());
        assertEquals(product.getDescription(), retrievedProduct.getDescription());
        assertEquals(product.getEan(), retrievedProduct.getEan());
        assertEquals(product.getPrice(), retrievedProduct.getPrice());
        assertEquals(product.isHidden_status(), retrievedProduct.isHidden_status());
        assertEquals(product.getCategory().getId(), retrievedProduct.getCategory().getId());
        assertEquals(product.getBrand().getId(), retrievedProduct.getBrand().getId());
    }

    @Test
    void getProductsByCategory() throws SQLException {
        Category category1 = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category1);
        Category category2 = new Category(2, "Computer", "Computer Description");
        create.addCategoryToDatabase(category2);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);

        Product product1 = new Product(1, "TV name 1", "Description 1", "1234", 1000.99, false, category1, brand);
        Product product2 = new Product(2, "Computer name 2", "Description 2", "1235", 2000.99, true, category2, brand);
        Product product3 = new Product(3, "TV name 3", "Description 3", "1236", 3000.99, true, category1, brand);

        List<Product> retrievedProducts = retrieve.getProductsByCategory(1);
        assertTrue(retrievedProducts.isEmpty());

        create.addProductToDatabase(product1);
        create.addProductToDatabase(product2);
        create.addProductToDatabase(product3);

        retrievedProducts = retrieve.getProductsByCategory(1);
        assertFalse(retrievedProducts.isEmpty());

        assertEquals(retrievedProducts.getFirst().getId(), product1.getId());
        assertEquals(retrievedProducts.get(1).getId(), product3.getId());
    }

    @Test
    void getProductByName() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product = new Product(1, "Test name", "Description", "1234", 10.99, false, category, brand);

        Product retrievedProduct = retrieve.getProductByName("Test name");
        assertNull(retrievedProduct);

        create.addProductToDatabase(product);

        retrievedProduct = retrieve.getProductByName("Test name");
        assertNotNull(retrievedProduct);

        assertEquals(product.getId(), retrievedProduct.getId());
        assertEquals(product.getName(), retrievedProduct.getName());
        assertEquals(product.getDescription(), retrievedProduct.getDescription());
        assertEquals(product.getEan(), retrievedProduct.getEan());
        assertEquals(product.getPrice(), retrievedProduct.getPrice());
        assertEquals(product.isHidden_status(), retrievedProduct.isHidden_status());
        assertEquals(product.getCategory().getId(), retrievedProduct.getCategory().getId());
        assertEquals(product.getBrand().getId(), retrievedProduct.getBrand().getId());
    }

    @Test
    void getCategories() throws SQLException {
        Category category1 = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category1);
        Category category2 = new Category(2, "Computer", "Computer Description");
        create.addCategoryToDatabase(category2);
        Category category3 = new Category(3, "Sound", "Sound Description");
        create.addCategoryToDatabase(category3);

        List<Category> categoryList = retrieve.getCategories();

        assertFalse(categoryList.isEmpty());
        assertEquals(categoryList.getFirst().getId(), category1.getId());
        assertEquals(categoryList.get(1).getId(), category2.getId());
        assertEquals(categoryList.get(2).getId(), category3.getId());
    }

    @Test
    void getSubcategoriesByCategoryID() throws SQLException {
        Category category1 = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category1);
        Category category2 = new Category(2, "Sound", "Sound Description", category1);
        create.addCategoryToDatabase(category2);
        Category category3 = new Category(3, "Remote", "Remote Description", category1);
        create.addCategoryToDatabase(category3);

        List<Category> subCategoryList = retrieve.getSubcategoriesByCategoryID(1);
        System.out.println(subCategoryList);

        assertEquals(2, subCategoryList.size());
        assertEquals(subCategoryList.getFirst().getId(), category2.getId());
        assertEquals(subCategoryList.get(1).getId(), category3.getId());
    }

    @Test
    void getSpecificationsOnProduct() {
    }

    @Test
    void getCategoryByID() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        Category retrievedCategory = retrieve.getCategoryByID(1);

        assertNull(retrievedCategory);

        create.addCategoryToDatabase(category);
        retrievedCategory = retrieve.getCategoryByID(1);

        assertNotNull(retrievedCategory);

        assertEquals(category.getId(), retrievedCategory.getId());
        assertEquals(category.getName(), retrievedCategory.getName());
        assertEquals(category.getDescription(), retrievedCategory.getDescription());
    }

    @Test
    void getBrandByID() throws SQLException {
        Brand brand = new Brand(1, "Samsung");
        Brand retrievedBrand = retrieve.getBrandByID(1);

        assertNull(retrievedBrand);

        create.addBrandToDatabase(brand);
        retrievedBrand = retrieve.getBrandByID(1);

        assertNotNull(retrievedBrand);

        assertEquals(brand.getId(), retrievedBrand.getId());
        assertEquals(brand.getName(), retrievedBrand.getName());
    }

    @Test
    void getBrands() throws SQLException {
        Brand brand1 = new Brand(1, "Apple");
        create.addBrandToDatabase(brand1);
        Brand brand2 = new Brand(2, "Samsung");
        create.addBrandToDatabase(brand2);
        Brand brand3 = new Brand(3, "Lenovo");
        create.addBrandToDatabase(brand3);

        List<Brand> brandList = retrieve.getBrands();

        assertFalse(brandList.isEmpty());
        assertEquals(brandList.getFirst().getId(), brand1.getId());
        assertEquals(brandList.get(1).getId(), brand2.getId());
        assertEquals(brandList.get(2).getId(), brand3.getId());
    }

    @Test
    void getCategoryIDByName() {
    }

    @Test
    void getBrandIDByName() {
    }
}