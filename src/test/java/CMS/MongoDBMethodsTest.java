package CMS;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MongoDBMethodsTest {

    @Test
    void saveToCollectionMethodAndDeleteFileFromCollection() {
        Document x = new Document();
        x.append("_id", "1");
        x.append("name", "test1");
        Document y = new Document();
        y.append("_id", "1");
        y.append("name", "test2");
        MongoDBMethods.saveToCollectionMethod(x, "1");
        Assertions.assertEquals(x, MongoDBMethods.getCollection().find(Filters.eq("_id", "1")).first()); // Test input of new document into database
        MongoDBMethods.deleteFileFromCollection("1");
        Assertions.assertNull(MongoDBMethods.getCollection().find(Filters.eq("_id", "1")).first()); // Test deletion of document from database
        MongoDBMethods.saveToCollectionMethod(x, "1");
        MongoDBMethods.saveToCollectionMethod(y, "1");
        Assertions.assertEquals(y, MongoDBMethods.getCollection().find(Filters.eq("_id", "1")).first()); // test replacing of document in database
        MongoDBMethods.deleteFileFromCollection("1");
    }
}