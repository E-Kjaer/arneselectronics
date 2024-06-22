package OMS.Domain.Interfaces;

import OMS.Domain.Product;
import javafx.scene.image.Image;

public interface DAMTOOMSInterface {
    public Image getProductImage(Product product);
    public Image getProductImage(long productId);
}