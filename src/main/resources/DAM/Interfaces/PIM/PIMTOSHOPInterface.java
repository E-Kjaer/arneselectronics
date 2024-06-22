package Interfaces.PIM;

public interface PIMTOSHOPInterface {
    // Interface between SHOP and PIM

    // Gets information about a single product to show in the PDP
    public String getProduct(String productId);

    // Get product information for showing a Product Detail Page in the Shop
    public String getProductPDP(String productId);

    // Get product information for showing a product in the basket.
    public String getProductBasket(String productId);

    public String getCategories(); // Returns a JSONString containing the availabe categories.

    // Returns a JSONString with a list of all products in that category.
    // PIM will get the pictures from the DAM.
    public String getProductsByCategory(String category);
}