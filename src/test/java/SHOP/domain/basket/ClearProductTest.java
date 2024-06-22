package SHOP.domain.basket;

import SHOP.data.models.Product;
import SHOP.domain.Basket;
import SHOP.domain.exceptions.InvalidProductException;
import SHOP.domain.exceptions.OutOfStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ClearProductTest {

    @Test
    public void test_map_can_remove_product(){
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_Monitor", 4999, null, "", 10, "");

        try {
            basket.addToBasket(product1, 2);
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        basket.clearProduct(product1);

        Map<Product, Integer> map = basket.getAllProducts();
        Assertions.assertFalse(map.containsKey(product1));
    }
}

