package OMS.Domain.Interfaces;

import OMS.Domain.Product;

public interface PIMTOOMSInterface {
    public Product[] getAllProducts();
    public String getDescription(Product product);
    public String getProductName(Product product);
    public String getDescription(long productId);
    public String getProductName(long productId);
}
