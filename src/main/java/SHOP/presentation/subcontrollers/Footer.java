package SHOP.presentation.subcontrollers;

import SHOP.presentation.ViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Footer extends BorderPane {

    @FXML
    private Label contactUsText;

    @FXML
    private Label guide;


    public Footer() {
        System.out.println("Footer Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/footer.fxml"));
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
       contactUsText.setOnMouseClicked(contactHandler);
       guide.setOnMouseClicked(guideHandler);
    }


    private EventHandler<MouseEvent> contactHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("We are going to contact you! :o)");
            ViewController.getInstance().showContactUs();
        }
    };


    private EventHandler<MouseEvent> guideHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            System.out.println("Clicked guide");
            ViewController.getInstance().showContentPageList();
        }
    };
}
