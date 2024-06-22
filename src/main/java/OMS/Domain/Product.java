package OMS.Domain;
/*
As an OMS operator I want to be able to sort based on prices,
 so that I can see if any changes to the pricing of products are necessary.
 (If two prices are the same then sort based on the product's name)
 */
// - I think we need Products class? - name, id, price?
// - sort=Collection - Comparable/Comparator
// - mmh no good, we use a map for the products :(
// - Then I need a list of the Strings and Integers?
// - But then we cant see the price of the products, so we DO need a products class right?

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    @JsonProperty("productId")
    private long id;
    private String name = "product";

    @JsonProperty("unitPrice")
    private double price;

    @JsonProperty("quantity")
    private int amount;
    private String type; //placeholder

    public Product() {

    }
    public Product(long id, int amount, double price, String name) {
        this.amount = amount;
        this.id= id;
        this.name = name;
        this.price= price;
    }

    public Product(long id, int amount, double price) {
        this.amount = amount;
        this.id= id;
        this.price= price;
        //this.type = type; //placeholder
    }

    public Product(long id, int amount) {
        this.amount = amount;
        this.id= id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName(){
        return name;
    }
    public long getId(){
        return id;
    }
    public double getPrice(){
        return price;
    }

    public String getType(){
        return type;
    }

    public int getAmount(){
        return amount;
    }

    @Override
    public String toString(){
        return String.format(
                "Product_id: %d >> \n\t Name: %s \n\t Price: %10.2f\n\t Amount: %d\n\t",
                getId() , (getName() == null ? "Missing name" : getName()) , getPrice(), getAmount());
    }
}

