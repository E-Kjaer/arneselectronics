package PIM.Data.Crud;

import PIM.Data.Communicator;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Domain.Product;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {
    private final Retrieve retrieve = new Retrieve();

    //Method to update a product
    public void updateProduct(int productId, String newName, String newEAN, String newDescription, String newPriceInput, boolean newHiddenStatus, Category newCategoryInput, Brand newBrandInput) {

        //Retrieves the current product by ID and saves in currentProduct
        Product currentProduct = retrieve.getProductByID(productId);

        //Query is created
        String query = "UPDATE product SET name = ?, ean = ?, description = ?, price = ?, hidden_status = ?, category_id = ?, brand_id = ? WHERE id = ?";

        try (PreparedStatement updateStatement = Communicator.connection.prepareStatement(query)) {
            //The data is inserted into the query
            updateStatement.setString(1, newName.isEmpty() ? currentProduct.getName() : newName);
            updateStatement.setString(2, newEAN.isEmpty() ? currentProduct.getEan() : newEAN);
            updateStatement.setString(3, newDescription.isEmpty() ? currentProduct.getDescription() : newDescription);
            updateStatement.setDouble(4, newPriceInput.isEmpty() ? currentProduct.getPrice() : Double.parseDouble(newPriceInput));
            updateStatement.setBoolean(5, newHiddenStatus);
            updateStatement.setInt(6, newCategoryInput != null ? newCategoryInput.getId() : currentProduct.getCategory().getId());
            updateStatement.setInt(7, newBrandInput != null ? newBrandInput.getId() : currentProduct.getBrand().getId());
            updateStatement.setInt(8, productId);

            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
