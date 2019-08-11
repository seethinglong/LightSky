package tachyonis.space;

import java.util.Date;
import org.joda.time.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Connecting to DarkSky with URI webservice
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import lombok.NonNull;

//Java Latest Drive https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver/3.11.0-beta4
//(August 9, 2019)
//Seet Hing-Long:
//Jersey JAX-RS Client for CRUD Restful Web Services
//

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Arrays;
import com.mongodb.Block;

import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

import org.springframework.stereotype.Service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import tachyonis.space.model.Location;
import tachyonis.space.model.Currently;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
@Service

public class TachyonService {
  private static final Logger log = LoggerFactory.getLogger(TachyonService.class);
  private List<Currently> C = new ArrayList<Currently>();
  private Currently c;
  private static MongoClient mongoClient;
  private static MongoDatabase db;
  private static MongoCollection<Document> collection;
  private static List<String> coordinates;
  //private static String uri = "https://api.darksky.net/forecast/605f52f879be7647dd460139220c83c8/";
  private static String uri = "https://api.darksky.net/forecast/6a3b5656878a2c807e2b0175cde1462b/";

  public TachyonService() {
    super();
    String user = "tachyon";
    String database = "admin";
    String password = "tachyon";
    String serveraddress1 = "cluster0-shard-00-00-out9e.mongodb.net";
    String serveraddress2 = "cluster0-shard-00-01-out9e.mongodb.net";
    String serveraddress3 = "cluster0-shard-00-02-out9e.mongodb.net";
    coordinates = Arrays.asList("37.288,-121.946","41.259,-95.938","30.271,-97.744",
            "34.296,135.882","-5.888,107.065");

    int port = 27017;
    char[] pw = password.toCharArray();
    //Clusters of Servers
    List<ServerAddress> servers = Arrays.asList(new ServerAddress(serveraddress1, port),
            new ServerAddress(serveraddress2, port),
            new ServerAddress(serveraddress3, port));

    MongoCredential credential = com.mongodb.MongoCredential.createCredential(user, database, pw);

    MongoClientSettings settings = MongoClientSettings.builder()
            .credential(credential)
            .applyToSslSettings(builder -> builder.enabled(true))
            .applyToClusterSettings(builder ->
                    builder.hosts(servers))
            .build();
    mongoClient = MongoClients.create(settings);
    db = mongoClient.getDatabase("tachyon-sky");
    for (String name : this.db.listCollectionNames()) {
      log.info("Database Collection name " + name);
      MongoCollection<Document> collection = db.getCollection(name);
      long x = collection.countDocuments();
      log.info("Collection total Documents " + x);
    }
    HouseKeepMongoDB();
    //- Campbell, CA, 37.288,-121.946
    //- Omaha, NE, 41.259,-95.938
    //- Austin, TX, 30.271,-97.744
    //- Niseko, Japan, 42.805,140.687
    //- Nara, Japan and 34.296,135.882
    //- Jakarta, Indonesia -5.888,107.065 latitude,longitude


    //ClientSession cs = mongoClient.startSession();
    //MongoCollection<Document> collection = db.getCollection("weather");
    }

  private final int HTTP_CONNECT_TIMEOUT = 10000;
  private final int HTTP_READ_TIMEOUT = 10000;
  //TODO add the error handling for REST Template  DarkSky Error response
  private ClientHttpRequestFactory TachyonRest() {
    HttpComponentsClientHttpRequestFactory c = new HttpComponentsClientHttpRequestFactory();
    c.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
    c.setReadTimeout(HTTP_READ_TIMEOUT);
    return c;
  }

  public void getAll(){
    //TODO RESTFUL to retrieve response and save to MongoDB
    try{
      RestTemplate TachyonServiceRest = new RestTemplate();
      for (String coordinate : this.coordinates) {
        String json = TachyonServiceRest.getForObject(this.uri + coordinate, String.class);
        //If DarkSky no response for certain time Show error maximum at 1 second perfomance per transaction
        //log.info(json.substring(1, 100));
        WriteToMongoDB(json);

      }
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    } catch (Exception e) {
      log.error("Unknwon Error " + e.getMessage());
    }
  }

  // SEET HING LONG Strong String from REST response and write in to Mongo DB Document
  // Object ID is unique by time -> Hex
  //
  // https://mongodb.github.io/mongo-java-driver/3.11/driver/tutorials/perform-write-operations/
  public void HouseKeepMongoDB(){
    //Delete all collections with time older than 3 days using the time property
    // Housekeep forecast records that already 3 days old time appear in seconds
    // 3 days = 24 x 3 x 60 x 60 = 259200
    // Retrive the current time from DarkSky and delete document with time less than the value 259200
    // obid is timebased 11 August 2019
    // http://mongodb.github.io/mongo-java-driver/3.11/javadoc//index.html?org/bson/types/ObjectId.html
    // Using JAVA SYNC https://docs.mongodb.com/manual/tutorial/query-documents/
    // Reactive ASYNC driver is different
    // https://mongodb.github.io/mongo-java-driver/3.11/javadoc/com/mongodb/client/MongoCollection.html#find--

    MongoCollection<Document> collection = db.getCollection("weather");
    log.debug("HouseKeep Before " +  collection.countDocuments());
    Date d = new DateTime().minusMinutes(10).toDate();
    ObjectId oid = new ObjectId(d);
    String t = oid.toHexString();
    log.debug("HouseKeep OID timestamp smaller than " + t );
    //https://docs.mongodb.com/manual/tutorial/remove-documents/
    collection.deleteMany(lt("_id", oid ));
    collection = db.getCollection("weather");
    log.debug("HouseKeep After  " + collection.countDocuments());
  }

