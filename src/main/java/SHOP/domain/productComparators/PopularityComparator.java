package SHOP.domain.productComparators;

import SHOP.data.models.Product;

import java.util.Comparator;

public class PopularityComparator implements Comparator<Product> {
    @Override
    //Can only be implemented properly if enough shopping data is gathered
    public int compare(Product o1, Product o2) {
        return 0;
    }
}
