package org.acme.weather.api;

import org.acme.weather.model.WeatherData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private final HttpClient httpClient;
    private final ApiResponseParser parser;
    private static final String API_BASE_URL = "https://api.tutiempo.net/json/";
    private final String apiKey;

    public WeatherService() {
        this("zwDX4azaz4X4Xqs"); // Default API key
    }

    public WeatherService(String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.parser = new ApiResponseParser();
        this.apiKey = apiKey;
    }

    /**
     * Get weather data by coordinates (latitude, longitude)
     */
    public WeatherData getWeatherByCoordinates(double latitude, double longitude) throws Exception {
        String url = String.format(
                "%s?lan=en&apid=%s&ll=%.4f,%.4f", 
                API_BASE_URL, apiKey, latitude, longitude
        );
        System.out.println("Request URL: " + url);
        return fetchWeatherData(url);
    }

    /**
     * Get weather data by city code
     */
    public WeatherData getWeatherByCityCode(String cityCode) throws Exception {
        String url = String.format(
                "%s?lan=en&apid=%s&localidad=%s", 
                API_BASE_URL, apiKey, cityCode
        );
        System.out.println("Request URL: " + url);
        return fetchWeatherData(url);
    }

    private WeatherData fetchWeatherData(String url) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("Accept", "application/json")
                    .header("User-Agent", "WeatherDashboard/1.0")
                    .build();

            System.out.println("Sending HTTP request...");
            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Response status: " + response.statusCode());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "API request failed with status: " + response.statusCode() + 
                        "\nResponse: " + response.body()
                );
            }

            String responseBody = response.body();
            if (responseBody == null || responseBody.isEmpty()) {
                throw new RuntimeException("Empty response from API");
            }

            System.out.println("Response body length: " + responseBody.length());
            return parser.parseWeatherData(responseBody);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("API request was interrupted", e);
        }
    }
}