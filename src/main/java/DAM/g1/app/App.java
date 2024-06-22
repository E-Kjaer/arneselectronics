package DAM.g1.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {


    private static App app;
    
    private Stage stage;

    public static App getApp(){
        if(app==null){
            throw new IllegalStateException("App instance not created yet!");
        }
        return app;
    }

    public Stage getStage(){
        return stage;
    }

    @Override
    public void start(Stage stage) throws IOException{
        app = this;
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/DAM/fxml/" + View.LOGIN.getFileName()));
    
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/DAM/css/login.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void launchApp(){
        launch();
    }
}
