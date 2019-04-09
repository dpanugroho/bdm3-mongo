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
        MongoClient mongoClient = MongoClients.create("mongodb://10.4.41.154:27017");
        MongoDatabase database = mongoClient.getDatabase("test");

//        // Populating People
//        MongoCollection<Document> peopleCollection = database.getCollection("people");
//        // Populate people document to be inserted
//        ArrayList<Document> peopleDocumentArrayList = new ArrayList<>();
//        for (int i=0; i<N; i++){
//            Person person = fairy.person();
//
//            Document doc = new Document("passportNumber", person.getPassportNumber())
//                    .append("firstName", person.getFirstName())
//                    .append("lastName", person.getLastName())
//                    .append("age", person.getAge())
//                    .append("company",new Document("name",person.getCompany().getName())
//                            .append("domain",person.getCompany().getDomain())
//                            .append("email",person.getCompany().getEmail())
//                            .append("url",person.getCompany().getUrl()))
//                    .append("email", person.getEmail());
//            peopleDocumentArrayList.add(doc);
//        }
//        peopleCollection.insertMany(peopleDocumentArrayList);


        MongoCollection<Document> personCollection = database.getCollection("person");
        MongoCollection<Document> companyCollection = database.getCollection("company");

        for (int i = 0; i < N; ++i) {
            Person person = fairy.person();
            Company personsCompany = person.getCompany();
            /**
             * First, check that the person does not already exist
             * The primary key for Person is passport number
             */
            Document personDocument = new Document();
            personDocument.put("_id",person.getPassportNumber());
            /**
             * If the count of document with this passport number is not zero then the person
             * already exists and we can ignore it
             */
            if (personCollection.countDocuments(personDocument) == 0) {
                personDocument.put("age", person.getAge());
                personDocument.put("companyEmail",person.getCompanyEmail());
                personDocument.put("dateOfBirth",person.getDateOfBirth().toString());
                personDocument.put("firstName",person.getFirstName());
                personDocument.put("lastName",person.getLastName());

                //This is the reference to the Company collection
                personDocument.put("worksIn",personsCompany.getName());

                //Insert the document to the collection
                personCollection.insertOne(personDocument);
            }
            /**
             * Same idea as before for companies
             * The primary key for Company is its VAT id number
             */
            Document companyDocument = new Document();
            companyDocument.put("_id",personsCompany.getName());
            if (companyCollection.countDocuments(companyDocument) == 0) {
                companyDocument.put("domain",personsCompany.getDomain());
                companyDocument.put("email",personsCompany.getEmail());
                companyDocument.put("url",personsCompany.getUrl());

                //Insert the document to the collection
                companyCollection.insertOne(companyDocument);
            }
            mongoClient.close();

        }
    }

}