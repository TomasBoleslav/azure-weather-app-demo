package org.example.functions;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;

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
        // TODO: Normalize query - here or in service?
        CosmosItemResponse<WeatherCacheItem> response = container.createItem(item);
        // TODO: check response status
    }

    @Override
    public WeatherCacheItem readItem(String id) {
        CosmosItemResponse<WeatherCacheItem> itemResponse = container.readItem(
                id, new PartitionKey(id), WeatherCacheItem.class
        );
        // TODO: check response status
        return itemResponse.getItem();
        /* TODO: decide between point read and query
        List<SqlParameter> queryParameters= List.of(
                new SqlParameter("@id", id)
        );
        SqlQuerySpec querySpec = new SqlQuerySpec(
                "SELECT * FROM c WHERE (c.id = @id)",
                queryParameters
        );
        CosmosPagedIterable<WeatherCacheItem> itemsPagedIterable = cosmosContainer.queryItems(
                querySpec, new CosmosQueryRequestOptions(), WeatherCacheItem.class
        );
        for (WeatherCacheItem item : itemsPagedIterable) {
            return item;
        }
        return null;
        */
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
