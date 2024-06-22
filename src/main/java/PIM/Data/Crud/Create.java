package PIM.Data.Crud;

import PIM.Data.Communicator;
import PIM.Domain.Brand;
import PIM.Domain.Product;
import PIM.Domain.Category;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Create {

    //Create a Product
    public void addProductToDatabase(Product product) throws SQLException {
        String sql = "INSERT INTO product (id, name, description, ean, price, hidden_status, category_id, brand_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(sql)) { //Try prepared statement to create product
            pstmt.setInt(1, product.getId()); //id
            pstmt.setString(2, product.getName()); //name
            pstmt.setString(3, product.getDescription()); //description
            pstmt.setString(4, product.getEan()); //EAN
            pstmt.setDouble(5, product.getPrice()); //price
            pstmt.setBoolean(6, product.isHidden_status()); //hidden_status
            pstmt.setInt(7, product.getCategory().getId()); //category
            pstmt.setInt(8, product.getBrand().getId()); //brand

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error occurred in adding product: " + e); //feedback message plus exception message
        }
    }

    //Create a Category
    public void addCategoryToDatabase(Category category) throws SQLException {
        String sql;
        if (category.getSubCategory() != null) { //If it has a subcategory then choose this String
            sql = "INSERT INTO category (id, name, description, subcategory) VALUES (?, ?, ?, ?)";
        } else { // if not then choose this String
            sql = "INSERT INTO category (id, name, description) VALUES (?, ?, ?)";
        }

        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(sql)) {
            pstmt.setInt(1, category.getId());
            pstmt.setString(2, category.getName());
            pstmt.setString(3, category.getDescription());
            if (category.getSubCategory() != null) {
                pstmt.setInt(4, category.getSubCategory().getId());
            }

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error occurred in adding category: " + e); //feedback message plus exception message
        }
    }

    //Create a Brand
    public void addBrandToDatabase(Brand brand) throws SQLException {
        String sql = "INSERT INTO brand (id, name) VALUES (?, ?)";
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(sql)) {
            pstmt.setInt(1, brand.getId());
            pstmt.setString(2, brand.getName());

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error Occured in adding brand" + e); //feedback message plus exception message
        }
    }
}

