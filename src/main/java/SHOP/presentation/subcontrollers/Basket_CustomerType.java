package SHOP.presentation.subcontrollers;

import SHOP.presentation.ViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Basket_CustomerType extends VBox {

    @FXML
    Button customerButton1;
    @FXML
    Button customerButton2;

    public Basket_CustomerType() {
        System.out.println("basket_CustomerType Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/basket_CustomerType.fxml"));
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
        customerButton1.setOnAction(customerButtonHandler);
        customerButton2.setOnAction(customerButtonHandler);
    }

    private EventHandler<ActionEvent> customerButtonHandler = new EventHandler<ActionEvent>() {
        // Handles the two customer types, company or private.
        @Override
        public void handle(ActionEvent actionEvent) {
            Button buttonClicked = (Button) actionEvent.getSource();
            if ((actionEvent.getSource() == customerButton1)) {
                // Calls the appropriate method in ViewController to show the customer checkout page
                ViewController.getInstance().showCustomerCheckout();

            } else if (actionEvent.getSource() == customerButton2) {
                // Calls the appropriate method in ViewController to show the company checkout page
                ViewController.getInstance().showCompanyCheckout();
            }
        }
    };
}