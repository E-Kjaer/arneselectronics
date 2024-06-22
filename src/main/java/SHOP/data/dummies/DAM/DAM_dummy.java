package SHOP.data.dummies.DAM;

import DAM.g1.domain.interfacemanagement.DAMToShop.DAMTOSHOPInterface;
import SHOP.data.interfaces.DAMInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DAM_dummy implements DAMTOSHOPInterface {
    @Override
    public String getImagesByTags(String tags) {
        // Returns a dummy JSON object "Images.json" which contains the path for the Logo
        try (Scanner scanner = new Scanner(new File("src/main/java/SHOP/data/dummies/DAM/images.json"))) {
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
}
