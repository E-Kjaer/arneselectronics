package PIM.Presentation;

import PIM.Domain.Brand;
import PIM.Domain.Category;
import PIM.Data.Crud.Retrieve;
import PIM.Data.Crud.Update;
import PIM.Domain.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import static PIM.Presentation.Message.showWarning;

public class UpdateProduct {
    @FXML
    private TextField updateDescriptionTextField;
    @FXML
    private TextField updateEANTextField;
    @FXML
    private TextField updateNameTextField;
    @FXML
    private TextField updatePriceTextField;
    @FXML
    private CheckBox updateVisibilityCheckBox;
    @FXML
    private ChoiceBox<Category> updateCategoryChoiceBox;
    @FXML
    private ChoiceBox<Brand> updateBrandChoiceBox;

    private int productID;
    private Retrieve retrieve = new Retrieve();

    public void initialize() {
        ObservableList<Category> categoryNames = FXCollections.observableArrayList(retrieve.getCategories());
        ObservableList<Brand> brandNames = FXCollections.observableArrayList(retrieve.getBrands());

        updateCategoryChoiceBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category.getName();
            }

            @Override
            public Category fromString(String s) {
                return null;
            }
        });

        updateBrandChoiceBox.setConverter(new StringConverter<Brand>() {
            @Override
            public String toString(Brand brand) {
                return brand.getName();
            }

            @Override
            public Brand fromString(String s) {
                return null;
            }
        });

        updateCategoryChoiceBox.setItems(categoryNames);
        updateBrandChoiceBox.setItems(brandNames);
    }

    public void loadProductData(int productID) {
        this.productID = productID;
        Product product = retrieve.getProductByID(productID);

        updateNameTextField.setText(product.getName());
        updateEANTextField.setText(product.getEan());
        updateDescriptionTextField.setText(product.getDescription());
        updatePriceTextField.setText(String.valueOf(product.getPrice()));
        updateVisibilityCheckBox.setSelected(product.isHidden_status());
        updateCategoryChoiceBox.setValue(product.getCategory());
        updateBrandChoiceBox.setValue(product.getBrand());
    }

    @FXML
    private void updateProductAction() {
        Update update = new Update();

        update.updateProduct(productID,
                updateNameTextField.getText(),
                updateEANTextField.getText(),
                updateDescriptionTextField.getText(),
                updatePriceTextField.getText(),
                updateVisibilityCheckBox.isSelected(),
                updateCategoryChoiceBox.getValue(),
                updateBrandChoiceBox.getValue());

        showWarning("Product updated", "The product " + updateNameTextField.getText() + " was updated successfully!");
    }

    @FXML
    public void sceneToMenu(ActionEvent event) {
        App.changeSceneToMenu(event);
    }
}
