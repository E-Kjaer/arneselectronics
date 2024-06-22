package SHOP.presentation.subcontrollers;

import SHOP.presentation.ViewController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

public class PLP_Product extends AnchorPane {
    @FXML
    private Text productDescription;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Text productPrice;

    private int productID;

    public PLP_Product() {
        // Loading the FXML file for the PLP-product
        System.out.println("PLP-Product Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/plp_product.fxml"));
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
        System.out.println("PLP-Product Initialized");
        // Add eventhandler for showing the PDP page for this product.
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, pdp);
        productImage.setImage(new Image("file:src/main/resources/SHOP/presentation/subcontrollers/images/toolbox.png"));
    }

    private EventHandler<MouseEvent> pdp = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ViewController.getInstance().showPDP(productID);
        }
    };

    public void setProductImage(String productImage) {
        this.productImage.setImage(new Image("file:" + productImage));
    }

    public void setProductName(String productName) {
        this.productName.setText(productName);
    }

    public void setProductPrice(float productPrice) {
        this.productPrice.setText("" + productPrice);
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setLayout(Document layoutBSON) {
        // Setting the layout of the PLP-Product based on the received BSON Document
        setProductNameLayout(layoutBSON);
        setProductPriceLayout(layoutBSON);
        setImageLayout(layoutBSON);
    }

    private void setProductNameLayout(Document layoutBSON) {
        // Setting font, position and color of the product name
        int x = layoutBSON.getDouble("titleplpX").intValue();
        int y =  layoutBSON.getDouble("titleplpY").intValue();
        String fontType = (String) layoutBSON.get("titleplpFontType");
        int fontSize = layoutBSON.getDouble("titleplpFontSize").intValue();
        String fontColor = (String) layoutBSON.get("titleplpFontColor");

        productName.setLayoutX(x);
        productName.setLayoutY(y);
        productName.setFont(new Font(fontType,fontSize));
        productName.setTextFill(Paint.valueOf(fontColor));
    }

    private void setProductPriceLayout(Document layoutBSON) {
        // Setting font, position and color of the product price
        int x = layoutBSON.getDouble("priceplpX").intValue();
        int y = layoutBSON.getDouble("priceplpY").intValue();
        String fontType = (String) layoutBSON.get("priceplpFontType");
        int fontSize = layoutBSON.getDouble("priceplpFontSize").intValue();
        String fontColor = (String) layoutBSON.get("priceplpFontColor");

        productPrice.setLayoutX(x);
        productPrice.setLayoutY(y);
        productPrice.setFont(new Font(fontType,fontSize));
        productPrice.setFill(Paint.valueOf(fontColor));
    }

    private void setImageLayout(Document layoutBSON) {
        // Setting position for the product image
        int x = layoutBSON.getDouble("pictureplpX").intValue();
        int y = layoutBSON.getDouble("pictureplpY").intValue();

        productImage.setLayoutX(x);
        productImage.setLayoutY(y);
    }
}

