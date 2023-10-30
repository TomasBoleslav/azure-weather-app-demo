package org.example.functions;

import java.io.IOException;

public interface WeatherProvider {
    Weather fetchWeather(Coordinates coordinates) throws IOException;
}
