import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import java.util.Arrays;

public class Q2 {

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

		// Aggregate number of employee for each company
		MongoCollection<Document> peopleCollection = database.getCollection("people");
		peopleCollection.aggregate(
				Arrays.asList(
						Aggregates.group("$company.name", Accumulators.sum("nEmployee",1)))
		).forEach(printBlock);
	}
}
