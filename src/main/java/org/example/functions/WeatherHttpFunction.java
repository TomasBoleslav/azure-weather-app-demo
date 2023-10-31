package org.example.functions;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

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
            WeatherService weatherService = new CachingWeatherService(
                new OwmGeocoder(apiKey),
                new OwmWeatherProvider(apiKey),
                new CosmosWeatherCacheDao(
                    System.getenv("COSMOS_DB_ENDPOINT"),
                    System.getenv("COSMOS_DB_KEY")
                )
            );
            List<LocalWeather> localWeatherList = weatherService.getLocalWeather(query);
            ObjectMapper mapper = new ObjectMapper();
            String localWeatherListJson = mapper.writeValueAsString(localWeatherList);
            return request.createResponseBuilder(HttpStatus.OK).body(localWeatherListJson).build();

            /*
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
            */
        }
    }
}
