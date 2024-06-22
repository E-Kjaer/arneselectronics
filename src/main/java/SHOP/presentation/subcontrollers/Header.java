package SHOP.presentation.subcontrollers;

import SHOP.presentation.ViewController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class Header extends BorderPane {
    @FXML
    private ImageView basketIcon;

    @FXML
    private Button cat1;

    @FXML
    private Button cat2;

    @FXML
    private Button cat3;

    @FXML
    private Button cat4;

    @FXML
    private Button guide;

    @FXML
    private Label contactUsText;

    @FXML
    private Label headerLabel;

    @FXML
    private ImageView helpLogo;

    @FXML
    private ImageView logoImg;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private Label amountLabel;

    public Header() {
        System.out.println("Start Header constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/header.fxml"));
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
        //Set handlers
        cat1.setOnAction(categoryHandler);
        cat2.setOnAction(categoryHandler);
        cat3.setOnAction(categoryHandler);
        cat4.setOnAction(categoryHandler);

        guide.setOnAction(guideHandler);

        searchButton.setOnAction(searchButtonHandler);
        searchField.setOnKeyPressed(searchEnterHandler);

        contactUsText.setOnMouseClicked(contactHandler);
        basketIcon.setOnMouseClicked(basketHandler);

        logoImg.setOnMouseClicked(homepageHandler);
        headerLabel.setOnMouseClicked(homepageHandler);

        System.out.println("End Header constructor");
    }

    public void setLogo(String image) {
        //System.out.println(image);
        logoImg.setImage(new Image("file:" + image));
    }

    //Handler for search button
    private EventHandler<ActionEvent> searchButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (!searchField.getText().isEmpty()){
                updatePLPFromSearch(searchField.getText());
            }
        }
    };

    //Handler for when enter is pressed while in search bar
    private EventHandler<KeyEvent> searchEnterHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!searchField.getText().isEmpty()) {
                    updatePLPFromSearch(searchField.getText());
                }
            }
        }
    };

    //Updates the PLP view based on the search input
    private void updatePLPFromSearch(String searchText){
        System.out.println("Update PLP from search");
        ViewController.getInstance().showPLPSearch(searchText);
    }

    //Returns to homepage when logo is pressed
    private EventHandler<MouseEvent> homepageHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            System.out.println("Back on the homepage");
            ViewController.getInstance().showHomePage();
        }
    };

    //Checks which category button is clicked and displays the corresponding PLP
    private EventHandler<ActionEvent> categoryHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Button buttonClicked = (Button) actionEvent.getSource();
            System.out.println(buttonClicked.getText() + " Clicked");
            ViewController.getInstance().showPLPbyCategory(buttonClicked.getText());
        }
    };

    private EventHandler<MouseEvent> contactHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("We are going to contact you! :o)");
            ViewController.getInstance().showContactUs();
        }
    };

    private EventHandler<MouseEvent> basketHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            ViewController.getInstance().showCheckout();
            System.out.println("Gone to checkout");
        }
    };
    private EventHandler<ActionEvent> guideHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println("Clicked guide");
            ViewController.getInstance().showContentPageList();
        }
    };
    //Label for quantity of products in basket
    public void setAmount(int amount) {
        amountLabel.setText(String.valueOf(amount));
    }
}


