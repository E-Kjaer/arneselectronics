package PIM.Domain.Test;

import PIM.Data.Crud.Create;
import PIM.Data.Crud.Delete;
import PIM.Data.Crud.Retrieve;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import PIM.Domain.Product;
import PIM.Data.Communicator;
import org.junit.jupiter.api.BeforeEach;

class DeleteTest {
    private Communicator communicator = new Communicator();
    private Create create;
    private Retrieve retrieve;
    private Delete delete;
    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        communicator = Communicator.connect();
        create = new Create();
        retrieve = new Retrieve();
        delete = new Delete();
        database = new Database();

        database.clearDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.clearDatabase();
    }

    @Test
    public void testRemoveProductByID() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product = new Product(1, "Test Product", "Description", "1234567890123", 10.99, false, category, brand);
        create.addProductToDatabase(product);

        Product retrievedProduct = retrieve.getProductByID(1);
        assertNotNull(retrievedProduct);

        delete.removeProductID(1);
        retrievedProduct = retrieve.getProductByID(1);
        assertNull(retrievedProduct);
    }

    @Test
    public void testRemoveCategory() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);

        Category retrievedCategory = retrieve.getCategoryByID(1);
        assertNotNull(retrievedCategory);

        delete.removeCategory(1);
        retrievedCategory = retrieve.getCategoryByID(1);
        assertNull(retrievedCategory);
    }

    @Test
    public void testRemoveBrand() throws SQLException {
        Brand brand = new Brand(1, "Samsung");
        create.addBrandToDatabase(brand);

        Brand retrievedBrand = retrieve.getBrandByID(1);
        assertNotNull(retrievedBrand);

        delete.removeBrand(1);
        retrievedBrand = retrieve.getBrandByID(1);
        assertNull(retrievedBrand);
    }
}

