package CMS;

import SHOP.data.interfaces.CMSInterface;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class CMS implements CMSInterface {
    public Document getPDPLayout(String category) {
        Document existingDocument = MongoDBMethods.getCollection().find(Filters.eq("category", category)).first();
        if (existingDocument != null) {
            return existingDocument;
        }
        return MongoDBMethods.getCollection().find().first();
    }

    public Document getPLPLayout() {
        Document existingDocument = MongoDBMethods.getCollection().find(Filters.eq("_id", "plp")).first();
        if (existingDocument != null) {
            return existingDocument;
        }
        return null;
    }
}
