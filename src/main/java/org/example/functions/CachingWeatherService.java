package org.example.functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CachingWeatherService implements WeatherService {
    private final Geocoder geocoder;
    private final WeatherProvider weatherProvider;
    private final WeatherCacheDao weatherCacheDao;

    public CachingWeatherService(Geocoder geocoder, WeatherProvider weatherProvider, WeatherCacheDao weatherCacheDao) {
        this.geocoder = geocoder;
        this.weatherProvider = weatherProvider;
        this.weatherCacheDao = weatherCacheDao;
    }

    @Override
    public List<LocalWeather> getLocalWeather(String locationName) throws IOException {
        // TODO:
        //  1. Normalize locationName
        //  2. Try to get an item from database
        //  3a. Item exists -> return item (in the future also check for timestamps)
        //  3b. Item does not exist -> fetch data using geocoder and weatherProvider, store it in a database and return
        //      the new item (maybe use upsert to avoid conflicts - writing an item with the same id
        //      from multiple instances of http function)
        WeatherCacheItem weatherCacheItem = weatherCacheDao.readItem(locationName);
        if (weatherCacheItem != null) {
            return weatherCacheItem.getResponses();
        }
        List<LocalWeather> localWeatherResults = new ArrayList<>();
        List<Location> locations = geocoder.findLocations(locationName);
        for (Location location : locations) {
            Weather weather = weatherProvider.fetchWeather(location.getCoordinates());
            LocalWeather localWeather = new LocalWeather(location, weather);
            localWeatherResults.add(localWeather);
        }
        // TODO: upsert for safety
        weatherCacheDao.createItem(
            new WeatherCacheItem(
                    locationName.toLowerCase(), localWeatherResults
            )
        );
        return localWeatherResults;
    }
}
