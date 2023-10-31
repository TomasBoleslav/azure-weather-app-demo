package org.example.functions;

import com.azure.cosmos.*;
import com.azure.cosmos.implementation.NotFoundException;
import com.azure.cosmos.models.*;

import java.io.IOException;

public class CosmosWeatherCacheDao implements WeatherCacheDao {

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
    public void createItem(WeatherCacheItem item) {
        CosmosItemResponse<WeatherCacheItem> response = container.createItem(item);
        // TODO: check response status
    }

    @Override
    public WeatherCacheItem readItem(String id) throws IOException {
        try {
            CosmosItemResponse<WeatherCacheItem> itemResponse = container.readItem(
                    id, new PartitionKey(id), WeatherCacheItem.class
            );
            // TODO: check response status
            return itemResponse.getItem();
        } catch (NotFoundException exception) {
            return null;
        } catch (CosmosException exception) {
            throw new IOException(exception);
        }
    }

    @Override
    public void updateItem(WeatherCacheItem item) {
        CosmosItemResponse<WeatherCacheItem> response = container.replaceItem(
                item,
                item.getId(),
                new PartitionKey(item.getId()),
                new CosmosItemRequestOptions()
        );
        // TODO: check response status
    }

    @Override
    public void deleteItem(String id) {
        CosmosItemResponse<Object> response = container.deleteItem(
                id, new PartitionKey(id), new CosmosItemRequestOptions()
        );
        // TODO: check response status
    }

    private static final String DATABASE_ID = "weatherdb";
    private static final String CONTAINER_ID = "weatherCache";

    private final CosmosClient client;
    private final CosmosDatabase database;
    private final CosmosContainer container;
}
