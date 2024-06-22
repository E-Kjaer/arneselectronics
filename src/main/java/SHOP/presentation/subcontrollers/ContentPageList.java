package SHOP.presentation.subcontrollers;

import SHOP.data.models.Guide;
import SHOP.presentation.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ContentPageList extends VBox {
    @FXML
    ListView<String> listInfoPages;

    ArrayList<Guide> guideList;

    public ContentPageList(ArrayList<Guide> guideList){
        this.guideList = guideList;
        System.out.println("ContentPageList Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/contentPages.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exc) {
            // handle exception
            System.out.println(exc);
        }

    }

    @FXML
    public void initialize(){
        System.out.println("ContentPageList initialize");
        System.out.println("initialize guidelist:" + this.guideList);

        ArrayList<String> titles = new ArrayList<>();
        for (Guide guide: this.guideList){
            titles.add(guide.getTitle());
        }

        // Sample data for pages/categories
        ObservableList<String> pages = FXCollections.observableArrayList(
                titles
        );
        listInfoPages.setItems(pages);
        listInfoPages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Handle selection change (e.g., navigate to selected page)
            System.out.println("Selected Page: " + newValue);
        });
        // Add event handler for when an item is clicked in the list
        listInfoPages.setOnMouseClicked(guidePageHandler);
    }
    private EventHandler<MouseEvent> guidePageHandler = new EventHandler(){
        @Override
        public void handle(Event event) {
            String selectedPage = listInfoPages.getSelectionModel().getSelectedItem();
            if (selectedPage != null) {
                System.out.println("Selected Page: " + selectedPage);
                // Call a method to handle the click event, e.g., navigate to the selected page
                Guide guide = null;
                for (Guide g : guideList) {
                    if (g.getTitle().equals(selectedPage)) {
                        guide = g;
                    }
                }
                ViewController.getInstance().showBlogPost(guide);
            }
        }
    };
}
