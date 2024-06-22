package PIM.Domain.Test;

import PIM.Data.Communicator;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Data.Crud.Create;
import PIM.Data.Crud.Retrieve;
import PIM.Data.Crud.Update;
import PIM.Domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTest {
    private Communicator communicator = new Communicator();
    private Create create;
    private Retrieve retrieve;
    private Update update;
    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        communicator = Communicator.connect();
        create = new Create();
        retrieve = new Retrieve();
        update = new Update();
        database = new Database();

        database.clearDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.clearDatabase();
    }

    @Test
    void updateProduct() throws SQLException {
        Category category = new Category(1, "TV", "TV Description");
        create.addCategoryToDatabase(category);
        Brand brand = new Brand(1, "Apple");
        create.addBrandToDatabase(brand);
        Product product = new Product(1, "TV", "TV Description", "55555555", 2000.0, true, category, brand);
        create.addProductToDatabase(product);

        Product productBeforeUpdate = retrieve.getProductByID(1);
        assertNotNull(productBeforeUpdate, "productBeforeUpdate");

        assertEquals(1, productBeforeUpdate.getId());
        assertEquals("TV", productBeforeUpdate.getName());
        assertEquals("TV Description", productBeforeUpdate.getDescription());
        assertEquals("55555555", productBeforeUpdate.getEan());
        assertEquals(Double.parseDouble("2000.0"), productBeforeUpdate.getPrice());
        assertTrue(productBeforeUpdate.isHidden_status());
        assertEquals(1, productBeforeUpdate.getCategory().getId());
        assertEquals(1, productBeforeUpdate.getBrand().getId());

        update.updateProduct(1, "UpdatedName", "UpdatedEAN", "UpdatedDescription", "100.0", true, category, brand);
        Product updatedProduct = retrieve.getProductByID(1);

        assertNotNull(updatedProduct, "Updated product should not be null.");
        assertEquals("UpdatedName", updatedProduct.getName(), "Name should match the updated value.");
        assertEquals("UpdatedEAN", updatedProduct.getEan(), "EAN should match the updated value.");
        assertEquals("UpdatedDescription", updatedProduct.getDescription(), "Description should match the updated value.");
        assertEquals(Double.parseDouble("100.0"), updatedProduct.getPrice(), "Price should match the updated value.");
        assertTrue(updatedProduct.isHidden_status(), "Hidden status should be true after update.");
        assertEquals(1, updatedProduct.getCategory().getId(), "Category ID should match the expected value.");
        assertEquals(1, updatedProduct.getBrand().getId(), "Brand ID should match the expected value.");
    }
}