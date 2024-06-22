package CMS;

import com.mongodb.client.model.Filters;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bson.Document;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import static java.lang.Integer.parseInt;
import static java.lang.Math.round;

public class EditPageController {


    private String[] categories = {"computer-kontor","mobil-tablet-smartwatch","tv-lyd-smart-home","gaming"};
    private String[] fonts = {"Calibri","System", "Georgia"};


    String selectedCategory = "", font, selectedFont = "";

    boolean plp = false;

    double fontSize;

    @FXML
    private ChoiceBox<String> chooseCategory, choosefont;

    @FXML
    private TextArea xValue, yValue;

    @FXML
    private Label price, textTitle, productId, productId2, description,
            titleXLabel, titleYLabel, textTitlePLP, pricePLP, productInfo;

    @FXML
    private ImageView imagePLP, image, returnButton;

    @FXML
    private Slider mySlider;

    @FXML
    private ColorPicker colorpicker, colorpicker2;

    @FXML
    private Rectangle background, backgroundPLP, chosenBackground = (Rectangle) background;

    @FXML
    private Button addToCart, switchButton1, saveButton;

    @FXML
    private Pane contentPane, plpPane, chosenpane;


    @FXML
    public Node clicked = (Label) textTitle;


    @FXML
    public void initialize() {
        description.setWrapText(true);
        description.setText("\"Lorem ipsum dolor sit amet, consectetur " +
                "adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et " +
                "dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis " +
                "nostrud exercitation " +
                "ullamco laboris nisi ut aliquip ex " +
                "ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in " +
                "voluptate velit esse cillum dolore eu " +
                "adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et " +
                "dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis " +
                "nostrud exercitation " +
                "ullamco laboris nisi ut aliquip ex " +
                "ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in " +
                "voluptate velit esse cillum dolore eu ");
        String id = DataMethods.getClickedContentPage();

        // Check if a document with the same ID exists
        Document existingDocumentPDP = MongoDBMethods.getCollection().find(Filters.eq("_id", id)).first();
        Document existingDocumentPLP = MongoDBMethods.getCollection().find(Filters.eq("_id", "plp")).first();
        scanForDocuments(existingDocumentPDP);
        scanForDocuments(existingDocumentPLP);

        clicked = (Label) textTitle;
        choosefont.getItems().addAll(fonts);
        choosefont.setOnAction(this::getFont);
        chooseCategory.getItems().addAll(categories);
        chooseCategory.setOnAction(this::getCategory);
        if (existingDocumentPDP != null) {
            chooseCategory.setValue(existingDocumentPDP.get("category").toString());
            choosefont.setValue(ControllerLogic.clickedFont(fonts,existingDocumentPDP.get("titleFontType").toString()));
        }
        updateTextPosition();
        xValue.setTextFormatter(formatterX);
        yValue.setTextFormatter(formatterY);
    }

    private void getCategory(ActionEvent event){
        selectedCategory = chooseCategory.getValue();
    }

