package org.acme.weather.api;

import org.acme.weather.model.WeatherData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class ApiResponseParser {

    private final Gson gson;

    public ApiResponseParser() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public WeatherData parseWeatherData(String json) throws RuntimeException {
        try {
            if (json == null || json.isEmpty()) {
                throw new RuntimeException("JSON string is null or empty");
            }
            
            System.out.println("Parsing JSON response...");
            System.out.println("JSON length: " + json.length());
            System.out.println("JSON content: " + json);  // ADD THIS LINE TO SEE ACTUAL RESPONSE
            
            WeatherData data = gson.fromJson(json, WeatherData.class);
            
            if (data == null) {
                throw new RuntimeException("Parsed data is null");
            }
            
            System.out.println("Successfully parsed: " + data);
            return data;
        } catch (JsonSyntaxException e) {
            System.err.println("JSON Syntax Error: " + e.getMessage());
            System.err.println("JSON content was: " + json);
            e.printStackTrace();
            throw new RuntimeException("Failed to parse weather data: " + e.getMessage(), e);
        }
    }
}