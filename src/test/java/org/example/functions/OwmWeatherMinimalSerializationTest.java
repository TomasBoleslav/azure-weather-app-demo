package org.example.functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class OwmWeatherMinimalSerializationTest {
    @ParameterizedTest
    @MethodSource(MethodArgumentsSource)
    void deserializeOwmWeatherMinimal(String jsonString, OwmWeatherMinimal expectedWeather) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        OwmWeatherMinimal actualWeather = mapper.readValue(jsonString, OwmWeatherMinimal.class);

        assertNotNull(actualWeather);
        assertEquals(expectedWeather, actualWeather);
    }

    private static final String MethodArgumentsSource = "provideArguments";

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideArguments() {
        Coordinates c1 = new Coordinates(44.34, 10.99);
        List<WeatherCondition> w1 = List.of(
                new WeatherCondition(501, "Rain", "moderate rain", "10d")
        );
        return Stream.of(
                Arguments.of(
                        getJsonOwmWeather(c1, w1.get(0)),
                        new OwmWeatherMinimal(c1, w1)
                )
        );
    }

    private static String getJsonOwmWeather(Coordinates coordinates, WeatherCondition condition) {
        return String.format("""
            {
                "coord": {
                    "lat": %s,
                    "lon": %s
                },
                "weather": [
                    {
                        "id": %s,
                        "main": "%s",
                        "description": "%s",
                        "icon": "%s"
                    }
                ]
            }""",
                coordinates.getLatitude(), coordinates.getLongitude(),
                condition.getId(), condition.getMain(),
                condition.getDescription(), condition.getIcon()
        );
    }
}