package OMS.Presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        DBManager.updateStatus();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("order.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}