    private void scanForDocuments( Document existingDocument) {
        if (existingDocument != null) {
            System.out.println("Document found:");
            for (String key : existingDocument.keySet()) {
                Object value = existingDocument.get(key);
                System.out.println(key + ": " + value);
            }

            // List of element names
            List<String> elementNames = Arrays.asList("title","titleplp","description",
                    "price", "priceplp", "addToCart", "picture", "pictureplp",
                    "productId", "productId2", "productInfo");

            for (String elementName : elementNames) {
                // Layout positions
                if (existingDocument.get(elementName + "X") != null && existingDocument.get(elementName + "Y") != null) {
                    String elementXString = existingDocument.get(elementName + "X").toString();
                    String elementYString = existingDocument.get(elementName + "Y").toString();
                    double elementX = Double.parseDouble(elementXString);
                    double elementY = Double.parseDouble(elementYString);

                    // Get UI element based on name
                    Object element = switch (elementName) {
                        case "title" -> textTitle;
                        case "description" -> description;
                        case "price" -> price;
                        case "addToCart" -> addToCart;
                        case "picture" -> image;
                        case "productId" -> productId;
                        case "productId2" -> productId2;
                        case "productInfo" -> productInfo;
                        case "titleplp" -> textTitlePLP;
                        case "priceplp" -> pricePLP;
                        case "pictureplp" -> imagePLP;
                        default -> null;
                    };

                    // Set layout positions
                    if (element != null && element != image && element != imagePLP) {
                        ((Node) element).setLayoutX(elementX);
                        ((Node) element).setLayoutY(elementY);
                    } else if (element != null && element == image || element == imagePLP){
                        ((ImageView) element).setX(elementX);
                        ((ImageView) element).setY(elementY);
                    } else {
                        // Handle the case where element is null
                        System.out.println("Element not found for: " + elementName);
                    }

                    // Styles
                    if (element != image  &&  element != imagePLP) {
                        String elementFontType = existingDocument.get(elementName + "FontType").toString();
                        double elementFontSize = Double.parseDouble(existingDocument.get(elementName + "FontSize").toString());
                        String elementStyle = "-fx-font-family: " + elementFontType + "; -fx-font-size: " + elementFontSize + ";";
                        // Set styles and color
                        if (element instanceof Labeled) {
                            ((Labeled) element).setStyle(elementStyle);
                            ((Labeled) element).setTextFill(Color.web(existingDocument.get(elementName + "FontColor").toString()));
                        } else if (element instanceof TextArea) {
                            ((TextArea) element).setStyle(elementStyle);
                        } else {
                            // Handle the case where textTitle is not a Labeled or a Textarea element
                            System.out.println("Unexpected element type for " + elementName + " Font style not applied.");
                        }
                    }
                } else {
                    System.out.println("Layout position not found for: " + elementName + "X and Y");
                }
            }

            // Background color

            if (existingDocument.get("_id") != "plp" && existingDocument.get("backgroundColor") != null) {
                String backgroundColor = existingDocument.get("backgroundColor").toString();
                background.setFill(Color.web(backgroundColor));
            } else {
                String backgroundColor = existingDocument.get("backgroundplpColor").toString();
                System.out.println(backgroundColor + "!");
                backgroundPLP.setFill(Color.web(backgroundColor));
            }
        }
    }



    private boolean isArrowKey(TextFormatter.Change change) {
        KeyCode keyCode = change.getControlNewText().charAt(0) <= 0 ? null : KeyCode.getKeyCode(String.valueOf(change
                .getControlNewText().charAt(0)));
        return keyCode != null && (keyCode.isArrowKey());
    }

    // Filter for numeric characters only
    UnaryOperator<TextFormatter.Change> filter = change -> {
        String text = change.getText();
        if (text.matches("[0-9]") || text.isEmpty() || change.isDeleted()
                || isArrowKey(change)) {
            return change;
        }
        return null;
    };

    // Text formatter with filter and converter
    TextFormatter<String> formatterX = new TextFormatter<>(filter);
    TextFormatter<String> formatterY = new TextFormatter<>(filter);

    public void updateTextPosition() {

        titleXLabel.setText( clicked.getId() + " x: ");
        xValue.setText(String.valueOf(clicked.getLayoutX()));
        titleYLabel.setText( clicked.getId() + " y :");
        yValue.setText(String.valueOf(clicked.getLayoutY()));
    }

    public void Dragging(MouseEvent event){
        chosenBackground =  background;
        ImageView choosenimage = image;
        chosenpane = contentPane;

        if(plp == true){
            choosenimage = (ImageView) imagePLP;
            chosenBackground = (Rectangle) backgroundPLP;
            chosenpane = (Pane) plpPane;
        }

        if(choosenimage.getX() >= 0 && choosenimage.getX() + choosenimage.getFitWidth() <= chosenBackground.getHeight()){


            choosenimage.setX(event.getSceneX() - choosenimage.getFitWidth());
        }
        if(choosenimage.getX() < 0){
            choosenimage.setX(0);
        }
        if(choosenimage.getX() + choosenimage.getFitWidth() > chosenBackground.getWidth()){
            choosenimage.setX(chosenBackground.getWidth() - choosenimage.getFitWidth());
        }


        if(choosenimage.getY() >= 0 && choosenimage.getY() + choosenimage.getFitHeight() <= chosenBackground.getHeight()){


            choosenimage.setY(event.getSceneY() - choosenimage.getFitHeight());
        }
        if(choosenimage.getY() < 0){
            choosenimage.setY(0);
        }
        if(choosenimage.getY() + choosenimage.getFitHeight() > chosenBackground.getHeight()){
            choosenimage.setY(chosenBackground.getHeight() - choosenimage.getFitHeight());
        }
    }

