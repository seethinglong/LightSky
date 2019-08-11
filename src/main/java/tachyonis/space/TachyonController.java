/*
@Path: It specifies the relative URI path to a resource. Can be used at class or method level.
@GET: Used at method level that specifies method responds to HTTP GET Request.
@POST: Used at method level that specifies method responds to HTTP POST Request. Basically used  to create a new resource.
@PUT: Used at method level that specifies method responds to HTTP PUT Request. Use this to update    your resource.
@DELETE: Used at method level that specifies method responds to HTTP DELETE Request. Use this to    delete the resourse.
@Produces: Used to define media type or response format that resourse method will produce such as XML, PLAIN TEXT, JSON etc.
@Consumes: Used to specify the media type that the resource method can accept and process.
@ApplicationPath: Used to define URL mapping for all resources. The path specified by @ApplicationPath is the base URI for all resource URIs specified by @Path annotations in the resource class. Basically used on the configuration class i.e. the subclass of javax.ws.rs.core.Application (eg. ResourceConfig in our case). Here we will register our class component (EmployeeController.class).
@PathParam: It is used to specify the URI path parameters that are extrated from a request URI path template specified in the @Path annotation. A URI template is a URI-like String that contains variables enclosed by braces {}.
 */

// TODO MongoDB Index by Object ID created by location [ coordinate ] and [ time ] inside currently
// TODO Tachyon Controller is CRUD WEBservice using Spring Framework to Retreive trigger by Java cron service regularly update the Document in MongoDB
// TODO Mongo DB will be purge by Java cronservice to housekeep by the Data for a Duration [ variable ]
// TODO TachyonService will store the JSON response from store the Response into JSON String
// TODO Document will be checked against MongoDB only if it does not exist and using BSON to parse As collection
// TODO All data have common sets of data UNQIUE by 3 KEYS time and latitude, longitude
// TODO MongoDB Object ID is unique at runtime using time of execution
// TODO Returns a new ObjectId value. The 12-byte ObjectId value consists of:
//a 4-byte value representing the seconds since the Unix epoch,
//a 5-byte random value, and
//a 3-byte counter, starting with a random value.

package tachyonis.space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

import tachyonis.space.model.Location;
import tachyonis.space.model.Currently;
//Jackson Databind 2.9.9.3 jackson-databind
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Service
@ApplicationPath("https://api.darksky.net/forecast/605f52f879be7647dd460139220c83c8/37.8267,-122.4233")

public class TachyonController {
    private static final Logger log = LoggerFactory.getLogger(TachyonController.class);
    private List<Currently> C = new ArrayList<Currently>();
    private Currently c;
    private TachyonService tachyonService;

    String coordinate="37.8267,-122.4233";
    String apikey = "605f52f879be7647dd460139220c83c8";
    String path = "https://api.darksky.net/forecast";
    String oid = coordinate;

    public TachyonController(TachyonService ts){
        this.tachyonService= ts;
    }
    @GET
    @Produces("application/json")
    public void getAll() {
        //Service to GetAll and update MongoDB with JSON
        tachyonService.getAll();
    }

    public void HouseKeepMongoDB(){
        tachyonService.HouseKeepMongoDB();
    }

    public void FindMongoCollection(){
        tachyonService.FindMongoCollection();
    }

    @GET
    @Produces("application/json")
    @Path("/{oid}")
    public Currently get(@PathParam("oid") String oid) {
        Currently cr = new Currently();
        // TODO cr = TODO TachyonService.get(oid);
        return cr;
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response add() {
        String city = "America/Los_Angeles";
        String url = path;
        // TODO Jersey REST API
        // TODO String json = ClientBuilder.newClient().target(url).request().accept("application/json").get(String.class);
        // Using jackson-databind 2.9.9.3
        // Jackson databind Jackson ObjectMapper
        try {
            String json = "";
            Currently currently = new ObjectMapper().readValue(json, Currently.class);
            // TODO TachyonService.add(currently);
            return Response.created(URI.create(currently.toString())).build();
        }catch(IOException e) {
            log.info(e.toString());
            return Response.noContent().build();
        }
    }

    @PUT
    @Consumes("application/json")
    @Path("/{oid}")
    public Response update(@PathParam("oid") String oid, Currently currently) {
        //TODO TachyonService.update(oid, currently);
        return Response.noContent().build();
    }
    @DELETE
    @Path("/{oid}")
    public Response delete(@PathParam("oid") String oid) {
        //TODO TachyonService.delete(oid);
        //To delete data by Webservice
        return Response.ok().build();
    }
}