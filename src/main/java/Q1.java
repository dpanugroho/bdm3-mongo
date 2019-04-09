import com.mongodb.Block;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class Q1 {


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

//		MongoCollection<Document> peopleCollection = database.getCollection("people");
//
//		// Project person's name and his/her company domain
//        peopleCollection.find(new Document())
//                .projection(new Document("name", 1)
//                .append("firstName", 1)
//                .append("lastName",1)
//                .append("company.domain", 1)
//                .append("_id", 0))
//                .forEach(printBlock);
        MongoCollection<Document> personCollection = database.getCollection("person");

        AggregateIterable<Document> q1 = personCollection.aggregate(Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from","company")
                        .append("localField","worksIn")
                        .append("foreignField","_id")
                        .append("as","company")
                )
        ));

        System.out.println("Q1: For each person, its name an its company name");
        for (Document d : q1 ) {
            Document company = (Document) d.get("company", List.class).get(0);
            System.out.println(d.get("firstName") + " " + d.get("lastName") + " works in " + company.get("_id"));
        }
        mongoClient.close();
    }

}
