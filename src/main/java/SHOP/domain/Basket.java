package SHOP.domain;

import SHOP.data.models.Product;
import SHOP.domain.exceptions.*;
import SHOP.domain.order.Order;
import SHOP.domain.order.OrderLine;
import SHOP.domain.order.Payment;
import SHOP.domain.order.Shipping;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Basket {
    private Map<Product, Integer> map;
    private Domain domain;

    public Basket(Map<Product, Integer> map) {
        this.map = map;
    }

    public Basket() {
        this.map = new HashMap<>();
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public float getTotalValue() {
        float total = 0;

        for (Product product : map.keySet()) {
            total += product.getPrice() * map.get(product);
        }

        return total;
    }

    public void addToBasket(Product product, int count) throws InvalidProductException, OutOfStockException {
        //Adds selected products to the basket
        if (product.getName() == null ||
                product.getName().isEmpty() ||
                product.getId() < 0 ||
                product.getPrice() < 0
        ) {
            throw new InvalidProductException();
        }
        //Checks the amount of selected products does not exceed stock count
        if (count > product.getStockCount()) {
            throw new OutOfStockException();
        }

        for (Product mapProduct : map.keySet()) {
            if (product.getId() == mapProduct.getId()) {
                for (int i = 0; i < count; i++) {
                    increment(mapProduct);
                }
                return;
            }
        }
        map.put(product, count);
    }
    public void clearProduct(Product product){
        map.remove(product);
    }

    public Map<Product, Integer> getAllProducts() {
        return map;
    }

    public void increment(Product product) throws OutOfStockException {
        //Increments products in basket
        if (map.get(product) >= product.getStockCount()) {
            throw new OutOfStockException();
        }
        map.put(product, map.get(product) + 1);
    }

    public int getProductCount(Product product) {
        return map.get(product);
    }

    public void setProductCount(Product product, int count) {
        // If the supplied count is 0 or less, the product should be removed from the basket
        if (count <= 0 && map.containsKey(product)) {
            map.remove(product);
        } else {
            map.put(product, count);
        }
    }

    public void clear() {
        map.clear();
    }
    
    public void decrement(Product product) {
        //Decrements products in basket
        if(map.get(product) <= 1){
            clearProduct(product);
        } else{
            map.put(product, map.get(product) -1);
        }
    }
    
    //Submits the order of selected products
    public void submitOrder(boolean isCompany, Payment payment, Shipping shipping) {
        ArrayList<OrderLine> lines = new ArrayList<>();
        Order order = new Order();
        for (Product product : map.keySet()) {
            //Submits order as company, price changes
            float price = isCompany ? product.getPrice() * 0.8f : product.getPrice();
            lines.add(new OrderLine(product.getId(), map.get(product), price));
        }

        //No products chosen
        if (lines.isEmpty()) {
            System.out.println("No order lines found");
            return;
        }
        order.setCompany(isCompany);
        order.setLines(lines);
        order.setPayment(payment);
        order.setShipping(shipping);
        order.setTimestamp(new Date());

        domain.submitOrder(order);
    }
}
