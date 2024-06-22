package PIM.Domain;

public class Product {
    private int id;
    private String name;
    private String description;
    private String ean;
    private double price;
    private boolean hidden_status;
    private Category category;
    private Brand brand;

    //Constructor
    public Product() {}

    public Product(int id, String name, String description, String ean, double price, boolean hidden_status, Category category, Brand brand) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ean = ean;
        this.price = price;
        this.hidden_status = hidden_status;
        this.category = category;
        this.brand = brand;
    }

    @Override
    public String toString() {
        return
                "Product{" + "id=" + id
                        + ", name=" + name
                        + ", description=" + description
                        + ", ean=" + ean
                        + ", price=" + price
                        + ", hidden status=" + hidden_status
                        + ", category_id=" + category
                        + ", brand_id=" + brand
                        + '}';
    }

    //Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isHidden_status() {
        return hidden_status;
    }

    public void setHidden_status(boolean hidden_status) {
        this.hidden_status = hidden_status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

}
