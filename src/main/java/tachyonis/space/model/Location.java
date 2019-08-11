// Map to MongoRepository
// https://www.technicalkeeda.com/spring-tutorials/spring-4-mongodb-repository-example

package tachyonis.space.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Location {
    private String latitude;
    private String longitude;
    private String timezone;
    private Currently currently;

    //Location Constructor
    public Location() {
        super();
    }

    public String getLatitude(){
        return latitude;
    }

    public void setlatitude(String latitude){
        this.latitude = latitude;
    }

    public String getlongitude(){
        return longitude;
    }

    public void setlongitude(String longitude){
        this.longitude = longitude;
    }

    public String gettimezone(){
        return timezone;
    }

    public void settimezone(String timezone ){
        this.timezone = timezone;
    }

    public Currently getcurrently(){return currently;}

    public void setcurrently(){ this.currently = currently ; }

    @Override
    public String toString() {
        return "Location=" + latitude + "," + longitude + " timezone=" + timezone + " data " + currently ;
    }

}