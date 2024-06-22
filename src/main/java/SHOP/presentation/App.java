package SHOP.presentation;

import SHOP.Flags;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Flag for setting development mode
        // if true, the system uses dummy data
        // if false, the system integrates with the other components
        Flags.setDevelopment(false);
        FXMLLoader fxmlLoader = ViewController.getInstance().getInitialGUILoader();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        Scene scene = new Scene(fxmlLoader.load(), width, height);

        stage.setTitle("🌐The Internet");
        scene.getRoot().setStyle("-fx-font-size: 13px;");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
