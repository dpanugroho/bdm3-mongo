import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Q1 {


	public static void execute() {
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };
        // Initiate mongo
        MongoClient mongoClient = MongoClients.create("mongodb://10.4.41.147:27017");
        MongoDatabase database = mongoClient.getDatabase("mydb");

		MongoCollection<Document> peopleCollection = database.getCollection("people");

		// Project person's name and his/her company domain
        peopleCollection.find(new Document())
                .projection(new Document("name", 1)
                .append("firstName", 1)
                .append("lastName",1)
                .append("company.domain", 1)
                .append("_id", 0))
                .forEach(printBlock);
    }

}
