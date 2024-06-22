package DAM.g1.app;

import DAM.g1.AppUser;
import DAM.g1.domain.interfacemanagement.AdminController.AdminController;
import DAM.g1.domain.interfacemanagement.AdminController.SingletonAdminController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private AdminController adminController = SingletonAdminController.getInstance().getAdminController();

    @FXML
    private Button submitButton;

    @FXML
    private Button viewAllButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorDisplay;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    

    @FXML
    public void submit(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if username or password is empty
        if (username.isEmpty() || password.isEmpty()) {
            errorDisplay.setText("Please enter username and password.");
            errorDisplay.setVisible(true);
            return;  // Exit the method
        }

        // Attempt login
        if (adminController.login(new AppUser(username, password))) {
            switchTo(View.MAIN);
        } else {
            // Show error message
            errorDisplay.setText("Invalid username or password.");
            errorDisplay.setVisible(true);
        }
    }

    @FXML
public void switchTo(View view) {
    SceneSwitcher.switchTo(view);
}

}
