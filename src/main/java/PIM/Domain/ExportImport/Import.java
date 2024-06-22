package PIM.Domain.ExportImport;

import PIM.Data.Communicator;
import PIM.Data.Crud.Create;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Domain.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Import {

    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //If the JSON file includes a property unknown for our systen, it will be ignored during deserialization.
    Create create = new Create();

    // Method to read JSON data from a file
    public String importJSONfile(String filePath) throws IOException {
        File file = new File(filePath);
        try {
            return objectMapper.readTree(file).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to deserialize JSON data into Product objects
    public List<Product> deserializeProducts(String json) throws IOException {
        // covert JSON to Java object
        List<Product> products = objectMapper.readValue(json, new TypeReference<List<Product>>() {
        });
        return products;
    }

    //Method that checks if a brand exist and if it does then it uses said brand
    //otherwise it creates a new brand
    public void ensureBrandExits(Brand brand) throws SQLException {
        String sql = "SELECT id FROM brand WHERE name = ?";
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(sql)) {
            pstmt.setString(1, brand.getName());
            ResultSet resultSet = pstmt.executeQuery();
            if (!resultSet.next()) {
                create.addBrandToDatabase(brand);
            } else {
                brand.setId(resultSet.getInt("id"));
            }
        }
    }

    //Method that checks if a category exist and if it does then it uses said category
    //otherwise it creates a new category
    public void ensureCategoryExists(Category category) throws SQLException {
        String sql = "SELECT id FROM category WHERE name = ?";
        try (PreparedStatement pstmt = Communicator.connection.prepareStatement(sql)) {
            pstmt.setString(1, category.getName());
            ResultSet resultSet = pstmt.executeQuery();
            if (!resultSet.next()) {
                create.addCategoryToDatabase(category);
            } else {
                category.setId(resultSet.getInt("id"));
            }
        }
    }


    public void importProducts(String json) throws IOException {
        List<Product> products = deserializeProducts(json); //Deserialize all products
        for (Product product : products) {
            try {
                ensureCategoryExists(product.getCategory()); //Checks if category exist
                ensureBrandExits(product.getBrand()); //Checks if brand exist
                create.addProductToDatabase(product); // Insert product into the database

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
