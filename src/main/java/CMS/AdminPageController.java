package CMS;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AdminPageController {

    @FXML
    private Button editButton;

    @FXML
    private ImageView settingsIm, returnIm;

    @FXML
    private VBox contentVbox;

    private ArrayList<String> buttonTexts = new ArrayList<>(); // Store the titles of created buttons to iterate in number by using .size()+1

    private Button selectedButton; // Store the currently selected button

    // Method for creating content page buttons takes a string gives the button that name
    private void createButton(String newButtonText) {
        buttonTexts.add(newButtonText);
        Button newButton = new Button(newButtonText);
        newButton.setOnAction(event -> onContentPageClick(newButton));
        newButton.setMinWidth(672);
        newButton.setMinHeight(80);
        newButton.setStyle("-fx-background-color: lightblue;");
        newButton.setFont(new Font("Times New Roman", 16));

        // Add the new Button to the VBox content
        contentVbox.getChildren().add(newButton);
    }

    @FXML
    public void initialize() {
        // Set opacity of unused images
        returnIm.setOpacity(0);
        settingsIm.setOpacity(0);
        System.out.println("Initializing");
        selectedButton = null; // Initially no button is selected
        for (int i = 0; i < DataMethods.getCreatedContentPages(); i++) {
            createButton(DataMethods.getCreatedContentPagesListPosition(i));
            System.out.println("Creating Button: " + DataMethods.getCreatedContentPagesListPosition(i));
        }
        // If first time setup has not been done yet do this
        if (DataMethods.isFirstTimeSetUp()) {
            // Filter documents with id starting with "Content"
            Bson filter = new Document("type", new Document("$regex", "^pd.*"));

            // Find documents that match the filter
            try (MongoCursor<Document> cursor = MongoDBMethods.getCollection().find(filter).iterator()) {
                // For every document that matches the filter create a button with a name that matches the documents id
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    String id = document.getString("_id");
                    createButton(id);
                    System.out.println("Creating Button: " + id);
                    updateContentPagesInfo(id);
                }
            }
            // Set first time setup variable to false
            DataMethods.setFirstTimeSetUp(false);
        }
        System.out.println(DataMethods.getCreatedContentPages());
    }

    // Increments the count of created content pages and adds the id the array list of created content page names
    public void updateContentPagesInfo(String id) {
        DataMethods.incrementCreatedContentPages();
        DataMethods.addCreatedContentPagesList(id);
    }

    public void removeContentPagesInfo(String id) {
        DataMethods.removeCreatedContentPagesList(id);
        DataMethods.decrementCreatedContentPages();
    }


    public void onCreateButtonClick(ActionEvent actionEvent) {
        createButton("Content Page " + (buttonTexts.size() + 1));
        updateContentPagesInfo("Content Page " + (buttonTexts.size()));
        System.out.println(DataMethods.getCreatedContentPages());
    }

    public void onContentPageClick(Button clickedButton){
        String buttonText = clickedButton.getText();
        System.out.println("Content page clicked: " + buttonText);
        // Update color of all buttons
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: lightblue;");
        }
        clickedButton.setStyle("-fx-background-color: #77c3ec;"); // Set clicked button to darkblue
        selectedButton = clickedButton; // Update selected button
        DataMethods.setClickedContentPage(buttonText);
        System.out.println(DataMethods.getClickedContentPage());

    }

    public void onReturnButtonClick(ActionEvent actionEvent) {

    }

    public void onSettingsButtonClick(ActionEvent actionEvent) {

    }

    public void onEditButtonClick(ActionEvent actionEvent) {
        if (Objects.equals(DataMethods.getClickedContentPage(), "0")) {
            System.out.println("Please Create Content Page First");
        }
        else {
            try {
                // Get a reference to the main stage
                Stage stage = (Stage) editButton.getScene().getWindow();

                // Load the FXML file for the HelloController
                FXMLLoader loader = new FXMLLoader(MainCMS.class.getResource("edit.fxml"));
                Parent root = loader.load();

                // Get the HelloController instance
                EditPageController editPageController = loader.getController();

                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);

                // Show the new scene
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDeleteButtonClick(ActionEvent actionEvent) {
        removeContentPagesInfo(selectedButton.getText());
        contentVbox.getChildren().remove(selectedButton);
        System.out.println(DataMethods.getCreatedContentPages());

        String id = DataMethods.getClickedContentPage();
        MongoDBMethods.deleteFileFromCollection(id);

    }

    public void onRenameButtonClick(MouseEvent mouseEvent) {
        if (!Objects.equals(DataMethods.getClickedContentPage(), "0")) {
            // Create the pop-up stage and scene
            Stage popupStage = new Stage();
            Label popupLabel = new Label();
            TextField textField = new TextField();
            popupLabel.setWrapText(true);
            popupLabel.setFont(new Font("Times New Roman", 14));
            popupLabel.setText("Please enter the new name for the content page");
            textField.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String userInput = textField.getText();
                    if (!userInput.isEmpty() && !DataMethods.getCreatedContentPagesList().contains(userInput)) {
                        System.out.println("You entered: " + userInput);
                        selectedButton.setText(userInput);
                        String id = DataMethods.getClickedContentPage();
                        Document existingDocument = MongoDBMethods.getCollection().find(Filters.eq("_id", id)).first();
                        if (existingDocument != null) {
                            existingDocument.replace("_id", userInput);
                            MongoDBMethods.deleteFileFromCollection(id);
                            MongoDBMethods.saveToCollectionMethod(existingDocument, userInput);
                        }
                        removeContentPagesInfo(id);
                        updateContentPagesInfo(userInput);
                        DataMethods.setClickedContentPage(userInput);
                        popupStage.close();
                    } else if (!userInput.isEmpty()) {
                        System.out.println("Please type a unique new name for the content page");
                        popupLabel.setText("Please type a unique new name for the content page");
                    }
                }
            });
            VBox popupLayout = new VBox(10);
            popupLayout.getChildren().addAll(popupLabel, textField);
            Scene popupScene = new Scene(popupLayout, 300, 100);

            // Configure the pop-up stage properties
            popupStage.initModality(Modality.APPLICATION_MODAL); // Makes the main stage unusable while popup is open
            popupStage.setTitle(" ");
            popupStage.setScene(popupScene);

            popupStage.showAndWait();
        }
    }
}