package Interfaces.OMS;

public interface OMSTOSHOPInterface {
    public int submitOrder(String jsonString);
    public int getProductCount(String productId);
    public int[] getProductCounts(String[] productIds);
    public String getOrderStatus(String orderId);
    public String getOrderStatus(String[] orderIds);
    public int createAccount(String jsonString);
    public String checkAccount(String username, String password); //Return Id OR jsonString
    public boolean deleteAccount(String accountId);
}
