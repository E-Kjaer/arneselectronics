package PIM.Presentation;

import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Data.Crud.Create;
import PIM.Data.Crud.Retrieve;
import PIM.Domain.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static PIM.Presentation.Message.showInformation;
import static PIM.Presentation.Message.showWarning;

public class Createproduct implements Initializable {


    @FXML
    private TextField product_name;
    @FXML
    private TextField product_description;
    @FXML
    private TextField product_EAN;
    @FXML
    private TextField product_price;
    @FXML
    private CheckBox product_visibillity;

    @FXML
    private ComboBox prod_catid;
    @FXML
    private ComboBox prod_brandid;

    private Retrieve retrieve = new Retrieve();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCombobox();
    }

    public void loadCombobox() {
        for (Category category : retrieve.getCategories())
            prod_catid.getItems().add(category.getName());

        for (Brand brand : retrieve.getBrands())
            prod_brandid.getItems().add(brand.getName());
    }


    public void createProductButtonClicked() throws SQLException {
        try {
            //Opret Variabler
            String name = product_name.getText();
            String description = product_description.getText();
            String EAN = product_EAN.getText();
            Double price = Double.valueOf(product_price.getText());
            Boolean hidden_status = product_visibillity.isSelected();

            int temp_catID = 0;
            String catvalue = prod_catid.getValue().toString();
            for (Category category : retrieve.getCategories()) {
                if (catvalue.equals(category.getName())) {
                    System.out.println("got " + category.getName());
                    temp_catID = category.getId();

                }
            }

            int temp_brandID = 0;
            String brandvalue = prod_brandid.getValue().toString();
            for (Brand brand : retrieve.getBrands()) {
                if (brandvalue.equals(brand.getName())) {
                    System.out.println("got " + brand.getName());
                    temp_brandID = brand.getId();

                }
            }
            Category category_id = retrieve.getCategoryByID(temp_catID);


            Brand brand_id = retrieve.getBrandByID(temp_brandID);


            Create create = new Create();
            Product product = new Product(
                    0,
                    name,
                    description,
                    EAN,
                    price,
                    hidden_status,
                    category_id,
                    brand_id);

            create.addProductToDatabase(product);
            showInformation("Product created succesfully!", "The product '" + name + "' was created succesfully");
        } catch (NumberFormatException e) {
            showWarning("Product wasn't created", "The product couldn't be created, please provide the correct information");
        } catch (SQLException e) {
            showWarning("Product wasn't created", "The product couldn't be created, please provide the correct information");
        }
    }

    public void sceneToMenu(ActionEvent event) {
        App.changeSceneToMenu(event);
    }
}
