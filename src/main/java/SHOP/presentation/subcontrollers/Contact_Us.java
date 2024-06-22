package SHOP.presentation.subcontrollers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Contact_Us extends VBox {

    @FXML
    private TextField Email;
    @FXML
    private TextArea HelpText;
    @FXML
    private TextField Name;
    @FXML
    private Button submitButton;

    public Contact_Us() {
        System.out.println("Contact_Us Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/contact_us.fxml"));
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
        submitButton.setOnAction(submitHandler);
    }

    private EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println("Submit Button clicked");

        }
    };

}
