package SHOP.domain.basket;

import SHOP.data.models.Product;
import SHOP.domain.Basket;
import SHOP.domain.exceptions.InvalidProductException;
import SHOP.domain.exceptions.OutOfStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IncrementProductTest {
    @Test
    public void test_increment_existing_product(){
        Basket basket = new Basket();
        Product product = new Product(12, "Gaming Monitor", 2999, null, null, 20, "");

        try {
            basket.addToBasket(product, 2);
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        try {
            basket.increment(product);
        } catch (OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertEquals(3, basket.getProductCount(product));
    }

    @Test
    public void test_incrementing_existing_product_without_stock(){
        Basket basket = new Basket();
        Product product = new Product(12, "Gaming Monitor", 2999, null, null, 2, "");

        try {
            basket.addToBasket(product, 2);
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertThrows(OutOfStockException.class, () -> basket.increment(product));
    }
}
