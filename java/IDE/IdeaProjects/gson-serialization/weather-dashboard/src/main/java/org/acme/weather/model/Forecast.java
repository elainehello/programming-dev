package org.acme.weather.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Forecast {
    
    @SerializedName("list")
    private List<ForecastItem> forecastItems;
    
    @SerializedName("city")
    private City city;
    
    // Getters and setters
    public List<ForecastItem> getForecastItems() {
        return forecastItems;
    }
    
    public void setForecastItems(List<ForecastItem> forecastItems) {
        this.forecastItems = forecastItems;
    }
    
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
    }
    
    // Nested classes
    public static class ForecastItem {
        @SerializedName("dt")
        private long timestamp;
        
        @SerializedName("main")
        private Main main;
        
        @SerializedName("weather")
        private Weather[] weather;
        
        @SerializedName("dt_txt")
        private String dateText;
        
        // Getters and setters
        public long getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
        
        public Main getMain() {
            return main;
        }
        
        public void setMain(Main main) {
            this.main = main;
        }
        
        public Weather[] getWeather() {
            return weather;
        }
        
        public void setWeather(Weather[] weather) {
            this.weather = weather;
        }
        
        public String getDateText() {
            return dateText;
        }
        
        public void setDateText(String dateText) {
            this.dateText = dateText;
        }
    }
    
    public static class Main {
        @SerializedName("temp")
        private double temperature;
        
        @SerializedName("feels_like")
        private double feelsLike;
        
        @SerializedName("humidity")
        private int humidity;
        
        @SerializedName("pressure")
        private int pressure;
        
        // Getters and setters
        public double getTemperature() {
            return temperature;
        }
        
        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }
        
        public double getFeelsLike() {
            return feelsLike;
        }
        
        public void setFeelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
        }
        
        public int getHumidity() {
            return humidity;
        }
        
        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
        
        public int getPressure() {
            return pressure;
        }
        
        public void setPressure(int pressure) {
            this.pressure = pressure;
        }
    }
    
    public static class Weather {
        @SerializedName("main")
        private String main;
        
        @SerializedName("description")
        private String description;
        
        @SerializedName("icon")
        private String icon;
        
        // Getters and setters
        public String getMain() {
            return main;
        }
        
        public void setMain(String main) {
            this.main = main;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getIcon() {
            return icon;
        }
        
        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
    
    public static class City {
        @SerializedName("name")
        private String name;
        
        @SerializedName("country")
        private String country;
        
        @SerializedName("coord")
        private Coordinates coordinates;
        
        // Getters and setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public Coordinates getCoordinates() {
            return coordinates;
        }
        
        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }
    }
    
    public static class Coordinates {
        @SerializedName("lat")
        private double latitude;
        
        @SerializedName("lon")
        private double longitude;
        
        // Getters and setters
        public double getLatitude() {
            return latitude;
        }
        
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
        
        public double getLongitude() {
            return longitude;
        }
        
        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}