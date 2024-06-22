package SHOP.presentation.subcontrollers;

import SHOP.data.models.Guide;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class BlogPostDisplay extends VBox {

    @FXML
    private Text titleText;

    @FXML
    private ImageView blogImage;

    @FXML
    private Text authorText;

    @FXML
    private Text dateText;

    @FXML
    private Text bodyText;

    // Loading FXML for blogpost
    public BlogPostDisplay(){
        System.out.println("BlogPostDisplay constructed");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/blogPostDisplay.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exc) {
            // handle exception
            System.out.println(exc.getCause().getMessage());
        }
    }
    @FXML
    public void initialize() {

    }

    // Setting up text for guides
    public void setText(Guide guide) {
        titleText.setText(guide.getTitle());
        authorText.setText(guide.getAuthor());
        dateText.setText(guide.getDate());
        bodyText.setText(guide.getBody());
        if (!guide.getImage().isEmpty()) {
            blogImage.setImage(new Image(guide.getImage()));
        }
    }
}
