package org.example.functions;

public interface WeatherCacheDao {
    void createItem(WeatherCacheItem item);
    WeatherCacheItem readItem(String id);
    void updateItem(WeatherCacheItem item);
    void deleteItem(String id);
}
