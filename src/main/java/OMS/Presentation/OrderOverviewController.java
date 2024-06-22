package OMS.Presentation;

import OMS.Database.OrderDBManager;
import OMS.Domain.Order;
import OMS.Domain.Status;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class OrderOverviewController implements Initializable {

    @FXML
    private RadioButton pendingR, processedR, shippedR, deselectR;

    @FXML
    private DatePicker start_date, end_date;

    @FXML
    private TextField search_bar;

    @FXML
    private TableView<Order> overview;
    @FXML
    private TableColumn<Order, Long> orderNo_col;
    @FXML
    private TableColumn<Order, Double> price_col;
    @FXML
    private TableColumn<Order, Date> date_col;
    @FXML
    private TableColumn<Order, Status> status_col;

    private FilteredList<Order> filterView;
    private ObservableList<Order> orderList;

    private ToggleGroup tg;

    private Stage stage;
    private String currentView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //filtered list needs an obsList
        orderList = FXCollections.observableArrayList();
        //populate the obsList
        orderList.addAll(OrderDBManager.getOrders());

        filterView = new FilteredList<>(orderList);

        //define how the columns in the tableview should extract data from the obsList
        orderNo_col.setCellValueFactory(order -> new SimpleLongProperty(order.getValue().getOrderIdLong()).asObject());
        price_col.setCellValueFactory(order -> new SimpleDoubleProperty(order.getValue().getTotalPrice()).asObject());
        date_col.setCellValueFactory(order -> new SimpleObjectProperty(order.getValue().getOrderDate()));
        status_col.setCellValueFactory(order -> new SimpleObjectProperty<>(order.getValue().getStatus()));

        //setting the content of the tableview with the FilteredList
        overview.setItems(filterView);
        initStatus();
        initSearchBar();
        initDatePickers();
    }

    private void initSearchBar() {
        search_bar.textProperty().addListener((observable, oldValue, newValue) ->
                filterView.setPredicate(order -> {
                    if (newValue == null || newValue.isBlank()) return true;

                    String lowerCaseNewValue = newValue.toLowerCase();
                    String orderIdString = String.valueOf(order.getOrderIdLong());
                    String priceString = String.valueOf(order.getTotalPrice());
                    String statusString = order.getStatus() != null ? order.getStatus().toString().toLowerCase() : "";
                    String orderDateString = order.getOrderDate() != null ? order.getOrderDate().toString().toLowerCase() : "";

                    return !orderIdString.equals("0") &&
                            (orderIdString.contains(lowerCaseNewValue) ||
                                    priceString.contains(lowerCaseNewValue) ||
                                    statusString.contains(lowerCaseNewValue)) ||
                                    order.getOrderDate().toString().contains(lowerCaseNewValue);
                })
        );
    }

    private void initStatus(){

        //setting toggle group for RadioButtons
        tg = new ToggleGroup();
        pendingR.setToggleGroup(tg);
        processedR.setToggleGroup(tg);
        shippedR.setToggleGroup(tg);
        deselectR.setToggleGroup(tg);

        //making the FilteredList reactive to which RadioButton is toggled, so we can filter
        //using buttons
        tg.selectedToggleProperty().addListener((observableValue, oldValue, newValue) ->
                filterView.setPredicate(order -> {
                    if (tg.getSelectedToggle() == null) {
                        return true;
                    } else if (newValue.equals(pendingR) && order.getStatus().equals(Status.PENDING)) {
                        return true;
                    } else if (newValue.equals(processedR) && order.getStatus().equals(Status.PROCESSED)) {
                        return true;
                    } else if (newValue.equals(shippedR) && order.getStatus().equals(Status.SHIPPED)) {
                        return true;
                    } else if (newValue.equals(deselectR)) {
                        return true;
                    } else {
                        return false;
                    }
                })
        );
    }

    private void initDatePickers(){
        start_date.setOnAction(actionEvent ->
            filterView.setPredicate(order -> {

                LocalDate orderLocalDate;
                LocalDate startDate = start_date.getValue().atStartOfDay().toLocalDate();

                if(order.getOrderDate() == null){
                    System.out.println("date is null");
                }
                orderLocalDate = order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if(end_date.getValue() == null && orderLocalDate.compareTo(startDate) >= 0){
                    return true;
                }else if(end_date.getValue() != null){
                    LocalDate endDate = end_date.getValue().atStartOfDay().toLocalDate().plusDays(1);
                    return orderLocalDate.compareTo(startDate) >= 0 && orderLocalDate.compareTo(endDate) < 0;
                }
                return false;
            })
        );

        end_date.setOnAction(actionEvent ->
            filterView.setPredicate(order -> {
                LocalDate endDate = end_date.getValue();
                LocalDate startDate = start_date.getValue();
                LocalDate orderLocalDate;

                orderLocalDate = order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if(startDate != null && endDate != null) {
                    // Convert orderDate to LocalDate to compare dates without timestamp
                    startDate = startDate.atStartOfDay().toLocalDate();
                    // Add one day to make the end date inclusive
                    endDate = endDate.atStartOfDay().toLocalDate().plusDays(1);
                    return (orderLocalDate.compareTo(startDate) >= 0 && orderLocalDate.compareTo(endDate) < 0);
                }else if(startDate == null && orderLocalDate.compareTo(endDate) < 0){
                    return true;
                }
                return false;
            })
        );
    }
    @FXML
    private void resetBtn(){
        start_date.getEditor().clear();
        end_date.getEditor().clear();
        filterView.setPredicate(order -> true);
    }

    @FXML
    private void productPressed(){
            Scene scene = overview.getScene();
            Window window = scene.getWindow();
            stage = (Stage) window;
            currentView = "product_overview";

            try {
                /*Scene*/
                scene = new Scene(loadFXML(currentView));
                stage.setTitle("Product Overview");
                stage.setScene(scene);
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @FXML
    private void updateStatus() {
        OrderDBManager.updateStatus();
    }




}

