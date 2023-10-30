package org.example.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class OwmWeatherProvider implements WeatherProvider {

    public OwmWeatherProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Weather fetchWeather(Coordinates coordinates) throws IOException {
        String url = getRequestUrl(coordinates);
        String owmWeatherJson = readContentFromUrl(url);
        OwmWeather owmWeather = deserializeOwmWeather(owmWeatherJson);
        WeatherConverter weatherConverter = new WeatherConverter();
        return weatherConverter.convertWeather(owmWeather);
    }

    private static final String WEATHER_URL_TEMPLATE =
            "https://api.openweathermap.org/data/2.5/weather" +
            "?appid=%s&lat=%s&lon=%s";

    private final String apiKey;

    private String getRequestUrl(Coordinates coordinates) {
        return String.format(Locale.ENGLISH, WEATHER_URL_TEMPLATE, apiKey,
                coordinates.getLatitude(), coordinates.getLongitude()
        );
    }

    private static OwmWeather deserializeOwmWeather(String owmWeatherJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(owmWeatherJson, OwmWeather.class);
    }

    private static String readContentFromUrl(String urlString) throws java.io.IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        try(InputStream inputStream = urlConnection.getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
