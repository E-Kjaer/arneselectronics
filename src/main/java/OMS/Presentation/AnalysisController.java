package OMS.Presentation;

import javafx.application.Platform;
import javafx.scene.control.Label;
import java.sql.SQLException;
import java.sql.Timestamp;
import OMS.Database.DBSalereportData;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AnalysisController {

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private CategoryAxis xaxis;

    @FXML
    private NumberAxis yaxis;

    @FXML
    private Label amount, customer, profit, revenue;

    @FXML
    private ToggleGroup analysis;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @FXML
    public void initialize() throws SQLException {
        // Set axis labels
        xaxis.setLabel("Days");
        yaxis.setLabel("Amount");

        // Add listener to handle changes in selected radio button
        analysis.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                String dataType = selectedRadioButton.getText();
                try {
                    loadData(dataType);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Load initial data
        loadData("Revenue");
    }

    private void loadData(String dataType) throws SQLException {
        // Clear existing data on the chart
        lineChart.getData().clear();

        // Submit tasks to load data asynchronously
        // Don't really understand the lambda function
        Future<ArrayList<Double>> avgAmountFuture = executor.submit(() -> loadAvgAmountData());
        Future<ArrayList<Double>> revenueFuture = executor.submit(() -> loadRevenueData());
        Future<ArrayList<Integer>> newUserFuture = executor.submit(() -> loadNewUserData());

        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Data Series");

            ArrayList<? extends Number> data = switch (dataType) {
                case "AvgAmount" -> avgAmountFuture.get();
                case "Revenue" -> revenueFuture.get();
                case "NewUser" -> newUserFuture.get();
                default -> throw new IllegalArgumentException("Invalid data type");
            };

            for (int i = 0; i < data.size(); i++) {
                series.getData().add(new XYChart.Data<>("Day " + i, data.get(i)));
            }

            lineChart.getData().add(series);


            long currentTimeMillis = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(currentTimeMillis);
            double profitValue = DBSalereportData.getProfit(timestamp);


            Platform.runLater(() -> {
                try {
                    double avgAmount = avgAmountFuture.get().stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    amount.setText(String.format("%.2f", avgAmount));
                    int totalCustomers = newUserFuture.get().stream().mapToInt(Integer::intValue).sum();
                    customer.setText(String.valueOf(totalCustomers));
                    String formattedProfit = String.format("%.2f", profitValue);
                    profit.setText(formattedProfit);
                    double totalRevenue = revenueFuture.get().stream().mapToDouble(Double::doubleValue).sum();
                    revenue.setText(String.format("%.2f", totalRevenue));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Double> loadAvgAmountData() throws SQLException {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        return DBSalereportData.getAvgAmountPerOrderPerDay(timestamp);
    }

    private ArrayList<Double> loadRevenueData() throws SQLException {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        return DBSalereportData.getRevenuePerDay(timestamp);
    }

    private ArrayList<Integer> loadNewUserData() throws SQLException {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        return DBSalereportData.getNewUserPerDay(timestamp);
    }

}