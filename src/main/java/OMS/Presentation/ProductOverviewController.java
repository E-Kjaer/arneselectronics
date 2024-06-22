package OMS.Presentation;

import OMS.Database.OrderDBManager;
import OMS.Domain.Order;
import OMS.Domain.Product;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ProductOverviewController implements Initializable {

    @FXML
    private DatePicker start_date, end_date;

    @FXML
    private Label mostSold;

    @FXML
    private TableView<Product> overview;
    @FXML
    private TableColumn<Product, String> name_col;
    @FXML
    private TableColumn<Product, Long> id_col;
    @FXML
    private TableColumn<Product, Double> price_col;
    @FXML
    private TableColumn<Product, Date> date_col;
    @FXML
    private TableColumn<Product, Integer> amount_col;

    private Stage stage;
    private String currentView;
    private FilteredList<Product> filterView;
    private HashMap<Product, Date> orderDates;
    private ArrayList<Order> orders;
    private ArrayList<Product> products;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orders = OrderDBManager.getOrders();
        products = OrderDBManager.getProducts();

        ObservableList<Product> obsProducts = FXCollections.observableArrayList();

        //populating the observable list
        obsProducts.addAll(products);
        filterView = new FilteredList<>(obsProducts);
        orderDates = new HashMap<>();

        for(Order o : orders){
            for(Product p : products){
                orderDates.put(p, o.getOrderDate());
            }
        }

        name_col.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getName()));
        id_col.setCellValueFactory(product -> new SimpleLongProperty(product.getValue().getId()).asObject());
        price_col.setCellValueFactory(product -> new SimpleDoubleProperty(product.getValue().getPrice()).asObject());
        amount_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject());
        date_col.setCellValueFactory(product -> new SimpleObjectProperty<>(orderDates.get(product.getValue())));


        overview.setItems(filterView);
        initDatePickers();

        mostSold.setText(mostSold().toString());
    }


    private void initDatePickers(){
        start_date.setOnAction(actionEvent ->
            filterView.setPredicate(product -> {

                LocalDate orderLocalDate = orderDates.get(product).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate startDate = start_date.getValue().atStartOfDay().toLocalDate();

                if(end_date.getValue() == null && orderLocalDate.compareTo(startDate) >= 0){
                    return true;
                }else if(end_date.getValue() != null){
                    LocalDate endDate = end_date.getValue().atStartOfDay().toLocalDate().plusDays(1);
                    return orderLocalDate.compareTo(startDate) >= 0 && orderLocalDate.compareTo(endDate) < 0;
                }else if(startDate == null && end_date == null){
                    return true;
                }
                return false;
            })
        );

        end_date.setOnAction(actionEvent ->
            filterView.setPredicate(product -> {
                LocalDate endDate = end_date.getValue();
                LocalDate startDate = start_date.getValue();
                LocalDate orderLocalDate = orderDates.get(product).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if(startDate != null && endDate != null) {
                    startDate = startDate.atStartOfDay().toLocalDate();
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
    private void backBtnPressed(){
        Scene scene = overview.getScene();
        Window window = scene.getWindow();
        stage = (Stage) window;
        currentView = "overview";

        try {
            scene = new Scene(loadFXML(currentView));
            stage.setTitle("Order Overview");
            stage.setScene(scene);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void resetBtn(){
        start_date.getEditor().clear();
        end_date.getEditor().clear();
        filterView.setPredicate(order -> true);
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private HashMap<String, Integer> mostSold(){
        int amountSold = 0;
        Product soldProduct = null;

        HashMap<String, Integer> map = new HashMap<>();

        if(orders.isEmpty()){
            map.put("No products", 0);
            return map;
        }

        for(int j = 0; j < orders.size(); j++) {
            LocalDate startDate = start_date.getValue();
            LocalDate endDate = end_date.getValue();
            for (Product p : products) {
                int foundAmount = Collections.frequency(products, p);
                boolean dateCheck;

                if (startDate != null && endDate != null) {
                    startDate = startDate.atStartOfDay().toLocalDate();
                    endDate = endDate.atStartOfDay().toLocalDate().plusDays(1);
                }
                dateCheck = checkDate(orderDates.get(p), startDate, endDate);

                if ((foundAmount > amountSold) && dateCheck) {
                    amountSold = foundAmount;
                    soldProduct = p;
                }else if(foundAmount == amountSold && dateCheck){
                    map.put("Equal amounts", amountSold);
                    return map;
                }
            }
        }
        map.put(soldProduct.getName(), amountSold);
        return map;
    }

    private boolean checkDate(Date orderDate, LocalDate start, LocalDate end){
        if(start == null || end == null){
            return true;
        }
        if(orderDate != null) {
            LocalDate orderLocalDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return orderLocalDate.compareTo(start) >= 0 && orderLocalDate.compareTo(end) < 0;
        }
        return false;
    }
}

