package SHOP.data.models;

import java.util.ArrayList;

public class Product {
    private int id;
    private String ean;
    private String name;
    private float price;
    private String brand;
    private ArrayList<String> highlights;
    private String description;
    private String category;
    private int stockCount;
    private String image;

    public Product() {
    }

    public Product(int id, String name, float price, String brand, String description, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.category = category;
    }

    public Product(int id, String name, float price, String brand, String description, String category, int stockCount, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.stockCount = stockCount;
        this.image = image;
    }

    public Product(int id, String name, float price, ArrayList<String> highlights, String description, int stockCount, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.highlights = highlights;
        this.description = description;
        this.stockCount = stockCount;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArrayList<String> getHighlights() {
        return highlights;
    }

    public void setHighlights(ArrayList<String> highlights) {
        this.highlights = highlights;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockcount) {
        this.stockCount = stockcount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object obj) {
        // Checks if they are the same class and then checks if the ID is the same
        if (obj instanceof Product p2) {
            return id == p2.getId();
        } else {
            return false;
        }
    }
}
