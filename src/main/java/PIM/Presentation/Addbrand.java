package PIM.Presentation;

import PIM.Data.Crud.Delete;
import PIM.Domain.Brand;
import PIM.Data.Crud.Create;
import PIM.Data.Crud.Retrieve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static PIM.Data.Communicator.connect;
import static PIM.Presentation.Message.*;

public class Addbrand implements Initializable {

    @FXML
    private TextField brand_name;

    @FXML
    private TableView<Brand> tableview_brands;

    @FXML
    private TableColumn<Brand, String> tb_names;

    public ObservableList<Brand> list;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }

    public void createBrandButtonClicked() {
        String name = brand_name.getText();

        Create create = new Create();
        Brand brand = new Brand(
                0,
                name);

        try {
            create.addBrandToDatabase(brand);
            updateTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sceneToMenu(ActionEvent event) {
        App.changeSceneToMenu(event);
    }


    public ObservableList<Brand> getObservableBrand() {
        Retrieve retrieve = new Retrieve();
        List<Brand> brand = retrieve.getBrands();
        return FXCollections.observableArrayList(brand);
    }


    public void updateTable() {
        tb_names.setCellValueFactory(new PropertyValueFactory<>("name"));
        connect(); //connecting to database
        list = getObservableBrand(); //Retrieve products from the database
        tableview_brands.setItems(list);
    }


    @FXML
    private void deleteBrand(ActionEvent event) {
        Brand brand = tableview_brands.getSelectionModel().getSelectedItem();
        if (brand != null) {
            boolean confirmed = showConfirmation("Confirm to delete", "Do you wish to delete the brand '" + brand.getName() + "' from the system? All products under this brand will be deleted too from the system.");
            if (confirmed) {
                try {
                    Delete deleter = new Delete();
                    deleter.removeBrand(brand.getId());
                    showInformation("Brand deleted successfully", "The Brand '" + brand.getName() + "' and associated products were deleted successfully from the system.");
                    list.remove(brand);  // Assuming 'list' is the ObservableList backing your TableView
                    updateTable();  // Refresh the TableView to reflect the deletion
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    showWarning("Deletion failed", "An error occurred while trying to delete the brand.");
                }
            } else {
                showWarning("No brand selected", "Please select the brand you wish to delete.");
            }
        }
    }
}
