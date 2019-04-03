import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.person.Person;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;


public class populateDB {

	public static void populate(int N) {
	    // Initiate fairy
		Fairy fairy = Fairy.create();

		// Initiate mongo
        MongoClient mongoClient = MongoClients.create("mongodb://10.4.41.147:27017");
        MongoDatabase database = mongoClient.getDatabase("mydb");

        // Populating People
        MongoCollection<Document> peopleCollection = database.getCollection("people");
        // Populate people document to be inserted
        ArrayList<Document> peopleDocumentArrayList = new ArrayList<>();
        for (int i=0; i<N; i++){
            Person person = fairy.person();

            Document doc = new Document("passportNumber", person.getPassportNumber())
                    .append("firstName", person.getFirstName())
                    .append("lastName", person.getLastName())
                    .append("age", person.getAge())
                    .append("company",new Document("name",person.getCompany().getName())
                            .append("domain",person.getCompany().getDomain())
                            .append("email",person.getCompany().getEmail())
                            .append("url",person.getCompany().getUrl()))
                    .append("email", person.getEmail());
            peopleDocumentArrayList.add(doc);
        }
        peopleCollection.insertMany(peopleDocumentArrayList);
    }

}