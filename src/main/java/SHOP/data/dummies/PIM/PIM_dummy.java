package SHOP.data.dummies.PIM;

import SHOP.data.interfaces.PIMInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.*;

public class PIM_dummy implements PIMInterface {
    @Override
    public String getProductByIdJson(int productId) {
        //Reads the JSON file and returns product IDs
        String json = "";
        try (Scanner scanner = new Scanner(new File("src/main/java/SHOP/data/dummies/PIM/products.json"))) {
            String result = "";
            while (scanner.hasNextLine()) {
                json += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }
        JSONArray productArr = new JSONArray(json);
        return productArr.getJSONObject(productId - 1).toString();
    }

    @Override
    public String getProductsJson() {
        //Reads products JSON file and returns the products
        try (Scanner scanner = new Scanner(new File("src/main/java/SHOP/data/dummies/PIM/products.json"))) {
            String result = "";
            while (scanner.hasNextLine()) {
                result += scanner.nextLine();
            }
            return result;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    @Override
    public String getCategoriesJson() {
        return "";
    }

    @Override
    public String getSubcategoriesByCategoryIdJson(int categoryId) {
        return "";
    }

    @Override
    public String getBrandsJson() {
        return "";
    }

    @Override
    public String getProductsByCategoryJson(int category) {
        //Reads the products JSON file and gets the products from each category
        try (Scanner scanner = new Scanner(new File("src/main/java/SHOP/data/dummies/PIM/products.json"))) {
            String result = "";
            while (scanner.hasNextLine()) {
                result += scanner.nextLine();
            }
            return result;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    @Override
    public String getProductsByNameJson(String name) {
        //Reads the products JSON file and gets the products by name
        try (Scanner scanner = new Scanner(new File("src/main/java/SHOP/data/dummies/PIM/products.json"))) {
            String result = "";
            while (scanner.hasNextLine()) {
                result += scanner.nextLine();
            }
            JSONArray productArr = new JSONArray(result);
            JSONArray resultArr = new JSONArray();
            Pattern searchPattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
            for (int i = 0; i < productArr.length(); i++) {
                if (searchPattern.matcher(productArr.getJSONObject(i).getString("name")).find()) {
                    resultArr.put(productArr.getJSONObject(i));
                }
            }
            return resultArr.toString();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
}
