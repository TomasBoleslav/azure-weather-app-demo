package org.example.functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesSerializationTest {
    @ParameterizedTest
    @MethodSource(MethodArgumentsSource)
    void serializeCoordinates(double latitude, double longitude) throws JsonProcessingException {
        Coordinates coordinates = new Coordinates(latitude, longitude);
        String expectedJson = getJsonCoordinates(latitude, longitude);
        ObjectMapper mapper = new ObjectMapper();

        String actualJson = mapper.writeValueAsString(coordinates);

        assertEquals(expectedJson, actualJson);
    }

    @ParameterizedTest
    @MethodSource(MethodArgumentsSource)
    void deserializeCoordinates(double latitude, double longitude) throws JsonProcessingException {
        String jsonString = getJsonCoordinates(latitude, longitude);
        ObjectMapper mapper = new ObjectMapper();

        Coordinates actualCoordinates = mapper.readValue(jsonString, Coordinates.class);

        assertTrue(actualCoordinates != null
                && actualCoordinates.getLatitude() == latitude
                && actualCoordinates.getLongitude() == longitude
        );
    }

    private static final String MethodArgumentsSource = "provideCoordinates";

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideCoordinates() {
        return Stream.of(
                Arguments.of(0.0, 0.0),
                Arguments.of(1.6, 12.8),
                Arguments.of(-20.8, -1.0)
        );
    }

    private static String getJsonCoordinates(double latitude, double longitude) {
        return String.format("{\"lat\":%s,\"lon\":%s}", latitude, longitude);
    }
}
