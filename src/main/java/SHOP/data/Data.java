package SHOP.data;

import CMS.CMS;
import DAM.g1.data.AccessDatabase;
import DAM.g1.domain.interfacemanagement.DAMToShop.DAMTOSHOPInterface;
import DAM.g1.domain.interfacemanagement.DAMToShop.DamToShop;
import OMS.Domain.Interfaces.OMSTOSHOPInterface;
import OMS.Domain.OMS;
import PIM.Data.Communicator;
import PIM.Data.Crud.RetrieveJSON;
import SHOP.Flags;
import SHOP.data.models.Guide;
import SHOP.domain.order.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import SHOP.data.dummies.CMS.CMS_dummy;
import SHOP.data.dummies.DAM.DAM_dummy;
import SHOP.data.dummies.OMS.OMS_dummy;
import SHOP.data.interfaces.CMSInterface;
import SHOP.data.interfaces.DAMInterface;
import SHOP.data.interfaces.OMSInterface;
import SHOP.data.interfaces.PIMInterface;
import SHOP.data.dummies.PIM.PIM_dummy;
import SHOP.data.models.Product;
import SHOP.data.models.Products;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class Data {
    private ObjectMapper objectMapper;
    private ObjectWriter objectWriter;
    private PIMInterface pimInterface;
    private CMSInterface cmsInterface;
    private DAMTOSHOPInterface damInterface;
    private OMSTOSHOPInterface omsInterface;

    public Data() {
        // ObjectMapper converts JSON to POJO
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        // Feature flag for development mode
        if (Flags.isDevelopment()) {
            this.pimInterface = new PIM_dummy();
            this.cmsInterface = new CMS_dummy();
            this.damInterface = new DAM_dummy();
            this.omsInterface = new OMS_dummy();
        } else {
            //Activate DB connection for PIM
            Communicator.connect();
            this.pimInterface = new RetrieveJSON();
            this.cmsInterface = new CMS();
            AccessDatabase accessDatabase = new AccessDatabase();
            this.damInterface = new DamToShop(accessDatabase);
            this.omsInterface = new OMS();
        }
    }

    public Product getProduct(int id) {
        // Getting the product from PIM based on an id.
        String jsonString = pimInterface.getProductByIdJson(id);
        return parseProduct(new JSONObject(jsonString));
    }

    public ArrayList<Product> getProductsByCategory(String category) {
        // Getting all the products from PIM, based on the chosen category
        ArrayList<Product> products = new ArrayList<>();

        // Translating the category to the associated id, before retrieving the products from PIM
        String jsonString = pimInterface.getProductsByCategoryJson(parseCategory(category));
        JSONArray productsJSON = new JSONArray(jsonString);

        // Adds the product to an array that gets returned
        for (int i = 0; i < productsJSON.length(); i++) {
            products.add(parseProduct(productsJSON.getJSONObject(i)));
        }

        return products;
    }

    private Product parseProduct(JSONObject jsonObject) {
        // Getting the relevant atttributes from a JSONObject and parsing it to a Product.
        // The following are currently being used
        int id = jsonObject.getInt("id");
        double price = jsonObject.getDouble("price");
        String description = jsonObject.getString("description");
        String name = jsonObject.getString("name");
        String brand = jsonObject.getJSONObject("brand").getString("name");

        // These attributes are not currently being used, but should be implemented in the future
        String ean = jsonObject.getString("ean");
        boolean hidden_status = jsonObject.getBoolean("hidden_status");
        String category = jsonObject.getJSONObject("category").getString("name");

        // This is dependant on the JSON structure.
        int count = jsonObject.getInt("stockCount");
        String image = null;
        try {
            image = jsonObject.getString("image");
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        // Setting a fixed stockCount for now, should be retrieved from the PIM in the future.
        Product product = new Product(id, name, (float) price, brand, description, category, count, image);
        return product;
    }

    private int parseCategory(String category) {
        // Translating our hardcoded categories into the associated PIM category-id's
        Map<String, Integer> categories = new HashMap<>();
        categories.put("Computers & Office", 1);
        categories.put("Phones & Smart Devices", 2);
        categories.put("TV & Smart Home", 3);
        categories.put("Gaming", 4);

        return categories.get(category);
    }

    public List<Product> getProductsBySearch(String name) {
        // Getting the products from PIM based on the search name.
        String jsonString = pimInterface.getProductsByNameJson(name);
        JSONArray productsJSON = new JSONArray(jsonString);

        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < productsJSON.length(); i++) {
            products.add(parseProduct(productsJSON.getJSONObject(i)));
        }

        return products;
    }

    public Document getPLPLayout() {
        // Return PLP-layout
        return cmsInterface.getPLPLayout();
    }

    public Document getPDPLayout(String category) {
        // Translating the hard-coded categories to their CMS counterparts, and retrieving the layout associated.
        Document document = switch (category) {
            case "Computers & Office" -> cmsInterface.getPDPLayout("computer-kontor");
            case "Phones & Smart Devices" -> cmsInterface.getPDPLayout("mobil-tablet-smartwatch");
            case "TV & Smart Home" -> cmsInterface.getPDPLayout("tv-lyd-smart-home");
            case "Gaming" -> cmsInterface.getPDPLayout("gaming");
            default -> cmsInterface.getPDPLayout("");
        };

        return document;
    }

    public void submitOrder(Order order) {
        // Writing the order as JSON
        String json;
        try {
            json = objectWriter.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Submitting the order to the OMS as a JSON-string
        try {
            omsInterface.submitOrder(json);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Guide> getGuides() {
        // Reading from a local file containing the guides in a JSON file.
        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(new File("src/main/java/SHOP/data/guides/guides.json"))) {
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            JSONArray jsonArray = new JSONArray(sb.toString());
            ArrayList<Guide> guideList = new ArrayList<>();

            // Iterating over the JSONObjects and parsing them into Guide objects.
            for (int i = 0; i < jsonArray.length(); i++) {
                // For each JSONObject, cast to a Guide object.
                guideList.add(objectMapper.readValue(jsonArray.get(i).toString(), Guide.class));
            }
            return guideList;

        } catch (FileNotFoundException | JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getLogo() {
        // Setting up the JSON-string for retrieving the logo from the DAM
        List<String> list = new ArrayList<>();
        list.add("Logo");
        JSONArray tags = new JSONArray(list);
        JSONObject json = new JSONObject();
        json.put("tags", tags);

        // Sending the JSON-string to the DAM and retrieving the path for the logo
        JSONObject returnJson = new JSONObject(damInterface.getImagesByTags(json.toString()));

        System.out.println(returnJson.toString());
        JSONArray jsonArray = returnJson.getJSONArray("images");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        return jsonObject.get("path").toString();
    }
}
