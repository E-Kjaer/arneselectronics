package SHOP.presentation.subcontrollers;

import SHOP.data.models.Product;
import SHOP.domain.Basket;
import SHOP.domain.exceptions.InvalidProductException;
import SHOP.domain.exceptions.OutOfStockException;
import SHOP.presentation.ViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class PDP extends AnchorPane {
    @FXML
    private Label productName;
    @FXML
    private Text productPrice;
    @FXML
    private Text productPriceNoVAT;
    @FXML
    private VBox priceBox;
    @FXML
    private Text productDescription;
    @FXML
    private ImageView productImage;
    @FXML
    private Button basketButton;
    @FXML
    private TextField productQuantity;
    @FXML
    private Button incrementButton;
    @FXML
    private Button decrementButton;
    @FXML
    private Button shareButton;
    @FXML
    private VBox addToBasketBox;

    private Product product;
    private Basket basket;

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public PDP() {
        System.out.println("PDP-Page Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/pdp_v2.fxml"));
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
        System.out.println("PDP-Page Initialized");

        productImage.setImage(new Image("file:src/main/resources/SHOP/presentation/subcontrollers/images/toolbox.png"));

        //Setting eventhandlers for buttons
        basketButton.setOnAction(addProductToBasket);
        incrementButton.setOnMouseClicked(incrementHandler);
        decrementButton.setOnMouseClicked(decrementHandler);
        shareButton.setOnAction(shareHandler);
        productQuantity.setOnKeyTyped(pqTypedHandler);
    }

    public void setProductName(String productName) {
        this.productName.setText(productName);
    }

    public void setProductPrice(float productPrice) {
        this.productPrice.setText(String.format(Locale.US,"%.2f DKK", productPrice));
    }

    public void setProductPriceNoVAT(float productPriceNoVAT) {
        this.productPriceNoVAT.setText(String.format(Locale.US, "w/o VAT: %.2f DKK", productPriceNoVAT));
    }

    public void setProductDescription(String productDescription) {
        this.productDescription.setText(productDescription);
    }

    public void setProductImage(String imageURL) {
        this.productImage.setImage(new Image("file:" + imageURL));
    }

    // Setting the name, price and description of the product.
    public void showProduct() {
        setProductName(product.getName());
        setProductPrice(product.getPrice());
        setProductPriceNoVAT(product.getPrice() * 0.8f);
        setProductDescription(product.getDescription());
        if (product.getImage() != null) setProductImage(product.getImage());
    }

    private EventHandler<MouseEvent> incrementHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            // Checking that incrementing the amount doesn't exceed the amount available, and incrementing.
            int quantity = Integer.parseInt(productQuantity.getText());
            if (quantity < getMaximumProductQuantity()) {
                productQuantity.setText(String.valueOf(quantity + 1));
            }
            System.out.println("Added " + productQuantity.getText() + " " + productName.getText());
            getMaximumProductQuantity();
        }
    };

    private EventHandler<MouseEvent> decrementHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            int quantity = Integer.parseInt(productQuantity.getText());
            if (quantity > 1) {
                productQuantity.setText(String.valueOf(quantity - 1));
            }
            System.out.println("Removed " + productQuantity.getText() + " " + productName.getText());
            getMaximumProductQuantity();
        }
    };

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

            if ((Integer.parseInt(productQuantity.getText())) > getMaximumProductQuantity()) {
                productQuantity.setText(String.valueOf(getMaximumProductQuantity()));
            } else if (Integer.parseInt(productQuantity.getText()) < 0) {
                productQuantity.setText(String.valueOf(0));
            }
        }
    };

    private int getMaximumProductQuantity() {
        // Getting the stockcount and subtracting by amounts already in the basket.
        int maxPQ = product.getStockCount();
        for (Map.Entry<Product, Integer> entry: basket.getAllProducts().entrySet()) {
            if (entry.getKey().equals(product)) {
                maxPQ -= entry.getValue();
                break;
            }
        }

        return maxPQ;
    }

    // Adding products to basket and updating the basketAmount.
    private EventHandler<ActionEvent> addProductToBasket = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("Add to basket!!!");
            System.out.println(product.getName());

            try {
                basket.addToBasket(product,Integer.parseInt(productQuantity.getText()));
            } catch (InvalidProductException | OutOfStockException e) {
                System.out.println("Not in stock");
            }
            ViewController.getInstance().updateBasketAmountInHeader();
            if (Integer.valueOf(productQuantity.getText()) > getMaximumProductQuantity()) {
                productQuantity.setText(String.valueOf(getMaximumProductQuantity()));
            }
        }
    };

    private EventHandler<ActionEvent> shareHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            // Generating the url
            String url = "https://www.arneselectronics.com/products/" + product.getId();

            // Storing the url in the clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(url);
            clipboard.setContent(content);
        }
    };

    // Adjusting the layout of the fields based on the received BSON document
    public void setLayout(Document layoutBSON) {
        setProductNameLayout(layoutBSON);
        setProductDescriptionLayout(layoutBSON);
        setProductPriceLayout(layoutBSON);
        setAddToCartLayout(layoutBSON);
        setImageLayout(layoutBSON);
        setBackgroundColor(layoutBSON);
    }

    private void setBackgroundColor(Document layoutBSON) {
        String color = (String) layoutBSON.get("backgroundColor");
        String colorCode = "#" + color.substring(2);
        this.setStyle("-fx-background-color: " + colorCode + ";");
    }

    private void setProductNameLayout(Document layoutBSON) {
        int x = layoutBSON.getDouble("titleX").intValue();
        int y = layoutBSON.getDouble("titleY").intValue();
        String fontType = (String) layoutBSON.get("titleFontType");
        int fontSize = layoutBSON.getDouble("titleFontSize").intValue();
        String fontColor = (String) layoutBSON.get("titleFontColor");

        productName.setLayoutX(x);
        productName.setLayoutY(y);
        productName.setFont(new Font(fontType,fontSize));
        productName.setTextFill(Paint.valueOf(fontColor));
    }

    private void setProductDescriptionLayout(Document layoutBSON) {
        int x = layoutBSON.getDouble("descriptionX").intValue();
        int y = layoutBSON.getDouble("descriptionY").intValue();
        String fontType = (String) layoutBSON.get("descriptionFontType");
        int fontSize = layoutBSON.getDouble("descriptionFontSize").intValue();
        String fontColor = (String) layoutBSON.get("descriptionFontColor");

        productDescription.setLayoutX(x);
        productDescription.setLayoutY(y);
        productDescription.setFont(new Font(fontType,fontSize));
        productDescription.setFill(Paint.valueOf(fontColor));
    }

    private void setProductPriceLayout(Document layoutBSON) {
        int x = layoutBSON.getDouble("priceX").intValue();
        int y = layoutBSON.getDouble("priceY").intValue();
        String fontType = (String) layoutBSON.get("priceFontType");
        int fontSize = layoutBSON.getDouble("priceFontSize").intValue();
        String fontColor = (String) layoutBSON.get("priceFontColor");

        priceBox.setLayoutX(x);
        priceBox.setLayoutY(y);
        productPrice.setFont(new Font(fontType,fontSize));
        productPrice.setFill(Paint.valueOf(fontColor));
        productPriceNoVAT.setFont(new Font(fontType,fontSize));
        productPriceNoVAT.setFill(Paint.valueOf(fontColor));
    }

    private void setImageLayout(Document layoutBSON) {
        int x = layoutBSON.getDouble("pictureX").intValue();
        int y = layoutBSON.getDouble("pictureY").intValue();

        productImage.setLayoutX(x);
        productImage.setLayoutY(y);
    }

    private void setAddToCartLayout(Document layoutBSON) {
        int x = layoutBSON.getDouble("addToCartX").intValue();
        int y = layoutBSON.getDouble("addToCartY").intValue();
        String fontType = (String) layoutBSON.get("addToCartFontType");
        int fontSize = layoutBSON.getDouble("addToCartFontSize").intValue();
        String fontColor = (String) layoutBSON.get("addToCartFontColor");

        addToBasketBox.setLayoutX(x);
        addToBasketBox.setLayoutY(y);
        basketButton.setFont(new Font(fontType,fontSize));
        basketButton.setTextFill(Paint.valueOf(fontColor));
        shareButton.setFont(new Font(fontType,fontSize));
        shareButton.setTextFill(Paint.valueOf(fontColor));
    }
}
