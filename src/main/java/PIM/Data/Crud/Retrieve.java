package PIM.Data.Crud;

import PIM.Data.Communicator;
import PIM.Domain.Brand;
import PIM.Domain.Product;
import PIM.Domain.Category;
import PIM.Domain.Specifications;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Retrieve {

    //Returns all products
    public List<Product> getProducts() {
        String selectSQL = "SELECT * FROM product"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            List<Product> listProducts = new ArrayList<>();
            while (resultSet.next()) {
                listProducts.add(new Product(
                        resultSet.getInt(1), //id
                        resultSet.getString(2), //name
                        resultSet.getString(3), //description
                        resultSet.getString(4), //ean
                        resultSet.getDouble(5), //price
                        resultSet.getBoolean(6), //hidden_status
                        getCategoryByID(resultSet.getInt(7)), //category_id
                        getBrandByID(resultSet.getInt(8)) //brand_id
                ));
            }
            return listProducts;
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred in retrieving products: " + e);
        }
    }

    //Returns a product based on the product id
    public Product getProductByID(int product_id) {
        String selectSQL = "SELECT * FROM product WHERE id = ?"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, product_id); //inserts the id at the first parameter
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            if (!resultSet.next()) {
                return null;
            }
            return new Product(
                    resultSet.getInt(1), //id
                    resultSet.getString(2), //name
                    resultSet.getString(3), //description
                    resultSet.getString(4), //ean
                    resultSet.getDouble(5), //price
                    resultSet.getBoolean(6), //hidden_status
                    getCategoryByID(resultSet.getInt(7)), //category_id
                    getBrandByID(resultSet.getInt(8)) //brand_id
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred in retrieving product by its id: " + e);
        }
    }

    //Returns all products based on their category (id)
    public List<Product> getProductsByCategory(int category_id) {
        String selectSQL = "SELECT * FROM product WHERE category_id = ?"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, category_id); ///inserts the id at the first parameter
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            List<Product> listProducts = new ArrayList<>();
            while (resultSet.next()) {
                listProducts.add(new Product(
                        resultSet.getInt(1), //id
                        resultSet.getString(2), //name
                        resultSet.getString(3), //description
                        resultSet.getString(4), //ean
                        resultSet.getDouble(5), //price
                        resultSet.getBoolean(6), //hidden_status
                        getCategoryByID(resultSet.getInt(7)), //category_id
                        getBrandByID(resultSet.getInt(8)) //brand_id
                ));
            }
            return listProducts;
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred in retrieving products by category: " + e);
        }
    }
    //Returns a product based on the product name
    public Product getProductByName(String name) {
        String selectSQL = "SELECT * FROM product WHERE name LIKE ?"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, "%" + name + "%"); //inserts the name with wildcards!
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            if (!resultSet.next()) {
                return null;
            }
            return new Product(
                    resultSet.getInt(1), //id
                    resultSet.getString(2), //name
                    resultSet.getString(3), //description
                    resultSet.getString(4), //ean
                    resultSet.getDouble(5), //price
                    resultSet.getBoolean(6), //hidden_status
                    getCategoryByID(resultSet.getInt(7)), //category_id
                    getBrandByID(resultSet.getInt(8)) //brand_id
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Returns all products based on their category (id)
    public List<Product> getProductsByName(String name) {
        String selectSQL = "SELECT * FROM product WHERE name LIKE ?"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, "%" + name + "%"); //inserts the name with wildcards!
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            List<Product> listProducts = new ArrayList<>();
            while (resultSet.next()) {
                listProducts.add(new Product(
                        resultSet.getInt(1), //id
                        resultSet.getString(2), //name
                        resultSet.getString(3), //description
                        resultSet.getString(4), //ean
                        resultSet.getDouble(5), //price
                        resultSet.getBoolean(6), //hidden_status
                        getCategoryByID(resultSet.getInt(7)), //category_id
                        getBrandByID(resultSet.getInt(8)) //brand_id
                ));
            }
            return listProducts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    //Returns all Categories
    public List<Category> getCategories() {
        String selectSQL = "SELECT * FROM Category"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            List<Category> listCategories = new ArrayList<>();
            while (resultSet.next()) {
                listCategories.add(new Category(
                        resultSet.getInt(1), //id
                        resultSet.getString(2), //name
                        resultSet.getString(3), //description
                        getCategoryByID(resultSet.getInt(4)) //subcategory
                ));
            }
            return listCategories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Returns all subcategories based on their parent category_id
    public List<Category> getSubcategoriesByCategoryID(int subcategory) {
        String selectSQL = "SELECT * FROM Category WHERE subcategory = ?"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, subcategory); //inserts the id at the first parameter
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            List<Category> listSubcategories = new ArrayList<>();
            while (resultSet.next()) {
                listSubcategories.add(new Category(
                        resultSet.getInt(1), //id
                        resultSet.getString(2), //name
                        resultSet.getString(3), //description
                        getCategoryByID(resultSet.getInt(4)) //subcategory
                ));
            }
            return listSubcategories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category getCategoryByID(int category_id) {
        String selectSQL = "SELECT * FROM Category WHERE id = ?"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, category_id); ///inserts the id at the first parameter
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            if (!resultSet.next()) {
                return null;
            }
            Category category;
            int parentId = resultSet.getInt(4); //parentId
            Category subCategory = null;

            if (parentId != 0 && parentId != category_id) { // Checks if it has a parentId (which isn't the category_id))
                subCategory = getCategoryByID(parentId); // Recursive call to get parent category
            }
            category = new Category(
                    resultSet.getInt(1), //id
                    resultSet.getString(2), //name
                    resultSet.getString(3), //description
                    subCategory //subcategory
            );
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Brand getBrandByID(int brand_id) {
        String selectSQL = "SELECT * FROM Brand WHERE id = ?"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, brand_id); ///inserts the id at the first parameter
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            if (!resultSet.next()) {
                return null;
            }
            return new Brand(
                    resultSet.getInt(1), //id
                    resultSet.getString(2) //name
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Brand> getBrands() {
        String selectSQL = "SELECT * FROM Brand"; //the SQL query
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = pstmt.executeQuery(); //executes the Query
            List<Brand> listBrands = new ArrayList<>();
            while (resultSet.next()) {
                listBrands.add(new Brand(
                        resultSet.getInt(1), //id
                        resultSet.getString(2) //name
                ));
            }
            return listBrands;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*public int getCategoryIDByName(String categoryName) {
        String query = "SELECT id FROM Category WHERE name = ?";
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(query)) {
            pstmt.setString(1, categoryName);
            ResultSet resultSet = pstmt.executeQuery();

            if (!resultSet.next()) {
                return 0;
            }
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBrandIDByName(String brandName) {
        String query = "SELECT id FROM Brand WHERE name = ?";

        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(query)) {
            pstmt.setString(1, brandName);
            ResultSet resultSet = pstmt.executeQuery();

            if (!resultSet.next()) {
                return 0;
            }
            return resultSet.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
}
