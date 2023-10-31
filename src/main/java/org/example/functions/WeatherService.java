package org.example.functions;

import java.io.IOException;
import java.util.List;

public interface WeatherService {
    List<LocalWeather> getLocalWeather(String query) throws IOException;
}
