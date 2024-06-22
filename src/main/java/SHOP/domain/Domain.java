package SHOP.domain;

import SHOP.data.Data;
import SHOP.data.models.Guide;
import SHOP.data.models.Product;
import SHOP.domain.order.Order;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Domain {
    private Data data;
    private Basket basket;

    public static void main(String[] args) {
        Data data = new Data();

        List<Product> products = data.getProductsByCategory("Computers & Office");
        for (Product product1 : products) {
            System.out.println(product1.getName());
        }
        ArrayList<Guide> guides = data.getGuides();
    }

    public Basket getBasket() {
        return basket;
    }

    public Domain() {
        this.basket = new Basket();
        this.data = new Data();
        basket.setDomain(this);
    }

    public Document getPLPLayout() {
        return data.getPLPLayout();
    }

    public Document getPDPLayout(String category) {
        return data.getPDPLayout(category);
    }

    public Product getProduct(int id) {
        return data.getProduct(id);
    }

    public ArrayList<Product> getProductsByCategory(String category) {
        return data.getProductsByCategory(category);
    }

    public Map<Product, Integer> getAllProducts() {
        return basket.getAllProducts();
    }

    public void submitOrder(Order order) {
        data.submitOrder(order);
    }

    public ArrayList<Guide> getGuides() {
        return data.getGuides();
    }

    public List<Product> getProductsBySearch(String search) {
        return data.getProductsBySearch(search);
    }

    public String getLogo(){
        return data.getLogo();
    }
}
