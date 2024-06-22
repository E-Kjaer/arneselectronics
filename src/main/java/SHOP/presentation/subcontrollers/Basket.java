package SHOP.presentation.subcontrollers;

import SHOP.presentation.ViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import SHOP.data.models.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Basket extends VBox {
    @FXML
    private VBox basketProducts;
    @FXML
    private Label priceNoVAT;
    @FXML
    private Label priceVAT;
    @FXML
    private Label totalPrice;
    @FXML
    private Button clearButton;

    private SHOP.domain.Basket dBasket;

    public Basket(SHOP.domain.Basket dBasket) {
        this.dBasket = dBasket;
        System.out.println("Basket Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/basket.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exc) {
            // handle exception
            System.out.println(exc);
        }
    }

    public void initialize() {
        System.out.println("Basket initialize");
        clearButton.setOnAction(clearButtonHandler);
        setBasketProducts(dBasket.getAllProducts());
    }

    private EventHandler<ActionEvent> clearButtonHandler = new EventHandler<ActionEvent>() {
        // Clears the basket when the 'Clear Basket' button is pressed
        @Override
        public void handle(ActionEvent actionEvent) {
            dBasket.clear();
            // Updates the products
            setBasketProducts(dBasket.getAllProducts());
            // Updates the product counter
            ViewController.getInstance().updateBasketAmountInHeader();
        }
    };


    public void setBasketProducts(Map<Product, Integer> allProducts) {
        // Clears all products that already exists in basket
        basketProducts.getChildren().clear();

        System.out.println("*SETTING BASKET PRODUCTS*");

        // Updates all products based on the map from the arguments
        for (Map.Entry<Product, Integer> entry : allProducts.entrySet()) {
            Basket_Product basketProduct = new Basket_Product(this, dBasket, entry.getKey(), entry.getValue());
            basketProducts.getChildren().add(basketProduct);
        }

        //Updates the prices in the basket
        float price = dBasket.getTotalValue();
        totalPrice.setText(String.format(Locale.US,"%.2f DKK", price ));
        priceNoVAT.setText(String.format(Locale.US,"%.2f DKK", price * 0.8));
        priceVAT.setText(String.format(Locale.US,"%.2f DKK", price * 0.2));
    }
}
