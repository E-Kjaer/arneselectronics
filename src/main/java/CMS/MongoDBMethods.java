package CMS;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import java.util.Objects;

public class MongoDBMethods {

    private static final String connectionString = "mongodb://localhost:27017";
    private static final String databaseName = "CMS";
    private static final String collectionName = "CMSMain";

    private static MongoClient mongoClient = MongoClients.create(connectionString);
    private static MongoDatabase database = mongoClient.getDatabase(databaseName);
    private static MongoCollection<Document> collection = database.getCollection(collectionName);

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static MongoCollection<Document> getCollection() {
        return collection;
    }

    public static void deleteFileFromCollection(String id) {
        // Check if a document with the same ID exists
        Document existingDocument = getCollection().find(Filters.eq("_id", id)).first();

        if (existingDocument != null) {
            // Delete the document with the matching ID
            DeleteResult deleteResult = getCollection().deleteOne(Objects.
                    requireNonNull(MongoDBMethods.getCollection().
                            find(Filters.eq("_id", id)).first()));

            // Check if the deletion was successful
            if (deleteResult.getDeletedCount() == 1) {
                System.out.println("Document with ID: " + id + " deleted successfully.");
            } else {
                System.out.println("No document found with ID: " + id);
            }
        }
    }

    public static void saveToCollectionMethod(Document cmsType, String idType) {
        Document existingDocument = getCollection().find(Filters.eq("_id", idType)).first();

        if (existingDocument != null) {
            // Update the existing document
            UpdateResult updateResult = getCollection().replaceOne(Filters.eq("_id", idType), cmsType);
            System.out.println("Document updated: " + updateResult.getModifiedCount());
        } else {
            // Insert the document if no existing document found
            getCollection().insertOne(cmsType);
            System.out.println("Document inserted");
        }
    }

}
