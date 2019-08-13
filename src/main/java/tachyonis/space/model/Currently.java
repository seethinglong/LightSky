package tachyonis.space.model;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Currently {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int time;
        private String summary;
        private String icon;
        private long nearestStormDistance;
        private long nearestStormBearing;
        private long precipIntensity;
        private long precipProbability;
        private long temperature;
        private long apparentTemperature;
        private long dewPoint;
        private long humidity;
        private long pressure;
        private long windSpeed;
        private long windGust;
        private long windBearing;
        private long cloudCover;
        private long uvIndex;
        private long visibility;
        private long ozone;
        private Date created;
        private Date updated;

        public Currently(){}

        public int getTime() {
                return time;
        }

        public void setTime(int time) {
                this.time = time;
        }

        public String getSummary() {
                return summary;
        }

        public void setSummary(String summary) {
                this.summary = summary;
        }

        public String getIcon() {
                return icon;
        }

        public void setIcon(String icon) {
                this.icon = icon;
        }

        public long getNearestStormDistance() {
                return nearestStormDistance;
        }

        public void setNearestStormDistance(long nearestStormDistance) {
                this.nearestStormDistance = nearestStormDistance;
        }

        public long getNearestStormBearing() {
                return nearestStormBearing;
        }

        public void setNearestStormBearing(long nearestStormBearing) {
                this.nearestStormBearing = nearestStormBearing;
        }

        public long getPrecipIntensity() {
                return precipIntensity;
        }

        public void setPrecipIntensity(long precipIntensity) {
                this.precipIntensity = precipIntensity;
        }

        public long getPrecipProbability() {
                return precipProbability;
        }

        public void setPrecipProbability(long precipProbability) {
                this.precipProbability = precipProbability;
        }

        public long getTemperature() {
                return temperature;
        }

        public void setTemperature(long temperature) {
                this.temperature = temperature;
        }

        public long getApparentTemperature() {
                return apparentTemperature;
        }

        public void setApparentTemperature(long apparentTemperature) {
                this.apparentTemperature = apparentTemperature;
        }

        public long getDewPoint() {
                return dewPoint;
        }

        public void setDewPoint(long dewPoint) {
                this.dewPoint = dewPoint;
        }

        public long getHumidity() {
                return humidity;
        }

        public void setHumidity(long humidity) {
                this.humidity = humidity;
        }

        public long getPressure() {
                return pressure;
        }

        public void setPressure(long pressure) {
                this.pressure = pressure;
        }

        public long getWindSpeed() {
                return windSpeed;
        }

        public void setWindSpeed(long windSpeed) {
                this.windSpeed = windSpeed;
        }

        public long getWindGust() {
                return windGust;
        }

        public void setWindGust(long windGust) {
                this.windGust = windGust;
        }

        public long getWindBearing() {
                return windBearing;
        }

        public void setWindBearing(long windBearing) {
                this.windBearing = windBearing;
        }

        public long getCloudCover() {
                return cloudCover;
        }

        public void setCloudCover(long cloudCover) {
                this.cloudCover = cloudCover;
        }

        public long getUvIndex() {
                return uvIndex;
        }

        public void setUvIndex(long uvIndex) {
                this.uvIndex = uvIndex;
        }

        public long getVisibility() {
                return visibility;
        }

        public void setVisibility(long visibility) {
                this.visibility = visibility;
        }

        public long getOzone() {
                return ozone;
        }

        public void setOzone(long ozone) {
                this.ozone = ozone;
        }

        public Date getCreated() {
                return created;
        }

        public void setCreated(Date created) {
                this.created = created;
        }

        public Date getUpdated() {
                return updated;
        }

        public void setUpdated(Date updated) {
                this.updated = updated;
        }
}
