package OMS.Presentation;

import OMS.Database.OrderDBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SideMenuController {

    @FXML
    private Label person_logo, person;

    private String name = "John petersen doe";


    public static String getInitials(String fullName) {
        int idxLastWhitespace = fullName.lastIndexOf(" ");
        String initials = ""+fullName.charAt(0) + fullName.charAt(idxLastWhitespace + 1);
        return initials.toUpperCase();
    }
    public void initialize(){
        person.setText(name);
        person_logo.setText(getInitials(name));
    }

    public void changeScene(ActionEvent event) {
        Button button = (Button) event.getSource();
        String newScene = button.getId()+".fxml";
        System.out.println(newScene);

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(newScene)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSts(){
        OrderDBManager.updateStatus();
    }


}
