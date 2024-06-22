package PIM.Data.Crud;

import DAM.g1.data.AccessDatabase;
import DAM.g1.domain.interfacemanagement.DAMToPIM.DamToPim;
import OMS.Domain.OMS;
import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Domain.Product;
import SHOP.data.interfaces.PIMInterface;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;

public class RetrieveJSON implements PIMInterface {

    Retrieve retrieve = new Retrieve();

    // Variables to be able to communicate with DAM and OMS
    static OMS oms = new OMS();
    static AccessDatabase accessDatabase = new AccessDatabase();
    static DamToPim damToPim = new DamToPim(accessDatabase);

    // Returns all products
    public String getProductsJson() {
        List<Product> products = retrieve.getProducts();
        return convertToJson(products);
    }

    // Returns a product based on the product id
    public String getProductByIdJson(int productId) {
        Product product = retrieve.getProductByID(productId);
        return convertToJson(product);
    }

    // Returns all products based on their category (id)
    public String getProductsByCategoryJson(int categoryId) {
        List<Product> products = retrieve.getProductsByCategory(categoryId);
        return convertToJson(products);
    }

    // Returns all products based on the product name (id)
    public String getProductsByNameJson(String name) {
        List<Product> products = retrieve.getProductsByName(name);
        return convertToJson(products);
    }

    // Returns a product based on the product name
    public String getProductByNameJson(String name) {
        Product product = retrieve.getProductByName(name);
        return convertToJson(product);
    }


    // Returns all Categories (parent-Categories only)
    public String getCategoriesJson() {
        List<Category> categories = retrieve.getCategories();
        return convertToJson(categories);
    }

    // Returns all subcategories based on their parent category_id
    public String getSubcategoriesByCategoryIdJson(int categoryId) {
        List<Category> subcategories = retrieve.getSubcategoriesByCategoryID(categoryId);
        return convertToJson(subcategories);
    }



    // Returns all Brands
    public String getBrandsJson() {
        List<Brand> brands = retrieve.getBrands();
        return convertToJson(brands);
    }

    // Converts object to JSON string
    public String convertToJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(Product.class, new ProductSerializer());
            mapper.registerModule(module);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Custom serializer for Product class
    private static class ProductSerializer extends JsonSerializer<Product> {

        @Override
        public void serialize(Product product, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", product.getId());
            jsonGenerator.writeStringField("name", product.getName());
            jsonGenerator.writeStringField("description", product.getDescription());
            jsonGenerator.writeStringField("ean", product.getEan());
            jsonGenerator.writeNumberField("price", product.getPrice());
            jsonGenerator.writeBooleanField("hidden_status", product.isHidden_status());

            try {
                int stockCount = oms.getProductCount(String.valueOf(product.getId()));
                if (stockCount > 0) {
                    jsonGenerator.writeNumberField("stockCount", stockCount);
                }
            } catch (Exception e) {
                // If there is an exception (e.g., unable to retrieve stock count), do nothing
                // This ensures that the JSON output does not include the "stockCount" field
            }

            try {
                String imagePath = damToPim.getImageByProductID(product.getId());
                if (imagePath != null) {
                    jsonGenerator.writeStringField("image", imagePath);
                }
            } catch (Exception e) {
                // Same logic as for stockCount.This ensures that the JSON output does not include the "imagePath" field if not found.
            }

            // Serialize Category
            if (product.getCategory() != null) {
                jsonGenerator.writeObjectFieldStart("category");
                jsonGenerator.writeNumberField("id", product.getCategory().getId());
                jsonGenerator.writeStringField("name", product.getCategory().getName());
                jsonGenerator.writeStringField("description", product.getCategory().getDescription());
                jsonGenerator.writeEndObject();
            }
            // Serialize Brand
            if (product.getBrand() != null) {
                jsonGenerator.writeObjectFieldStart("brand");
                jsonGenerator.writeNumberField("id", product.getBrand().getId());
                jsonGenerator.writeStringField("name", product.getBrand().getName());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndObject();
        }
    }
}
