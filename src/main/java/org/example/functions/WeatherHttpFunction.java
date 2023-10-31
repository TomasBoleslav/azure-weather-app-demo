package org.example.functions;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.azure.cosmos.implementation.ConflictException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.apache.commons.io.IOUtils;

/*
 * TODO:
 *  1. Refactoring - weather api
 *  2. Persistence layer - store cache in Cosmos DB
 */

/**
 * Azure Functions with HTTP Trigger.
 */
@SuppressWarnings("unused")
public class WeatherHttpFunction {
    @FunctionName("get-current-weather")
    public HttpResponseMessage getCurrentWeather(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) throws Exception {
        context.getLogger().info("Java HTTP trigger processed a request.");

        String apiKey = System.getenv("OPEN_WEATHER_MAP_API_KEY");

        // Parse query parameter
        String query = request.getQueryParameters().get("q");

        if (query == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("No query").build();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Geocoder geocoder = new OwmGeocoder(apiKey);
            List<Location> locations = geocoder.findLocations(query);
            WeatherProvider weatherProvider = new OwmWeatherProvider(apiKey);
            List<LocalWeather> localWeatherResults = new ArrayList<>();
            for (Location location : locations) {
                Weather weather = weatherProvider.fetchWeather(location.getCoordinates());
                localWeatherResults.add(new LocalWeather(location, weather));
            }
            {
                CosmosWeatherCacheDao weatherCacheDao = new CosmosWeatherCacheDao(
                        System.getenv("COSMOS_DB_ENDPOINT"),
                        System.getenv("COSMOS_DB_KEY")
                );
                try {
                    weatherCacheDao.createItem(
                            new WeatherCacheItem(
                                    query.toLowerCase(), localWeatherResults
                            )
                    );
                } catch (ConflictException exception) {
                    WeatherCacheItem item = weatherCacheDao.readItem(query.toLowerCase());
                    String localWeatherResultsJson = mapper.writeValueAsString(item);
                    return request.createResponseBuilder(HttpStatus.OK).body(localWeatherResultsJson).build();
                }
            }
            String localWeatherResultsJson = mapper.writeValueAsString(localWeatherResults);
            return request.createResponseBuilder(HttpStatus.OK).body(localWeatherResultsJson).build();
        }
    }

    private static String readContentFromUrl(String urlString) throws java.io.IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        try(InputStream inputStream = urlConnection.getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
