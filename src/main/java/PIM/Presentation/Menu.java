package PIM.Presentation;

import PIM.Data.Crud.Delete;
import PIM.Data.Crud.Retrieve;
import PIM.Data.Crud.RetrieveJSON;
import PIM.Domain.ExportImport.Export;
import PIM.Domain.ExportImport.Import;
import PIM.Domain.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import static PIM.Data.Communicator.connect;
import static PIM.Presentation.Message.*;

public class Menu implements Initializable {
    @FXML
    private TableColumn<Product, String> col_brand;
    @FXML
    private TableColumn<Product, String> col_cat;
    @FXML
    private TableColumn<Product, String> col_des;
    @FXML
    private TableColumn<Product, String> col_ean;
    @FXML
    private TableColumn<Product, String> col_name;
    @FXML
    private TableColumn<Product, Double> col_price;
    @FXML
    private TableColumn<Product, Boolean> col_status;
    @FXML
    private TableView<Product> producttable;
    @FXML
    private TextField search_field;

    public static int selectedProductID;

    private FilteredList<Product> filteringProducts;

    public ObservableList<Product> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
        updateFilteredList();
        resetMenuState();
    }

    @FXML
    public void getSelected(MouseEvent event) {
        Product selectedProduct = producttable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            selectedProductID = 0;
        } else {
            selectedProductID = selectedProduct.getId();
        }
    }

    @FXML
    public void sceneToUpdateProduct(ActionEvent event) {
        if (producttable.getSelectionModel().getSelectedItem() == null) {
            showWarning("No Product Selected", "Please select a product to update.");
        } else {
            App.changeSceneToUpdateProduct(event);
        }
    }

    public void resetMenuState() {
        producttable.getSelectionModel().clearSelection(); // Clear selection in the table
        selectedProductID = 0; // Reset selected product ID
    }

    public ObservableList<Product> getObservableProducts() {
        Retrieve retrieve = new Retrieve();
        List<Product> products = retrieve.getProducts();
        return FXCollections.observableArrayList(products);
    }


    public SortedList<Product> updateFilteredList() {
        filteringProducts = new FilteredList<>(list);
        search_field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteringProducts.setPredicate(Product -> {
                if (newValue.isEmpty()) {
                    return true;
                }

                String searchKeyWord = newValue.toLowerCase();
                if (Product.getName().toLowerCase().contains(searchKeyWord)) {
                    return true;
                } else if (Product.getEan().toLowerCase().contains(searchKeyWord)) {
                    return true;
                } else if (Product.getDescription().toLowerCase().contains(searchKeyWord)) {
                    return true;
                } else if (Double.toString(Product.getPrice()).contains(searchKeyWord)) {
                    return true;
                } else if (Product.getCategory().getName().contains(searchKeyWord)) {
                    return true;
                } else if (Product.getBrand().getName().contains(searchKeyWord)) {
                    return true;
                } else if (Boolean.toString(Product.isHidden_status()).contains(searchKeyWord)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Product> sortedProducts = new SortedList<>(filteringProducts);
        sortedProducts.comparatorProperty().bind(producttable.comparatorProperty());
        producttable.setItems(sortedProducts);
        return sortedProducts;
    }

    public void updateTable() {
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_ean.setCellValueFactory(new PropertyValueFactory<>("ean"));
        col_des.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_cat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));
        col_brand.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand().getName()));
        col_status.setCellValueFactory(new PropertyValueFactory<>("hidden_status"));

        connect();
        list = getObservableProducts();
        producttable.setItems(list);
    }

    @FXML
    private void deleteProduct(ActionEvent event) {
        Product product = producttable.getSelectionModel().getSelectedItem();
        if (product != null) {
            boolean confirmed = showConfirmation("Confirm to delete", "Do you wish to delete the product '" + product.getName() + "' from the system?");
            if (confirmed) {
                try {
                    Delete deleter = new Delete();
                    deleter.removeProductID(product.getId());
                    showInformation("Product deleted succesfully", "The product '" + product.getName() + "' was deleted successfully from the system");
                    list.remove(product);
                    updateTable();
                    updateFilteredList();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showWarning("No product selected", "Please select the product you wish to delete");
        }
    }

    public void saveFilteredProductsToFile() {
        SortedList<Product> sortedProducts = null;
        updateFilteredList();
        String filteredProductsJson = new RetrieveJSON().convertToJson(sortedProducts);
        Export export = new Export();
        export.saveProductList(filteredProductsJson);
        showInformation("Saved as file", "File was saved with the products as JSON objects");
    }

    @FXML
    private void openFile(ActionEvent event) {
        Import importer = new Import();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JSON File");

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String json = importer.importJSONfile(selectedFile.getPath());
                importer.importProducts(json);
                updateTable();
                updateFilteredList();
                showInformation("File was imported", "The products was successfully imported from the file");
            } catch (IOException e) {
                e.printStackTrace();
                showWarning("Couldn't import from file", "The products couldn't be imported. Try picking a JSON file or check the products in the file");
            }
        }
    }


    public void sceneToProduct(ActionEvent event) {
        App.changeSceneToCreateProduct(event);
    }

    public void sceneToBrand(ActionEvent event) {
        App.changeSceneToAddBrand(event);
    }

    public void sceneToCategory(ActionEvent event) {
        App.changeSceneToCreateCategory(event);
    }
}