package SHOP.presentation.subcontrollers;

import SHOP.domain.order.Payment;
import SHOP.domain.order.Shipping;
import SHOP.presentation.ViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class Company_Checkout extends HBox {
    @FXML
    private TextField city;
    @FXML
    private TextField email;
    @FXML
    private TextField fNameText;
    @FXML
    private TextField houseNumberText;
    @FXML
    private TitledPane informationPane;
    @FXML
    private TextField lNameText;
    @FXML
    private RadioButton option1Radio;
    @FXML
    private RadioButton option2Radio;
    @FXML
    private ToggleButton payment1;
    @FXML
    private ToggleButton payment2;
    @FXML
    private TitledPane paymentId;
    @FXML
    private TextField phone;
    @FXML
    private TitledPane shippingPane;
    @FXML
    private TextField streetNameText;
    @FXML
    private TextField zip;
    @FXML
    private Button confirmButton;

    @FXML
    private TextField companyName;

    @FXML
    private TextField cvr;

    @FXML
    private ToggleGroup paymentGroup = new ToggleGroup();
    private ToggleGroup shippingGroup = new ToggleGroup();

    private SHOP.domain.Basket basket;

    public Company_Checkout() {
        System.out.println("Company_Checkout Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/checkout_company.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exc) {
            // handle exception
            System.out.println(exc);
        }
    }

    public void setBasket(SHOP.domain.Basket basket) {
        this.basket = basket;
    }

    public void initialize() {
        confirmButton.setVisible(false);
        confirmButton.setOnAction(confirmHandler);

        //Set ToggleGroups
        payment1.setToggleGroup(paymentGroup);
        payment2.setToggleGroup(paymentGroup);

        option1Radio.setToggleGroup(shippingGroup);
        option2Radio.setToggleGroup(shippingGroup);

        //Set handlers
        payment1.setOnAction(paymentHandler);
        payment2.setOnAction(paymentHandler);
    }

    //Handler for payment -> when a payment is chosen, the confirm button becomes visible
    private EventHandler<ActionEvent> paymentHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            confirmButton.setVisible(true);
            if (paymentGroup.getSelectedToggle() == null) {
                confirmButton.setVisible(false);
            }
        }
    };
    //Handler for confirm button
    private EventHandler<ActionEvent> confirmHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println("Confirm Button clicked");
            ToggleButton chosenPayment = (ToggleButton) paymentGroup.getSelectedToggle();
            RadioButton chosenShipping = (RadioButton) shippingGroup.getSelectedToggle();
            //Check if the information is filled in
            if (chosenPayment == null || chosenShipping == null || fNameText.getText().isEmpty() || lNameText.getText().isEmpty() || streetNameText.getText().isEmpty() || houseNumberText.getText().isEmpty() || zip.getText().isEmpty() || city.getText().isEmpty() || phone.getText().isEmpty() || email.getText().isEmpty() || cvr.getText().isEmpty() || companyName.getText().isEmpty()) {
                System.out.println("Abort order");
                return;
            }
            //Create payment and shipping object, insert in submit order
            Payment payment = new Payment(chosenPayment.getText());
            Shipping shipping = new Shipping(
                    fNameText.getText() + " " + lNameText.getText(),
                    streetNameText.getText() + " " + houseNumberText.getText() + ", " + zip.getText() + " " + city.getText(),
                    Integer.parseInt(phone.getText()),
                    email.getText(),
                    chosenShipping.getText(),
                    cvr.getText(),
                    companyName.getText()
            );
            basket.submitOrder(true, payment, shipping);
            basket.clear();
            ViewController.getInstance().showHomePage();
            ViewController.getInstance().updateBasketAmountInHeader();
        }
    };
}

