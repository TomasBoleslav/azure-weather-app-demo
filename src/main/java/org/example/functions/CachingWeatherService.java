package org.example.functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        String normalizedLocationName = normalizeLocationName(locationName);
        System.out.println(normalizedLocationName);
        WeatherCacheItem weatherCacheItem = weatherCacheDao.readItem(normalizedLocationName);
        if (weatherCacheItem != null) {
            return weatherCacheItem.getResponses();
        }
        List<LocalWeather> localWeatherList = new ArrayList<>();
        List<Location> locations = geocoder.findLocations(normalizedLocationName);
        for (Location location : locations) {
            Weather weather = weatherProvider.fetchWeather(location.getCoordinates());
            LocalWeather localWeather = new LocalWeather(location, weather);
            localWeatherList.add(localWeather);
        }
        // TODO: upsert for safety
        WeatherCacheItem newItem = new WeatherCacheItem(
            normalizedLocationName, localWeatherList
        );
        weatherCacheDao.createItem(newItem);
        return localWeatherList;
    }

    private static String normalizeLocationName(String locationName) {
        return locationName
                .trim()
                .replaceAll("\\s", " ")
                .replaceAll(" +", " ")
                .replaceAll(" *, *", ",")
                .toLowerCase(Locale.ROOT);
    }
}
