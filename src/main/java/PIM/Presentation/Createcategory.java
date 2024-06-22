package PIM.Presentation;

import PIM.Domain.Category;
import PIM.Data.Crud.Create;
import PIM.Data.Crud.Retrieve;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static PIM.Presentation.Message.showInformation;

public class Createcategory implements Initializable {

    @FXML
    private TextField category_name;
    @FXML
    private TextField category_description;
    @FXML
    private ComboBox<String> category_subcategory;
    @FXML
    private CheckBox category_checkbox;
    @FXML
    public Text category_text;

    Retrieve retrieve = new Retrieve();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCombobox();
    }

    public void loadCombobox() {

        for (Category category : retrieve.getCategories()) {
            category_subcategory.getItems().add(category.getName());
        }
        category_subcategory.getSelectionModel().select("Undefined");
        category_subcategory.setVisible(false);
        category_text.setVisible(false);
    }

    public void createCategoryButtonClicked() throws SQLException {
        loadCombobox();
        //Opret Variabler
        String name = category_name.getText();
        String description = category_description.getText();
        Category subCategory = null;

        if (category_subcategory.getValue() != null) {
            int temp_categoryID = 0;
            String categoryvalue = category_subcategory.getValue().toString();
            for (Category category : retrieve.getCategories()) {
                if (categoryvalue.equals(category.getName())) {
                    System.out.println("got " + category.getName());
                    temp_categoryID = category.getId();

                }
            }
            subCategory = retrieve.getCategoryByID(temp_categoryID);

        }
        Create create = new Create();

        Category category = new Category(
                0,
                name,
                description,
                subCategory);
        create.addCategoryToDatabase(category);
        showInformation("The category was added", "The category '"+category.getName()+"' was added succesfully");
    }


    public void sceneToMenu(ActionEvent event) {
        App.changeSceneToMenu(event);
    }

    public void addsubcategorymenu(ActionEvent event) {
        if (category_checkbox.isSelected()) {
            category_subcategory.setVisible(true);
            category_text.setVisible(true);
        } else {
            category_subcategory.setVisible(false);
            category_text.setVisible(false);
            category_subcategory.getSelectionModel().select("Undefined");
        }
    }
}
