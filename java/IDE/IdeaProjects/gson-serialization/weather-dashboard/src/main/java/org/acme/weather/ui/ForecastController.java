package org.acme.weather.ui;

import org.acme.weather.model.Forecast;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ForecastController {

    @FXML
    private VBox forecastContainer;

    public void displayForecast(Forecast forecast) {
        forecastContainer.getChildren().clear();

        if (forecast == null || forecast.getForecastItems() == null) {
            Label noDataLabel = new Label("No forecast data available");
            forecastContainer.getChildren().add(noDataLabel);
            return;
        }

        // Display forecast items with null safety
        for (int i = 0; i < Math.min(5, forecast.getForecastItems().size()); i++) {
            Forecast.ForecastItem item = forecast.getForecastItems().get(i);

            if (item == null || item.getMain() == null) {
                continue;
            }

            String dateText = item.getDateText() != null ? item.getDateText() : "--";
            String temp = item.getMain().getTemperature() > 0 ?
                    String.valueOf(item.getMain().getTemperature()) : "--";
            String description = (item.getWeather() != null && item.getWeather().length > 0) ?
                    item.getWeather()[0].getDescription() : "--";

            Label label = new Label(String.format("%s: %s°C - %s",
                    dateText, temp, description));

            label.getStyleClass().add("forecast-item");
            label.setWrapText(true);
            forecastContainer.getChildren().add(label);
        }
    }
}