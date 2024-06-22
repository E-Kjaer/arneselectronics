package PIM.Presentation;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/PIM/Presentation/menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void changeSceneToCreateProduct(ActionEvent event) {
        try {
            // Load the FXML file
            Stage stage;
            FXMLLoader loader = new FXMLLoader(Menu.class.getResource("/PIM/Presentation/createproduct.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the scene to the stage and show the stage
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            // Handle loading error
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeSceneToAddBrand(ActionEvent event) {
        try {
            // Load the FXML file
            Stage stage;
            FXMLLoader loader = new FXMLLoader(Menu.class.getResource("/PIM/Presentation/addbrand.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the scene to the stage and show the stage
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            // Handle loading error
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeSceneToUpdateProduct(ActionEvent event) {
        try {
            // Load the FXML file
            Stage stage;
            FXMLLoader loader = new FXMLLoader(Menu.class.getResource("/PIM/Presentation/updateProduct.fxml"));
            Parent root = loader.load();

            UpdateProduct controller = loader.getController();
            controller.loadProductData(Menu.selectedProductID);

            Scene scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeSceneToCreateCategory(ActionEvent event) {
        try {
            // Load the FXML file
            Stage stage;
            FXMLLoader loader = new FXMLLoader(Menu.class.getResource("/PIM/Presentation/createcategory.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the scene to the stage and show the stage
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            // Handle loading error
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeSceneToMenu(ActionEvent event) {
        try {
            // Load the FXML file
            Stage stage;
            FXMLLoader loader = new FXMLLoader(Menu.class.getResource("/PIM/Presentation/menu.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the scene to the stage and show the stage
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            // Handle loading error
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
