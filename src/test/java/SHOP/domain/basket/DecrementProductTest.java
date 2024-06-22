package SHOP.domain.basket;

import SHOP.data.models.Product;
import SHOP.domain.Basket;
import SHOP.domain.exceptions.InvalidProductException;
import SHOP.domain.exceptions.OutOfStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DecrementProductTest {
    @Test
    public void test_decrement_existing_product(){
        Basket basket = new Basket();
        Product product = new Product(12, "Gaming Monitor", 2999, null, null, 20, "");
        try {
            basket.addToBasket(product, 2);
        } catch (InvalidProductException | OutOfStockException e){
            System.out.println("");
        }
        basket.decrement(product);
        Assertions.assertEquals(1, basket.getProductCount(product));
    }

    @Test
    public void test_decrementing_existing_product_with_1_or_lower_product(){
        Basket basket = new Basket();
        Product product = new Product(12, "Gaming Monitor", 2999, null, null, 2, "");
            try {
                basket.addToBasket(product, 1);
            } catch (InvalidProductException | OutOfStockException e){
                System.out.println("");
            }
        basket.decrement(product);
        Assertions.assertFalse(basket.getAllProducts().containsKey(product));
    }
}