  public void FindMongoCollection(){

    MongoCollection<Document> collection = db.getCollection("weather");
    log.debug("Total Collections Documents = " +  collection.countDocuments());
    log.debug("Find America/Cancun " +  count(collection.find(eq("timezone", "America/Cancun"))));
    log.debug("Find latitude -5.888 " + count(collection.find(eq("latitude", -5.888 ))));

    Date d = new DateTime().minusHours(12).toDate();
    ObjectId oid = new ObjectId(d);
    String t = oid.toHexString();
    log.info("Find _id greater than " + t );
    FindIterable<Document> findIterable =
            collection.find(gt("_id", oid ));
            log.debug("_id gt 30 Minutes  " + count(findIterable));
  }

  //lombok 1.18.8 @NonNull
  protected int count(@NonNull FindIterable<Document> findIterable){
    int count = 0;
    for(Document c: findIterable) {
      count++;
    }
    return count;
  }

  protected void WriteToMongoDB(String json) {
    //  private MongoClient m
    //  private MongoDatabase db
    //BsonArray parse = BsonArray.parse(json);
    //BasicDBList dbList = new BasicDBList();
    //dbList.addAll(parse);
    //DBObject dbObject = dbList;
    //DBCursor cursorDoc = collection.find();
    // If no top-level _id field is specified in the document, the Java driver automatically adds the _id field to the inserted document.
    try {
      MongoCollection<Document> collection = db.getCollection("weather");
      long x = collection.countDocuments();
      Document d = Document.parse(json);
      collection.insertOne(d);
      //Count 5 locations total documents updated with bson filter
      long y = collection.countDocuments();
      log.info("Collection increased from " + x + " to " + y);
    } catch (MongoException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] argv) {
    try {
      //https://mongodb.github.io/mongo-java-driver/3.11/driver/tutorials/connect-to-mongodb/
      //Connect to a Sharded Cluster/Standalone
      //DarkSky using https SSL/TLS
      //MongoClient mongoClient = MongoClients.create("mongodb://user1:pwd1@host1/?authSource=db1&ssl=true");
      //MongoClient mongoClient = MongoClients.create("mongodb://tachyon:tachyon@tcluster0-out9e.mongodb.net:27017/?authSource=admin@ssl=true");
      // ...
      String user = "tachyon";
      String database = "admin";
      String password = "tachyon";
      String serveraddress1 = "cluster0-shard-00-00-out9e.mongodb.net";
      String serveraddress2 = "cluster0-shard-00-01-out9e.mongodb.net";
      String serveraddress3 = "cluster0-shard-00-02-out9e.mongodb.net";

      int port = 27017;
      char[] pw = password.toCharArray();
      //Clusters of Servers
      List<ServerAddress> servers = Arrays.asList(new ServerAddress(serveraddress1,port),
                                                  new ServerAddress(serveraddress2,port),
                                                  new ServerAddress(serveraddress3,port));

      MongoCredential credential = com.mongodb.MongoCredential.createCredential(user, database, pw );

      MongoClientSettings settings = MongoClientSettings.builder()
              .credential(credential)
              .applyToSslSettings(builder -> builder.enabled(true))
              .applyToClusterSettings(builder ->
                      builder.hosts(servers))
              .build();
      //List<String> list = Arrays.asList(stringArray);
      MongoClient mongoClient = MongoClients.create(settings);

      MongoDatabase db = mongoClient.getDatabase("tachyon-sky");
      //ClientSession cs = mongoClient.startSession();
      MongoCollection<Document> collection = db.getCollection("weather");
      //Query from Darksky weather data using RESTful URI
      //Jersey ClientBuilder

      /*convert TachyonController Retrieve JSON to DBObject directly and insert into MongoDB
      BsonArray parse = BsonArray.parse(json);
      BasicDBList dbList = new BasicDBList();
      dbList.addAll(parse);
      DBObject dbObject = dbList;
      DBCursor cursorDoc = collection.find();
      collection.insert(dbObject);
      */
      mongoClient.close();


    } catch (MongoException e) {
      e.printStackTrace();
    } finally{

    }
  }
    // Please, do not remove this line from file template, here invocation of web service will be inserted  
  }
