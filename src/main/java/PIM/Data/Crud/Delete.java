package PIM.Data.Crud;

import PIM.Data.Communicator;

import java.sql.*;

public class Delete {

    //Delete a product using its id attribute
    public void removeProductID(int productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement statement = Communicator.connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            statement.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error occurred in deleting product: " + ex); //feedback message plus exception message
        }
    }

    //Delete a Category using its id
    public void removeCategory(int category_id) {
        String sql = "DELETE FROM Category WHERE id = ?";
        try (PreparedStatement statement = Communicator.connection.prepareStatement(sql)) {
            statement.setInt(1, category_id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error occurred in deleting category: " + ex); //feedback message plus exception message
        }
    }

    //Delete a brand and its associated products
    public void removeBrand(int brand_id) {
        String sql2 = "DELETE FROM product WHERE brand_id = ?";
        String sql = "DELETE FROM Brand WHERE id = ?";

        //Delete products associated with the brand
        try (PreparedStatement statement = Communicator.connection.prepareStatement(sql2)) {
            statement.setInt(1, brand_id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //Delete the brand
        try (PreparedStatement statement = Communicator.connection.prepareStatement(sql)) {
            statement.setInt(1, brand_id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error occurred in deleting a brand and its products: " + ex); //feedback message plus exception message
        }
    }
}

