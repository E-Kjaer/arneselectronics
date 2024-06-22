package DAM.g1.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneSwitcher {

    public static void switchTo(View view) {
        try {
            Stage stage = App.getApp().getStage();
            Parent root = FXMLLoader.load(SceneSwitcher.class.getResource("/DAM/fxml/" + view.getFileName()));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
