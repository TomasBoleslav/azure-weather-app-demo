package org.example.functions;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.apache.commons.io.IOUtils;

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
            String url = "https://api.openweathermap.org/data/2.5/weather?appid=" + apiKey + "&q=" + query;
            String responseBody = readContentFromUrl(url);
            System.out.println(responseBody);
            ObjectMapper mapper = new ObjectMapper();
            OwmWeather owmWeather = mapper.readValue(responseBody, OwmWeather.class);
            WeatherConverter converter = new WeatherConverter();
            Weather weather = converter.convertWeather(owmWeather);
            String weatherJson = mapper.writeValueAsString(weather);
            return request.createResponseBuilder(HttpStatus.OK).body(weatherJson).build();

            //return request.createResponseBuilder(HttpStatus.OK).body(weather).build();
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
