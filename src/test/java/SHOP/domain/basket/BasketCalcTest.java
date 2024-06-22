package SHOP.domain.basket;

import SHOP.data.models.Product;
import SHOP.domain.Basket;
import SHOP.domain.exceptions.InvalidProductException;
import SHOP.domain.exceptions.OutOfStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class BasketCalcTest {
    @Test
    public void test_total_value_calc_after_added_product() {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_Monitor", 10, null, "", 10, "");
        Product product2 = new Product(1234, "Gaming_PC", 5, null, "", 13, "");

        try {
            basket.addToBasket(product1, 1);
            basket.addToBasket(product2, 2);
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertEquals(20, basket.getTotalValue());
    }
    @Test
    public void test_total_value_calc_after_incr_product() {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_Monitor", 10, null, "", 10, "");

        try {
            basket.addToBasket(product1, 1);
            basket.increment(product1);
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertEquals(20, basket.getTotalValue());
    }

    @Test
    public void test_total_value_calc_after_decrement_product_count() {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_Monitor", 10, null, "", 10, "");
        Product product2 = new Product(1234, "Gaming_PC", 5, null, "", 13, "");

        try {
            basket.addToBasket(product1, 1);
            basket.addToBasket(product2, 2);
            basket.decrement(product2);
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertEquals(15, basket.getTotalValue());
    }

    @Test
    public void test_total_value_calc_after_clear_product() {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_Monitor", 10, null, "", 10, "");
        Product product2 = new Product(1234, "Gaming_PC", 5, null, "", 13, "");

        try {
            basket.addToBasket(product1, 1);
            basket.addToBasket(product2, 2);
            basket.clearProduct(product2);
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertEquals(10, basket.getTotalValue());
    }

    @Test
    public void test_total_value_calc_after_clear_basket() {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_Monitor", 10, null, "", 10, "");
        Product product2 = new Product(1234, "Gaming_PC", 5, null, "", 13, "");

        try {
            basket.addToBasket(product1, 1);
            basket.addToBasket(product2, 2);
            basket.clear();
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertEquals(0, basket.getTotalValue());
    }
}
