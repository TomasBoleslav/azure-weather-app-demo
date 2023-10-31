package org.example.functions;

import java.io.IOException;

public interface WeatherCacheDao {
    void createItem(WeatherCacheItem item) throws IOException;
    WeatherCacheItem readItem(String id) throws IOException;
    void updateItem(WeatherCacheItem item) throws IOException;
    void deleteItem(String id) throws IOException;
}
