package org.acme.weather.model;

import com.google.gson.annotations.SerializedName;

public class DayForecast {

    @SerializedName("date")
    private String date;

    @SerializedName("temperature_max")
    private String temperatureMax;

    @SerializedName("temperature_min")
    private String temperatureMin;

    @SerializedName("text")
    private String text;

    @SerializedName("humidity")
    private String humidity;

    @SerializedName("wind")
    private String wind;

    @SerializedName("wind_direction")
    private String windDirection;

    @SerializedName("icon")
    private String icon;

    @SerializedName("sunrise")
    private String sunrise;

    @SerializedName("sunset")
    private String sunset;

    @SerializedName("moonrise")
    private String moonrise;

    @SerializedName("moonset")
    private String moonset;

    @SerializedName("moon_phases_icon")
    private String moonPhasesIcon;

    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(String temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(String temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getMoonrise() {
        return moonrise;
    }

    public void setMoonrise(String moonrise) {
        this.moonrise = moonrise;
    }

    public String getMoonset() {
        return moonset;
    }

    public void setMoonset(String moonset) {
        this.moonset = moonset;
    }

    public String getMoonPhasesIcon() {
        return moonPhasesIcon;
    }

    public void setMoonPhasesIcon(String moonPhasesIcon) {
        this.moonPhasesIcon = moonPhasesIcon;
    }
}