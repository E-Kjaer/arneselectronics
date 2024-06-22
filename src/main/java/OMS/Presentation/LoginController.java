package OMS.Presentation;

import OMS.Presentation.Operator.Operator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
//    private Scene scene;
    private Stage stage;
    private String currentView;
    private Parent root;
    private Scene scene;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrongInput;
    @FXML
    private Button login_Btn;
    @FXML
    private VBox container;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_Btn.setDefaultButton(true);
    }

    @FXML
    protected void loginClick(ActionEvent event) throws IOException {
        Operator operator = new Operator();
        String username_ = username.getText().toLowerCase();

        if (username_.equals(operator.getUsername()) && password.getText().equals(operator.getPassword())) {
            currentView = "analysis";
            root = loadFXML(currentView);
            Node n = (Node)event.getSource();
            Scene s = n.getScene();
            stage = (Stage)s.getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else if (username.getText().isBlank() || password.getText().isBlank() || username.getText().isBlank() && password.getText().isBlank()) {
            incorrectInput(false);
        } else if (!username.getText().equals(operator.getUsername()) || !password.getText().equals(operator.getPassword())) {
            incorrectInput(true);
        }
    }

    private Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
        try{
            return fxmlLoader.load();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }



    //error code false = didn't fill out username or password or both
    //error code true = username or password or both are incorrect
    public void incorrectInput(boolean errorCode){
        if (!wrongInput.isVisible()){
            wrongInput.setVisible(true);
            username.setStyle("-fx-border-radius: 3; -fx-border-color:  #a31414;");
            password.setStyle("-fx-border-radius: 3; -fx-border-color:  #a31414;");
            double current_y = login_Btn.getLayoutY();
            login_Btn.setLayoutY(current_y+10);
        }
        if (!errorCode){
            wrongInput.setText("Fill out username and password");
        }else {
            wrongInput.setText("Invalid username or password");
        }
        password.setText("");
    }
}