package SHOP.domain.productComparators;

import SHOP.data.models.Product;

import java.util.Comparator;

public class DescendingComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        //Sorts the products by price in descending order
        if (o1.getPrice() == o2.getPrice()) {
            return o1.getName().compareTo(o2.getName());
        } else if (o1.getPrice() > o2.getPrice()) {
            return -1;
        } else if (o1.getPrice() < o2.getPrice()) {
            return +1;
        }
        return 0;
    }
}
