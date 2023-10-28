package org.example.functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CoordinatesSerializationTest {
    @ParameterizedTest
    @MethodSource(MethodArgumentsSource)
    void serializeCoordinates(double latitude, double longitude) throws JsonProcessingException {
        Coordinates coordinates = new Coordinates(latitude, longitude);
        String expectedJson = String.format(
                "{\"latitude\":%s,\"longitude\":%s}", latitude, longitude
        );
        ObjectMapper mapper = new ObjectMapper();

        String actualJson = mapper.writeValueAsString(coordinates);

        assertTrue(actualJson != null && actualJson.equals(expectedJson));
    }

    @ParameterizedTest
    @MethodSource(MethodArgumentsSource)
    void deserializeCoordinates(double latitude, double longitude) throws JsonProcessingException {
        String jsonString = String.format(
                "{\"latitude\":%s,\"longitude\":%s}",
                latitude,
                longitude
        );
        ObjectMapper mapper = new ObjectMapper();

        Coordinates actualCoordinates = mapper.readValue(jsonString, Coordinates.class);

        assertTrue(actualCoordinates != null
                && actualCoordinates.getLatitude() == latitude
                && actualCoordinates.getLongitude() == longitude
        );
    }

    private static final String MethodArgumentsSource = "provideCoordinates";

    private static Stream<Arguments> provideCoordinates() {
        return Stream.of(
                Arguments.of(0.0, 0.0),
                Arguments.of(1.6, 12.8),
                Arguments.of(-20.8, -1.0)
        );
    }
}
