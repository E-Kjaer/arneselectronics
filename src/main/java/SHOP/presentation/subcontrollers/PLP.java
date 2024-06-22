package SHOP.presentation.subcontrollers;

import SHOP.data.models.Product;
import SHOP.domain.productComparators.*;
import SHOP.presentation.ViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.RangeSlider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PLP extends HBox {
    @FXML
    private Label categoryHeaderLabel;
    @FXML
    private Label categoryTextLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button filterButton;
    @FXML
    private ChoiceBox<String> sortChoiceBox;
    @FXML
    private RangeSlider rangeSlider;
    @FXML
    private Label minPriceLabel;
    @FXML
    private Label maxPriceLabel;
    @FXML
    private CheckListView<String> brandCheckList;

    private String[] sortOptions = {"Alphabetical A-Z", "Alphabetical Z-A", "Price Ascending", "Price Descending"};

    private String category;

    private ArrayList<Product> productList;

    public PLP() {
        System.out.println("PLP Constructor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/plp.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exc) {
            // handle exception
            System.out.println(exc);
        }
    }

    public void initialize() {
        System.out.println("PLP initialize");

        // Setting up rangeslider, filterButton, choicebox and brandslist
        filterButton.setOnAction(updateProducts);
        rangeSlider.adjustHighValue(50000);
        sortChoiceBox.getItems().addAll(sortOptions);
        sortChoiceBox.getSelectionModel().selectFirst();
        sortChoiceBox.setOnAction(updateProducts);

        // Binding the value of the range slider to the labels.
        minPriceLabel.textProperty().bind(rangeSlider.lowValueProperty().asString("%.0f"));
        maxPriceLabel.textProperty().bind(rangeSlider.highValueProperty().asString("%.0f"));

        brandCheckList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public void setCategory(String category, String categoryText) {
        // Updating the category title and text.
        categoryHeaderLabel.setText(category);
        categoryTextLabel.setText(categoryText);
        this.category = category;
        // Resetting the sorting box.
        sortChoiceBox.getSelectionModel().selectFirst();
    }

    public void setProducts(ArrayList<PLP_Product> products) {
        gridPane.getChildren().clear(); // Clears previous children

        System.out.println("SET PRODUCTS");
        int numCols = 4;
        // Calculating the amount of rows, by dividing the amount with the number of columns.
        int numRows = (int)Math.ceil(((double) products.size() /numCols));
        int i = 0;
        // Iterating over the rows and columns, and keeping count of the product with i
        for (int row = 0; row < numRows; row++) {
            // Breaking out of the loop, when we run out of products to show
            if (i >= products.size()){
                break;
            }
            for (int col = 0; col < numCols; col++) {
                gridPane.add(products.get(i), col, row);
                i++;
                // Breaking out of the loop, when we run out of products to show
                if (i >= products.size()){
                    break;
                }
            }
        }
    }

    // Eventhandler for updating the view.
    private EventHandler<ActionEvent> updateProducts = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ViewController.getInstance().updatePLP();
        }
    };

    // Method for updating the order of the products based on the chosen filters and sorting
    public ArrayList<Product> updateProducts(ArrayList<Product> products) {
        return sortbyOrder(sortByFilters(products));
    }

    private ArrayList<Product> sortbyOrder(ArrayList<Product> products) {
        //Switch case to select which Comparator to use
        return switch (sortChoiceBox.getSelectionModel().getSelectedItem()) {
            case "Price Ascending" -> {
                products.sort(new AscendingComparator());
                yield products;
            }
            case "Price Descending" -> {
                products.sort(new DescendingComparator());
                yield products;
            }
            case "Alphabetical A-Z" -> {
                products.sort(new AZAlphabeticComparator());
                yield products;
            }
            case "Alphabetical Z-A" -> {
                products.sort(new ZAAlphabetComparator());
                yield products;
            }
            default -> null;
        };
    }

    // Method for filtering by price and brand.
    private ArrayList<Product> sortByFilters(ArrayList<Product> products) {
        ArrayList<Product> priceFiltered = sortByPrice(products);
        ArrayList<Product> brandFiltered = sortByBrand(priceFiltered);
        return brandFiltered;
    }


    private ArrayList<Product> sortByPrice(ArrayList<Product> products) {
        // Variables
        int minPrice = (int) rangeSlider.getLowValue();
        int maxPrice = (int) rangeSlider.getHighValue();

        // List for filtered products
        ArrayList<Product> filteredProducts = new ArrayList<>();

        // Filtering products
        for (Product product : products) {
            if ((minPrice <  product.getPrice()) && (product.getPrice() < maxPrice)) {
                filteredProducts.add(product);
            }
        }

        System.out.println(products);
        System.out.println(filteredProducts);

        return filteredProducts;
    }

    private ArrayList<Product> sortByBrand(ArrayList<Product> products) {
        // If no brands are chosen, no filtering should be done.
        if (brandCheckList.getCheckModel().getCheckedItems().isEmpty()) {
            return products;
        }
        
        ArrayList<Product> filteredProducts = new ArrayList<>();

        // Adding only products that belong to one of the chosen brand.
        for (Product product : products) {
            if (brandCheckList.getCheckModel().getCheckedItems().contains(product.getBrand())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    // Method for extracting the brands from a list of products and adding them to the brandCheckList.
    public void setBrandsFromProducts(List<Product> products) {
        brandCheckList.getItems().clear();
        for (Product product : products) {
            if (!brandCheckList.getItems().contains(product.getBrand())) {
                brandCheckList.getItems().add(product.getBrand());
            }
        }

        // Updating the size of the checklist to match the amount of items.
        brandCheckList.setPrefHeight(brandCheckList.getItems().size() * 26.3);
        System.out.println(brandCheckList);
    }

    public List<Product> defaultSort(List<Product> products) {
        products.sort(new AZAlphabeticComparator());
        return products;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
}
