package org.acme.weather.ui;

import org.acme.weather.api.WeatherService;
import org.acme.weather.model.DayForecast;
import org.acme.weather.model.WeatherData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private TextField latitudeTextField;

    @FXML
    private TextField longitudeTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Label locationLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label windLabel;

    @FXML
    private Label sunriseLabel;

    @FXML
    private Label sunsetLabel;

    @FXML
    private VBox forecastContainer;

    private WeatherService weatherService;

    @FXML
    public void initialize() {
        weatherService = new WeatherService("zwDX4azaz4X4Xqs"); // Use your API key
        latitudeTextField.setText("40.4178");
        longitudeTextField.setText("-3.7022");

        // Auto-load on startup
        handleSearch();
    }

    @FXML
    private void handleSearch() {
        String latStr = latitudeTextField.getText();
        String lonStr = longitudeTextField.getText();

        if (latStr == null || latStr.trim().isEmpty() ||
                lonStr == null || lonStr.trim().isEmpty()) {
            locationLabel.setText("Please enter valid coordinates");
            return;
        }

        try {
            double latitude = Double.parseDouble(latStr);
            double longitude = Double.parseDouble(lonStr);

            searchButton.setDisable(true);
            searchButton.setText("Loading...");

            new Thread(() -> {
                try {
                    System.out.println("Fetching weather for: " + latitude + ", " + longitude);
                    WeatherData weatherData = weatherService.getWeatherByCoordinates(latitude, longitude);

                    System.out.println("Weather data received: " + weatherData);

                    Platform.runLater(() -> {
                        updateWeatherDisplay(weatherData);
                        searchButton.setDisable(false);
                        searchButton.setText("Search");
                    });
                } catch (Exception e) {
                    System.err.println("Error fetching weather: " + e.getMessage());
                    e.printStackTrace();

                    Platform.runLater(() -> {
                        locationLabel.setText("Error: " + e.getMessage());
                        searchButton.setDisable(false);
                        searchButton.setText("Search");
                    });
                }
            }).start();

        } catch (NumberFormatException e) {
            locationLabel.setText("Invalid coordinate format. Use decimal numbers.");
        }
    }

    private void updateWeatherDisplay(WeatherData weather) {
        if (weather == null) {
            locationLabel.setText("No data available");
            System.err.println("Weather data is null");
            return;
        }

        System.out.println("Updating display with: " + weather);

        DayForecast today = weather.getDay1();

        if (today == null) {
            locationLabel.setText("Day forecast data is not available");
            System.err.println("Day1 forecast is null");
            return;
        }

        // Update location
        String location = weather.getFullLocation();
        System.out.println("Location: " + location);
        locationLabel.setText(location);

        // Update date
        dateLabel.setText(today.getDate() != null ? today.getDate() : "--");

        // Update temperature
        System.out.println("Temperature Max: " + today.getTemperatureMax() +
                ", Min: " + today.getTemperatureMin());
        temperatureLabel.setText(String.format("↑%.1f°C  ↓%.1f°C",
                today.getTemperatureMax(),
                today.getTemperatureMin()));

        // Update description
        String description = today.getText() != null ? today.getText() : "--";
        System.out.println("Description: " + description);
        descriptionLabel.setText(description);

        // Update humidity
        System.out.println("Humidity: " + today.getHumidity());
        humidityLabel.setText("Humidity: " + today.getHumidity() + "%");

        // Update wind
        System.out.println("Wind: " + today.getWind() + " " + today.getWindDirection());
        windLabel.setText(String.format("Wind: %d km/h %s",
                today.getWind(),
                today.getWindDirection() != null ? today.getWindDirection() : "--"));

        // Update sunrise/sunset
        sunriseLabel.setText("Sunrise: " + (today.getSunrise() != null ? today.getSunrise() : "--"));
        sunsetLabel.setText("Sunset: " + (today.getSunset() != null ? today.getSunset() : "--"));

        updateForecast(weather);
    }

    private void updateForecast(WeatherData weather) {
        forecastContainer.getChildren().clear();

        if (weather == null) {
            System.err.println("Weather is null in updateForecast");
            return;
        }

        DayForecast[] days = weather.getAllDays();

        if (days == null) {
            System.err.println("Days array is null");
            return;
        }

        System.out.println("Updating forecast with " + days.length + " days");

        for (int i = 0; i < days.length; i++) {
            if (days[i] == null) {
                System.out.println("Day " + (i + 1) + " is null");
                continue;
            }

            DayForecast day = days[i];
            System.out.println("Day " + (i + 1) + ": " + day);

            Label forecastLabel = new Label(String.format(
                    "Day %d - %s: %.1f°C / %.1f°C - %s",
                    i + 1,
                    day.getDate() != null ? day.getDate() : "--",
                    day.getTemperatureMax(),
                    day.getTemperatureMin(),
                    day.getText() != null ? day.getText() : "--"));

            forecastLabel.getStyleClass().add("forecast-item");
            forecastLabel.setWrapText(true);
            forecastContainer.getChildren().add(forecastLabel);
        }
    }
}