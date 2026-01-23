package org.acme.weather.model;

import com.google.gson.annotations.SerializedName;

public class WeatherData {

    @SerializedName("copyright")
    private String copyright;

    @SerializedName("use")
    private String use;

    @SerializedName("web")
    private String web;

    @SerializedName("language")
    private String language;

    @SerializedName("locality")
    private Locality locality;

    @SerializedName("day1")
    private DayForecast day1;

    @SerializedName("day2")
    private DayForecast day2;

    @SerializedName("day3")
    private DayForecast day3;

    @SerializedName("day4")
    private DayForecast day4;

    @SerializedName("day5")
    private DayForecast day5;

    @SerializedName("day6")
    private DayForecast day6;

    @SerializedName("day7")
    private DayForecast day7;

    @SerializedName("hour_hour")
    private HourlyForecast hourlyForecast;

    // Getters and setters
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public DayForecast getDay1() {
        return day1;
    }

    public void setDay1(DayForecast day1) {
        this.day1 = day1;
    }

    public DayForecast getDay2() {
        return day2;
    }

    public void setDay2(DayForecast day2) {
        this.day2 = day2;
    }

    public DayForecast getDay3() {
        return day3;
    }

    public void setDay3(DayForecast day3) {
        this.day3 = day3;
    }

    public DayForecast getDay4() {
        return day4;
    }

    public void setDay4(DayForecast day4) {
        this.day4 = day4;
    }

    public DayForecast getDay5() {
        return day5;
    }

    public void setDay5(DayForecast day5) {
        this.day5 = day5;
    }

    public DayForecast getDay6() {
        return day6;
    }

    public void setDay6(DayForecast day6) {
        this.day6 = day6;
    }

    public DayForecast getDay7() {
        return day7;
    }

    public void setDay7(DayForecast day7) {
        this.day7 = day7;
    }

    public HourlyForecast getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(HourlyForecast hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public DayForecast[] getAllDays() {
        return new DayForecast[]{day1, day2, day3, day4, day5, day6, day7};
    }

    public String getFullLocation() {
        if (locality == null || locality.getName() == null) {
            return "Unknown Location";
        }
        String name = locality.getName();
        String country = locality.getCountry();
        if (country != null && !country.isEmpty()) {
            return name + ", " + country;
        }
        return name;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "locality=" + (locality != null ? locality.getName() : "null") +
                ", day1=" + day1 +
                '}';
    }

    // Nested Locality class
    public static class Locality {
        @SerializedName("name")
        private String name;

        @SerializedName("country")
        private String country;

        @SerializedName("url_weather_forecast_15_days")
        private String urlWeatherForecast15Days;

        @SerializedName("url_hourly_forecast")
        private String urlHourlyForecast;

        @SerializedName("url_country")
        private String urlCountry;

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

        public String getUrlWeatherForecast15Days() {
            return urlWeatherForecast15Days;
        }

        public void setUrlWeatherForecast15Days(String urlWeatherForecast15Days) {
            this.urlWeatherForecast15Days = urlWeatherForecast15Days;
        }

        public String getUrlHourlyForecast() {
            return urlHourlyForecast;
        }

        public void setUrlHourlyForecast(String urlHourlyForecast) {
            this.urlHourlyForecast = urlHourlyForecast;
        }

        public String getUrlCountry() {
            return urlCountry;
        }

        public void setUrlCountry(String urlCountry) {
            this.urlCountry = urlCountry;
        }

        @Override
        public String toString() {
            return "Locality{" +
                    "name='" + name + '\'' +
                    ", country='" + country + '\'' +
                    '}';
        }
    }
}