    public void getFont(ActionEvent event){

        selectedFont = choosefont.getValue();
        System.out.println(selectedFont);
        clicked.setStyle("-fx-font-family:" + selectedFont + "; -fx-font-size:" + fontSize +";");

    }


    public void onColor2(){
        String id = clicked.getId();
        if(id.contains("add")){
            final Button source = (Button) addToCart;
            source.setTextFill(colorpicker2.getValue());
        }
        else{
            final Label source = (Label) clicked;
            source.setTextFill(colorpicker2.getValue());
        }
        {

        }
    }

    public void onSlide(){
        fontSize = mySlider.getValue();
        clicked.setStyle("-fx-font-family:" + font + "; -fx-font-size:" + fontSize +";");

    }

    public void onColor(){
        chosenBackground = background;
        if(plp == true){
            chosenBackground = backgroundPLP;
        }
        chosenBackground.setFill(colorpicker.getValue());
    }




    public void getClicked(MouseEvent event){


        if( event.getSource().getClass().toString().contains("Button")){
            clicked = (Button) event.getSource();

        }

        if( event.getSource().getClass().toString().contains("Label")){
            clicked = (Label) event.getSource();
                    choosefont.setValue(ControllerLogic.clickedFont(fonts,clicked.getStyle()));


         updateTextPosition();

    }
    }


