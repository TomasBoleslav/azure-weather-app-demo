package org.example.functions;

import com.azure.cosmos.*;
import com.azure.cosmos.implementation.NotFoundException;
import com.azure.cosmos.models.*;

import java.io.IOException;

public class CosmosWeatherCacheDao implements WeatherCacheDao {
    private static final String DATABASE_ID = "weatherdb";
    private static final String CONTAINER_ID = "weatherCache";

    private final CosmosClient client;
    private final CosmosDatabase database;
    private final CosmosContainer container;

    public CosmosWeatherCacheDao(String host, String masterKey) {
        this.client = new CosmosClientBuilder()
                .endpoint(host)
                .key(masterKey)
                .consistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildClient();
        this.database = client.getDatabase(DATABASE_ID);
        this.container = database.getContainer(CONTAINER_ID);
    }

    @Override
    public void createItem(WeatherCacheItem item) throws IOException {
        try {
            CosmosItemResponse<WeatherCacheItem> response = container.createItem(item);
            throwOnFailedRequest(response.getStatusCode());
        } catch (CosmosException exception) {
            throw new IOException(exception);
        }
    }

    @Override
    public WeatherCacheItem readItem(String id) throws IOException {
        try {
            CosmosItemResponse<WeatherCacheItem> response = container.readItem(
                    id, new PartitionKey(id), WeatherCacheItem.class
            );
            throwOnFailedRequest(response.getStatusCode());
            return response.getItem();
        } catch (NotFoundException exception) {
            return null;
        } catch (CosmosException exception) {
            throw new IOException(exception);
        }
    }

    @Override
    public void updateItem(WeatherCacheItem item) throws IOException {
        try {
            CosmosItemResponse<WeatherCacheItem> response = container.replaceItem(
                    item,
                    item.getId(),
                    new PartitionKey(item.getId()),
                    new CosmosItemRequestOptions()
            );
            throwOnFailedRequest(response.getStatusCode());
        } catch (CosmosException exception) {
            throw new IOException(exception);
        }
    }

    @Override
    public void deleteItem(String id) throws IOException {
        try {
            CosmosItemResponse<Object> response = container.deleteItem(
                    id, new PartitionKey(id), new CosmosItemRequestOptions()
            );
            throwOnFailedRequest(response.getStatusCode());
        } catch (CosmosException exception) {
            throw new IOException(exception);
        }
    }

    private void throwOnFailedRequest(int statusCode) throws IOException {
        if (statusCode < 200 || statusCode >= 300) {
            String message = "Request failed with code %s".formatted(statusCode);
            throw new IOException(message);
        }
    }
}
