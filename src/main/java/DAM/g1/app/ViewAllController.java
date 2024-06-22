package DAM.g1.app;

import java.net.URL;
import java.util.ResourceBundle;

import DAM.g1.domain.interfacemanagement.AdminController.AdminController;
import DAM.g1.domain.interfacemanagement.AdminController.SingletonAdminController;
import DAM.g1.domain.usermanagement.Authenticator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ViewAllController implements Initializable{
    
    public AdminController adminController = SingletonAdminController.getInstance().getAdminController();

    private Authenticator auth = adminController.getAuthenticator();

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }
    @FXML
    Button backButton;

    @FXML
    public void testFunc(ActionEvent event){
        if(auth.hasAccess()){
            System.out.println("access");
        }else{
            System.out.print("no access");
        }
    }


    @FXML
    public void switchTo() {
        SceneSwitcher.switchTo(View.MAIN);
    }




}
