package org.example.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OwmGeocoder implements Geocoder {

    public OwmGeocoder(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<Location> findLocations(String locationName) throws IOException {
        String url = getRequestUrl(locationName);
        String owmLocationsJson = readContentFromUrl(url);
        List<OwmLocation> owmLocations = deserializeOwmLocations(owmLocationsJson);
        return convertLocationsType(owmLocations);
    }

    private static final String GEOCODE_URL_TEMPLATE =
            "https://api.openweathermap.org/geo/1.0/direct" +
                    "?limit=5&appid=%s&q=%s";

    private final String apiKey;

    private String getRequestUrl(String locationName) {
        return String.format(GEOCODE_URL_TEMPLATE, apiKey, locationName);
    }

    private static List<OwmLocation> deserializeOwmLocations(String owmLocationsJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OwmLocation[] owmLocations = mapper.readValue(owmLocationsJson, OwmLocation[].class);
        return Arrays.asList(owmLocations);
    }

    private static List<Location> convertLocationsType(List<OwmLocation> owmLocations) {
        List<Location> locations = new ArrayList<>(owmLocations.size());
        for (OwmLocation owmLocation : owmLocations) {
            Location location = convertLocationType(owmLocation);
            locations.add(location);
        }
        return locations;
    }

    private static Location convertLocationType(OwmLocation owmLocation) {
        return new Location(
            owmLocation.getName(),
            owmLocation.getState(),
            owmLocation.getCountry(),
            new Coordinates(
                    owmLocation.getLatitude(),
                    owmLocation.getLongitude()
            )
        );
    }

    private static String readContentFromUrl(String urlString) throws java.io.IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        try(InputStream inputStream = urlConnection.getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
