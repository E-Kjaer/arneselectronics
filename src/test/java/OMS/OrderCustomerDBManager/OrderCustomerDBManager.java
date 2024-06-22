package OMS.OrderCustomerDBManager;

import OMS.Domain.Order;
import OMS.Domain.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderCustomerDBManager {
    Order order;
    @BeforeEach
    void setUp(){
        HashMap<String, Integer> products = new HashMap<>();
        products.put("naan",3);
        products.put("pepsi", 1);

       // order = new Order(5, 4,products, 11.12);

    }
    @AfterEach
    void tearDown(){

    }
    @Test
    void setStatus() {
        order.setStatus(Status.SHIPPED);
        assertEquals(order.getStatus(),Status.SHIPPED);
    }

    @Test
    void getStatus() {
        assertEquals(order.getStatus(),Status.PENDING);
    }

    @Test
    void getProducts() {
        HashMap<String, Integer> test = new HashMap<>();
        test.put("naan",3);
        test.put("pepsi", 1);

        assertEquals(order.getProducts(),test);

    }

    @Test
    void testToString() {
    }
}