    public void onReturnButtonClick(MouseEvent event) {
        try {
            System.out.println(DataMethods.getCreatedContentPagesList());
            // Get a reference to the main stage
            Stage stage = (Stage) returnButton.getScene().getWindow();

            // Load the FXML file for the HelloController
            FXMLLoader loader = new FXMLLoader(MainCMS.class.getResource("adminpage.fxml"));
            Parent root = loader.load();

            // Get the HelloController instance
            AdminPageController adminController = loader.getController();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the new scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save_data(){
        double titleX = textTitle.getLayoutX();
        double titleY = textTitle.getLayoutY();
        String titleFontType = textTitle.getFont().getName();
        double titleFontSize = textTitle.getFont().getSize();
        String titleFontColor = textTitle.getTextFill().toString();
        double descriptionX = description.getLayoutX();
        double descriptionY = description.getLayoutY();
        String descriptionFontType = description.getFont().getName();
        double descriptionFontSize = description.getFont().getSize();
        String descriptionFontColor = description.getTextFill().toString();
        double priceX = price.getLayoutX();
        double priceY = price.getLayoutY();
        String priceFontType = price.getFont().getName();
        double priceFontSize = price.getFont().getSize();
        String priceFontColor = price.getTextFill().toString();
        double addToCartX = addToCart.getLayoutX();
        double addToCartY = addToCart.getLayoutY();
        String addToCartFontType = addToCart.getFont().getName();
        double addToCartFontSize = addToCart.getFont().getSize();
        String addToCartFontColor = addToCart.getTextFill().toString();
        double pictureX = image.getX();
        double pictureY = image.getY();
        String backgroundColor = background.getFill().toString();
        double pictureplpX = imagePLP.getX();
        double pictureplpY = imagePLP.getY();
        String backgroundColorplp = backgroundPLP.getFill().toString();
        double titleXplp = textTitlePLP.getLayoutX();
        double titleYplp = textTitlePLP.getLayoutY();
        String titleplpFontType = textTitlePLP.getFont().getName();
        double titleplpFontSize = textTitlePLP.getFont().getSize();
        String titleplpFontColor = textTitlePLP.getTextFill().toString();
        double priceplpX = pricePLP.getLayoutX();
        double priceplpY = pricePLP.getLayoutY();
        String priceplpFontType = pricePLP.getFont().getName();
        double priceplpFontSize = pricePLP.getFont().getSize();
        String priceplpFontColor = pricePLP.getTextFill().toString();
        double productIdX = productId.getLayoutX();
        double productIdY = productId.getLayoutY();
        String productIdFontType = productId.getFont().getName();
        double productIdFontSize = productId.getFont().getSize();
        String productIdFontColor = productId.getTextFill().toString();
        String category = selectedCategory;

        System.out.println("Saving Data...");

        Document cmsPDP = new Document("_id", DataMethods.getClickedContentPage())
                .append("titleX", titleX)
                .append("titleY", titleY)
                .append("titleFontType", titleFontType)
                .append("titleFontSize", titleFontSize)
                .append("titleFontColor", titleFontColor)
                .append("descriptionX", descriptionX)
                .append("descriptionY", descriptionY)
                .append("descriptionFontType", descriptionFontType)
                .append("descriptionFontSize", descriptionFontSize)
                .append("descriptionFontColor", descriptionFontColor)
                .append("priceX", priceX)
                .append("priceY", priceY)
                .append("priceFontType", priceFontType)
                .append("priceFontSize", priceFontSize)
                .append("priceFontColor", priceFontColor)
                .append("addToCartX", addToCartX)
                .append("addToCartY", addToCartY)
                .append("addToCartFontType", addToCartFontType)
                .append("addToCartFontSize", addToCartFontSize)
                .append("addToCartFontColor", addToCartFontColor)
                .append("pictureX", pictureX)
                .append("pictureY", pictureY)
                .append("backgroundColor", backgroundColor)
                .append("productIdX", productIdX)
                .append("productIdY", productIdY)
                .append("productIdFontType", productIdFontType)
                .append("productIdFontSize", productIdFontSize)
                .append("productIdFontColor", productIdFontColor)
                .append("category", selectedCategory)
                .append("type", "pdp");

        Document cmsPLP = new Document("_id", "plp")
                .append("titleplpX", titleXplp)
                .append("titleplpY", titleYplp)
                .append("titleplpFontType", titleplpFontType)
                .append("titleplpFontSize", titleplpFontSize)
                .append("titleplpFontColor", titleplpFontColor)
                .append("priceplpX", priceplpX)
                .append("priceplpY", priceplpY)
                .append("priceplpFontType", priceplpFontType)
                .append("priceplpFontSize", priceplpFontSize)
                .append("priceplpFontColor", priceplpFontColor)
                .append("pictureplpX", pictureplpX)
                .append("pictureplpY", pictureplpY)
                .append("backgroundplpColor", backgroundColorplp)
                .append("type", "plp");

        String idPDP = cmsPDP.getString("_id");
        String idPLP = cmsPLP.getString("_id");

        // Save documents
        MongoDBMethods.saveToCollectionMethod(cmsPDP, idPDP);
        MongoDBMethods.saveToCollectionMethod(cmsPLP, idPLP);
    }

    public void onTitleXPlus(ActionEvent actionEvent) {
        double xPositionPlus = clicked.getLayoutX()+1;
        clicked.setLayoutX(xPositionPlus);
        updateTextPosition();
    }

    public void onTitleXMinus(ActionEvent actionEvent) {
        double xPositionMinus = clicked.getLayoutX()-1;
        clicked.setLayoutX(xPositionMinus);
        updateTextPosition();
    }

    public void onTitleYPlus(ActionEvent actionEvent) {
        double yPositionPlus = clicked.getLayoutY()+1;
        clicked.setLayoutY(yPositionPlus);
        updateTextPosition();
    }

    public void onTitleYMinus(ActionEvent actionEvent) {
        double yPositionMinus = clicked.getLayoutY()-1;
        clicked.setLayoutY(yPositionMinus);
        updateTextPosition();
    }


    public void Text(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ENTER){
            //System.out.println(x_value.getText());
            clicked.setLayoutX(Double.parseDouble(xValue.getText()));
            clicked.setLayoutY(Double.parseDouble(yValue.getText()));
            updateTextPosition();
        }
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        saveButton.setBlendMode(BlendMode.HARD_LIGHT);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> saveButton.setBlendMode(BlendMode.SRC_OVER));
        pause.play();
        save_data();
    }



    public void onSwitchButtonClick(ActionEvent actionEvent) {
        switchButton1.setBlendMode(BlendMode.HARD_LIGHT);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> switchButton1.setBlendMode(BlendMode.SRC_OVER));
        pause.play();
        if(!plp){
            contentPane.setDisable(true);
            contentPane.setOpacity(0.0);
            plpPane.setOpacity(1.0);
            plpPane.setDisable(false);
            plp = true;
        }

        else if(plp){
            contentPane.setDisable(false);
            contentPane.setOpacity(1.0);
            plpPane.setOpacity(0.0);
            plpPane.setDisable(true);
            plp = false;
        }

    }
}