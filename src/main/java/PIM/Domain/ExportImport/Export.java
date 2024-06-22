package PIM.Domain.ExportImport;

import PIM.Data.Crud.RetrieveJSON;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Export {
    RetrieveJSON retrieveJSON = new RetrieveJSON();
    String filename = "products.json";

    public void saveProductList(String fileName) {
        try {
            // Get the absolute path of the file
            String absolutePath = new File(filename).getAbsolutePath();
            System.out.println("File will be created at: " + absolutePath);

            // Create a FileWriter with append mode (second argument is true)
            FileWriter writer = new FileWriter(filename, true);

            // Append data to the file
            writer.write(retrieveJSON.getProductsJson());

            // Close the writer
            writer.close();

            System.out.println("Data appended to the file successfully.");
        } catch (IOException e) {
            System.err.println("Error appending to the file: " + e.getMessage());
        }
    }
}