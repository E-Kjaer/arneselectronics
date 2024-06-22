package SHOP.presentation.subcontrollers;

import SHOP.data.models.*;
import SHOP.domain.Basket;
import SHOP.domain.exceptions.OutOfStockException;
import SHOP.presentation.ViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Locale;

public class Basket_Product extends VBox {
    @FXML
    private ImageView productImage;
    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    @FXML
    private TextField productQuantity;
    @FXML
    private Button incrementButton;
    @FXML
    private Button decrementButton;
    @FXML
    private Button removeButton;

    private Product product;

    private int productCount = 0;

    private SHOP.domain.Basket dBasket;

    private SHOP.presentation.subcontrollers.Basket parent;

    public Basket_Product(SHOP.presentation.subcontrollers.Basket parent, Basket dBasket, Product product, int count) {
        this.product = product;
        this.dBasket = dBasket;
        this.productCount = count;
        this.parent = parent;
        System.out.println("Basket-Product Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/basket_product.fxml"));
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
        System.out.println("Basket-Product initialize");
        removeButton.setOnAction(removeProductHandler);
        incrementButton.setOnMouseClicked(incrementHandler);
        decrementButton.setOnMouseClicked(decrementHandler);
        this.productName.setText(product.getName());
        this.productPrice.setText(String.format(Locale.US,"%.2f DKK", product.getPrice()));

        //Sets the image of the product, if the product does not have an image it gets assigned a default image
        if (product.getImage() != null) {
            this.productImage.setImage(new Image("file:" + product.getImage()));
        } else {
            this.productImage.setImage(new Image("file:src/main/resources/SHOP/presentation/subcontrollers/images/toolbox.png"));
        }

        this.productQuantity.setText("" + productCount);
        productQuantity.setOnKeyTyped(pqTypedHandler);
        productQuantity.setOnKeyPressed(pqEnterHandler);
        productQuantity.focusedProperty().addListener(pqFocusListener);
    }

    private EventHandler<ActionEvent> removeProductHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println("Product removed from basket");
            dBasket.clearProduct(product);
            //Updates the products in basket, and updates the counter when a product is removed
            parent.setBasketProducts(dBasket.getAllProducts());
            ViewController.getInstance().updateBasketAmountInHeader();
        }
    };

    private EventHandler<MouseEvent> incrementHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            try {
                dBasket.increment(product);
            } catch (OutOfStockException e) {
                throw new RuntimeException(e);
            }
            // Updates the products in basket, and updates the counter when a product is incremented
            parent.setBasketProducts(dBasket.getAllProducts());
            ViewController.getInstance().updateBasketAmountInHeader();
        }
    };

    private EventHandler<MouseEvent> decrementHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (productCount > 1) {
                try {
                    dBasket.decrement(product);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // Updates the products in basket, and updates the counter when a product is decremented
                parent.setBasketProducts(dBasket.getAllProducts());
                ViewController.getInstance().updateBasketAmountInHeader();
            }
        }
    };


    private ChangeListener<Boolean> pqFocusListener = new ChangeListener<Boolean>() {
        // If you edit the product quantity in the basket, but only click away, this Change listener ensures
        // that the value is still saved
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            // If old value is true then the new value for isFocused must be false, so not in focus
            // and the system then updates the quantity
            if (oldValue) {
                checkAndUpdateQuantity();
            }
        }
    };

    private EventHandler<KeyEvent> pqEnterHandler = new EventHandler<KeyEvent>() {
        // Update quantity with enter handler
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                checkAndUpdateQuantity();
            }
        }
    };

    private void checkAndUpdateQuantity() {
        // Get caretposition since if you change the text then it moves to the start
        // this is to move it back
        int caretPosition = productQuantity.getCaretPosition(); // Get position of caret

        // Try to convert value to int and if an error then set to 1
        int quantity = 1;
        try {
            quantity = Integer.parseInt(productQuantity.getText());
        } catch (NumberFormatException ignored) {
            quantity = product.getStockCount();
        }

        // If entered number is negative then change to 1
        if (quantity < 0 && product.getStockCount() > 0) {
            quantity = 1;
        }

        // If the stock count is 0 then set quantity to 0
        if (product.getStockCount() < 1) {
            quantity = 0;
        }

        // If a digit is entered then check if the new quantity is over or under the stock count
        if (product.getStockCount() < quantity) {
            // If over stock count then reset quantity to the stock count itself
            // and update dBasket as necessary
            dBasket.setProductCount(product, product.getStockCount());
            productQuantity.setText(product.getStockCount() + "");
            productQuantity.positionCaret(caretPosition);
        } else {
            // Accept new quantity if under the stock limit and update dBasket
            dBasket.setProductCount(product, quantity);
            productQuantity.setText(quantity + "");
        }

        // Changes price in basket
        parent.setBasketProducts(dBasket.getAllProducts());
        ViewController.getInstance().updateBasketAmountInHeader();
    }

    private EventHandler<KeyEvent> pqTypedHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            // Get caretposition since if you change the text then it moves to the start
            // this is to move it back
            int caretPosition = productQuantity.getCaretPosition(); // Get position of caret

            // Check for negative index since it can throw ArrayIndexOutOfBounds later
            if (caretPosition < 1) {
                caretPosition = 1;
            }

            // Check for empty array since it can throw ArrayIndexOutOfBounds
            if (productQuantity.getText().isEmpty()) {
                return;
            }

            // Check if newly entered character is not a digit and remove if necessary
            if (!Character.isDigit(productQuantity.getText().toCharArray()[caretPosition - 1])) {
                // If not digit then remove
                productQuantity.setText(productQuantity.getText().replaceAll("\\D", ""));
                // Reset caret back to previous location
                productQuantity.positionCaret(caretPosition - 1);
            }
        }
    };
}

