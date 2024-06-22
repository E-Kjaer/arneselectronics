package OMS.Domain.Interfaces;

import javax.mail.MessagingException;
import java.util.Map;

public interface OMSTOSHOPInterface {
    boolean submitOrder(String jsonString) throws MessagingException;
    int getProductCount(String productId);
    Map<String, Integer> getProductCounts(String[] productIds);
    String getOrderStatus(String orderId);
    //String getOrderStatus(String[] orderIds);
}

