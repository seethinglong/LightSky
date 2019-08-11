//    9 Aug 2019 JSON mapping to Java object Serialization by Tachyonis Space
//    String json = "...";
//    Weather weather = new ObjectMapper().readValue(json, Weather.class);
//    Using jackson-databind 2.9.9.3
//    https://github.com/FasterXML/jackson-databind
//    POJOs to JSON and back
//    JSON supports mainly 6 data types:
//
//    string
//    number
//    boolean
//    null/empty
//    object
//    array

package tachyonis.space.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Currently {

        private int time;
        private String summary;
        private String icon;
        private Double nearestStormDistance;
        private Double precipIntensity;
        private Double precipProbability;
        private Double temperature;
        private String precipType;


        public Currently() {
        }

        public void settime(int time) { this.time = time ;}
        public int gettime(){ return time; }

        public String getsummary(){
                return summary;
        }

        public void setsummary(String summary){
                this.summary = summary;
        }

        public String geticon(){
                return icon;
        }

        public void icon(String icon){
                this.icon = icon;
        }

        public Double gettemperature(){
                return temperature;
        }

        public void settemperature(Double temperature ){
                this.temperature = temperature;
        }

        @Override
        public String toString() {
                return "{ icon:" + icon + ", summary:" + summary + ", temperature:" + temperature + "}";
        }
}
