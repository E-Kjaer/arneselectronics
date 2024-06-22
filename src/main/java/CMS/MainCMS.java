package CMS;

import SHOP.data.interfaces.CMSInterface;
import com.mongodb.client.model.Filters;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class MainCMS extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainCMS.class.getResource("adminpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 960);
        stage.setTitle("CMS");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }
}
