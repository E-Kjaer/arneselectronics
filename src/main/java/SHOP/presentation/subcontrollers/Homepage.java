package SHOP.presentation.subcontrollers;

import SHOP.data.models.Product;
import SHOP.domain.Domain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class Homepage extends VBox {

    @FXML
    ImageView logo;

    @FXML
    GridPane highlightedProducts;

    public void initialize() {
    }

    public void setLogo(String image) {
        logo.setImage(new Image("file:" + image));
    }

    // Method for loading the Homepage FXML
    public Homepage() {
        System.out.println("Homepage Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/homepage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exc) {
            // handle exception
            System.out.println(exc);
        }
    }

    // Method for getting products and displaying them on the homepage
    public void loadHighlightedProducts(Domain domain, int count) {
        // Clearing old products
        highlightedProducts.getChildren().clear();
        // Making an arraylist of new products
        ArrayList<Product> products = domain.getProductsByCategory("Phones & Smart Devices");
        // instantiating plpProduct and setting up the products layout
        for (int i = 0; i < count; i++) {
            PLP_Product plpProduct = new PLP_Product();
            plpProduct.setProductID(products.get(i).getId());
            plpProduct.setProductName(products.get(i).getName());
            plpProduct.setProductPrice(products.get(i).getPrice());
            if (products.get(i).getImage() != null) plpProduct.setProductImage(products.get(i).getImage());
            plpProduct.setLayout(domain.getPLPLayout());

            highlightedProducts.add(plpProduct, i, 0);
        }
    }
}
