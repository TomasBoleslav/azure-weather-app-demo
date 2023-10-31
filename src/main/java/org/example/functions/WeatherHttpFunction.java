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
    private static final String QUERY_NAME = "q";

    @FunctionName("get-current-weather")
    public HttpResponseMessage getCurrentWeather(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) throws Exception {
        context.getLogger().info("Java HTTP trigger processed a request.");

        String owmApiKey = System.getenv("OPEN_WEATHER_MAP_API_KEY");
        String cosmosHost = System.getenv("COSMOS_DB_ENDPOINT");
        String cosmosKey = System.getenv("COSMOS_DB_KEY");

        String query = request.getQueryParameters().get(QUERY_NAME);

        if (query == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("No query").build();
        } else {
            WeatherService weatherService = new CachingWeatherService(
                new OwmGeocoder(owmApiKey),
                new OwmWeatherProvider(owmApiKey),
                new CosmosWeatherCacheDao(cosmosHost, cosmosKey)
            );
            List<LocalWeather> localWeatherList = weatherService.getLocalWeather(query);
            ObjectMapper mapper = new ObjectMapper();
            String localWeatherListJson = mapper.writeValueAsString(localWeatherList);
            return request.createResponseBuilder(HttpStatus.OK).body(localWeatherListJson).build();
        }
    }
}
