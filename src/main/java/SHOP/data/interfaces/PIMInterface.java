package SHOP.data.interfaces;

import DAM.g1.data.AccessDatabase;

public interface PIMInterface {
    // Gets information about a single product to show in the PDP
    public String getProductByIdJson(int productId);

    public String getProductsJson();

    public String getCategoriesJson();

    public String getSubcategoriesByCategoryIdJson(int categoryId);

    public String getBrandsJson();

    // Returns a JSONString with a list of all products in that category.
    // PIM will get the pictures from the DAM.
    public String getProductsByCategoryJson(int category);

    public String getProductsByNameJson(String name);
}
