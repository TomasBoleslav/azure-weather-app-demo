package org.example.functions;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class WeatherHttpFunction {
    @FunctionName("get-current-weather")
    public HttpResponseMessage getCurrentWeather(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String query = request.getQueryParameters().get("q");

        if (query == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("No query").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Weather for " + query).build();
        }
    }
}
