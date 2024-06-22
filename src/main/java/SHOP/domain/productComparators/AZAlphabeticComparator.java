package SHOP.domain.productComparators;

import SHOP.data.models.Product;

import java.util.Comparator;

public class AZAlphabeticComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        //Sorts the products alphabetically
        return o1.getName().compareTo(o2.getName());
    }
}
