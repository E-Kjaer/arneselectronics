package SHOP.data.interfaces;

import org.bson.Document;

public interface CMSInterface {
    public Document getPLPLayout();
    public Document getPDPLayout(String category);
}
