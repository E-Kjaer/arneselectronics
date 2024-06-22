package OMS.Presentation;

import OMS.Database.DBSalereportData;
import OMS.Domain.PDF.PDFcreate;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SalereportController {

    @FXML
    private ComboBox<Month> monthPicker;

    @FXML
    private ComboBox<Integer> yearPicker;
    @FXML
    private Label salereport_loading;


    @FXML
    public void initialize() {
        // Initialize month picker with all months
        monthPicker.setItems(FXCollections.observableArrayList(Month.values()));
        monthPicker.setConverter(new StringConverter<Month>() {
            @Override
            public String toString(Month month) {
                return month != null ? month.name() : "";
            }

            @Override
            public Month fromString(String string) {
                return Month.valueOf(string);
            }
        });

        // Initialize year picker with a range of years
        int currentYear = Year.now().getValue();
        yearPicker.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(currentYear - 24, currentYear)
                        .boxed()
                        .collect(Collectors.toList())
        ));

        // Set default values to current month and year
        monthPicker.setValue(Month.of(LocalDateTime.now().getMonthValue()));
        yearPicker.setValue(currentYear);
    }

    @FXML
    public void generatePdf() {
        salereport_loading.setVisible(true);
        salereport_loading.setText("Downloading...");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            Month selectedMonth = getSelectedMonth();
            Integer selectedYear = getSelectedYear();

            if (selectedMonth != null && selectedYear != null) {
                Random random = new Random();
                int randomNumber = random.nextInt(61);
                LocalDateTime dateTime = LocalDateTime.of(selectedYear, selectedMonth, 1, 1, 0, randomNumber);
                Timestamp timestamp = Timestamp.valueOf(dateTime);

                PDFcreate.createSaleReport(timestamp);
                salereport_loading.setText("Done");
            } else {
                System.out.println("Please select both a month and a year.");
            }
        });
        pause.play();
    }

    public Month getSelectedMonth() {
        return monthPicker.getValue();
    }

    public Integer getSelectedYear() {
        return yearPicker.getValue();
    }


}