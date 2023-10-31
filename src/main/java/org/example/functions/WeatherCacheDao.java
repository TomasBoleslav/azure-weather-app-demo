package org.example.functions;

import java.io.IOException;

public interface WeatherCacheDao {
    void createItem(WeatherCacheItem item);
    WeatherCacheItem readItem(String id) throws IOException;
    void updateItem(WeatherCacheItem item);
    void deleteItem(String id);
}
