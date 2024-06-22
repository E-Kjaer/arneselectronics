package SHOP.data.models;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Products {
    private ArrayList<Product> products;

    public Products() {};

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
