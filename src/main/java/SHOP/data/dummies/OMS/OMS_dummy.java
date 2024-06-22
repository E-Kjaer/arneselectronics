package SHOP.data.dummies.OMS;

import OMS.Domain.Interfaces.OMSTOSHOPInterface;
import SHOP.data.interfaces.OMSInterface;

import java.util.Map;

public class OMS_dummy implements OMSTOSHOPInterface {
    // Submitting a fake order. Just returns true and prints the order out
    @Override
    public boolean submitOrder(String jsonString) {
        System.out.println(jsonString);
        return true;
    }

    // Get dummy product count
    @Override
    public int getProductCount(String productId) {
        return 10;
    }

    // Get dummy product count for a number of products
    @Override
    public Map<String, Integer> getProductCounts(String[] productIds) {
        return Map.of();
    }

    // Get order status
    @Override
    public String getOrderStatus(String orderId) {
        return "";
    }
}
