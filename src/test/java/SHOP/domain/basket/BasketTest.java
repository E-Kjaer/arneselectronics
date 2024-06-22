package SHOP.domain.basket;

import SHOP.data.models.Product;
import SHOP.domain.Basket;
import SHOP.domain.exceptions.InvalidProductException;
import SHOP.domain.exceptions.OutOfStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class BasketTest {
    @Test
    public void test_map_can_store_valid_product() {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_Monitor", 4999, null, "", 10, "");
        Product product2 = new Product(1234, "Gaming_PC", 9999, null, "", 13, "");

        try {
            basket.addToBasket(product1, 2);
            basket.addToBasket(product2, 3);
        } catch (InvalidProductException | OutOfStockException e) {
            System.out.println(e.getMessage());
        }

        Map<Product, Integer> map = basket.getAllProducts();

        Assertions.assertTrue(map.containsKey(product1) && map.containsKey(product2));
    }

    @Test
    public void test_map_can_reject_invalid_product_name() {
        Basket basket = new Basket();
        Product product1 = new Product(123, null, 4999, null, "", 10, "");
        Product product2 = new Product(123, "", 4999, null, "", 10, "");

        Assertions.assertThrows(InvalidProductException.class, () -> basket.addToBasket(product1, 1));
        Assertions.assertThrows(InvalidProductException.class, () -> basket.addToBasket(product2, 1));
    }

    @Test
    public void test_map_can_reject_invalid_product_id() {
        Basket basket = new Basket();
        Product product1 = new Product(-1, "Gaming_PC", 4999, null, "", 10, "");
        Product product2 = new Product(0, "", 4999, null, "", 10, "");

        Assertions.assertThrows(InvalidProductException.class, () -> basket.addToBasket(product1, 1));
        Assertions.assertThrows(InvalidProductException.class, () -> basket.addToBasket(product2, 1));
    }

    @Test
    public void test_map_can_reject_invalid_price() {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_PC", -1, null, "", 10, "");

        Assertions.assertThrows(InvalidProductException.class, () -> basket.addToBasket(product1, 1));
    }

    @Test
    public void test_map_can_reject_low_stock() {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_PC", 1, null, "", 1, "");

        Assertions.assertThrows(OutOfStockException.class, () -> basket.addToBasket(product1, 2));
    }


    @Test
    public void test_basket_can_clear() throws OutOfStockException, InvalidProductException {
        Basket basket = new Basket();
        Product product1 = new Product(123, "Gaming_PC", 1, null, "", 10, "");

        basket.addToBasket(product1, 2);
        basket.clear();

        Assertions.assertEquals(0, basket.getAllProducts().size());
    }
}