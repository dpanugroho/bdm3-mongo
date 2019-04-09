import com.mongodb.Block;
import com.mongodb.client.*;
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
		MongoClient mongoClient = MongoClients.create("mongodb://10.4.41.154:27017");
		MongoDatabase database = mongoClient.getDatabase("test");

//		// Aggregate number of employee for each company
//		MongoCollection<Document> peopleCollection = database.getCollection("people");
//		peopleCollection.aggregate(
//				Arrays.asList(
//						Aggregates.group("$company.name", Accumulators.sum("nEmployee",1)))
//		).forEach(printBlock);
		MongoCollection<Document> companyCollection = database.getCollection("company");

		AggregateIterable<Document> q2 = companyCollection.aggregate(Arrays.asList(
				new Document("$lookup", new Document()
						.append("from","person")
						.append("localField","_id")
						.append("foreignField","worksIn")
						.append("as","employees")
				),
				new Document("$project", new Document()
						.append("_id",1)
						.append("employees", new Document("$size","$employees"))
				)
		));

		System.out.println("Q2: For each company, the name and the number of employees");
		for (Document d : q2 ) {
			System.out.println(d.get("_id") + " has " + d.get("employees")
					+ (d.getInteger("employees") == 1 ? " employee." : " employees."));
		}
	}
}
