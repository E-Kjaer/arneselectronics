package SHOP.domain.productComparators;

import SHOP.data.models.Product;

import java.util.Comparator;

public class ZAAlphabetComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        //Sorts the products in reverse alphabetically
        return o2.getName().compareTo(o1.getName());
    }
}
