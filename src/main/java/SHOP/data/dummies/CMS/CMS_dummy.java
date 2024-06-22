package SHOP.data.dummies.CMS;

import SHOP.data.interfaces.CMSInterface;
import org.bson.Document;

public class CMS_dummy implements CMSInterface {
    @Override
    public Document getPLPLayout() {
        // Returns a dummy document like one from the CMS for the PLP layout
        return new Document("_id", 0.0)
                .append("titleplpX", 60.0)
                .append("titleplpY", 115.0)
                .append("titleplpFontType", "System Bold")
                .append("titleplpFontSize", 18.0)
                .append("titleplpFontColor", "0x333333ff")
                .append("priceplpX", 60.0)
                .append("priceplpY", 180.0)
                .append("priceplpFontType", "System")
                .append("priceplpFontSize", 36.0)
                .append("priceplpFontColor", "0x333333ff")
                .append("pictureplpX", 60.0)
                .append("pictureplpY", 10.0)
                .append("backgroundplpColor", "0x333333ff");
    }

    @Override
    public Document getPDPLayout(String category) {
        // Returns a dummy document like one from the CMS for the PDP layout
        return new Document("_id", 0.0)
                .append("titleX", 350.0)
                .append("titleY", 50.0)
                .append("titleFontType", "System Bold")
                .append("titleFontSize", 25.0)
                .append("titleFontColor", "0x333333ff")
                .append("descriptionX", 350.0)
                .append("descriptionY", 400.0)
                .append("descriptionFontType", "System")
                .append("descriptionFontSize", 20.0)
                .append("descriptionFontColor", "0x333333ff")
                .append("priceX", 700.0)
                .append("priceY", 126.0)
                .append("priceFontType", "System")
                .append("priceFontSize", 20.0)
                .append("priceFontColor", "0x333333ff")
                .append("addToCartX", 700.0)
                .append("addToCartY", 200.0)
                .append("addToCartFontType", "System")
                .append("addToCartFontSize", 12.0)
                .append("addToCartFontColor", "0x333333ff")
                .append("pictureX", 375.0)
                .append("pictureY", 125.0)
                .append("backgroundColor", "0xffffffff");
    }
}
