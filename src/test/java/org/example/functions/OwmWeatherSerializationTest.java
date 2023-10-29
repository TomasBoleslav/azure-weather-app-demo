package org.example.functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class OwmWeatherSerializationTest {
    @ParameterizedTest
    @MethodSource(MethodArgumentsSource)
    void deserializeOwmWeatherMinimal(String jsonString, OwmWeather expectedWeather) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        OwmWeather actualWeather = mapper.readValue(jsonString, OwmWeather.class);

        assertNotNull(actualWeather);
        assertEquals(expectedWeather, actualWeather);
    }

    private static final String MethodArgumentsSource = "provideArguments";

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideArguments() throws JsonProcessingException {
        Coordinates c1 = new Coordinates(44.34, 10.99);
        List<WeatherCondition> wc1 = List.of(
                new WeatherCondition(501, "Rain", "moderate rain", "10d")
        );
        OwmWeatherMain wm1 = new OwmWeatherMain(
                298.48, 298.74, 297.56, 300.05,
                1015, 64, 1015, 933);
        OwmClouds clouds1 = new OwmClouds(50);
        int visibility1 = 5000;
        Wind wind1 = new Wind(0.62, 349, 1.18);
        OwmPrecipitation rain1 = new OwmPrecipitation(3.16, 0.0);
        OwmPrecipitation snow1 = new OwmPrecipitation(2.5, 4.4);
        Long timestamp1 = 1661870592L;
        return Stream.of(
                Arguments.of(
                        getJsonOwmWeather(c1, wc1, wm1, clouds1, visibility1, wind1, rain1, snow1, timestamp1),
                        new OwmWeather(c1, wc1, wm1, clouds1, visibility1, wind1, rain1, snow1, timestamp1)
                )
        );
    }

    private static String getJsonOwmWeather(
            Coordinates coordinates, List<WeatherCondition> conditions,
            OwmWeatherMain main, OwmClouds clouds, int visibility, Wind wind,
            OwmPrecipitation rain, OwmPrecipitation snow, Long timestamp
            )
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        String coordinatesJson = mapper.writeValueAsString(coordinates);
        String conditionsJson = mapper.writeValueAsString(conditions);
        String mainJson = mapper.writeValueAsString(main);
        String cloudsJson = mapper.writeValueAsString(clouds);
        String windJson = mapper.writeValueAsString(wind);
        String rainJson = mapper.writeValueAsString(rain);
        String snowJson = mapper.writeValueAsString(snow);
        return String.format("""
                        {
                            "coord": %s,
                            "weather": %s,
                            "main": %s,
                            "clouds": %s,
                            "visibility": %s,
                            "wind": %s,
                            "rain": %s,
                            "snow": %s,
                            "dt": %s
                        }
                        """,
                coordinatesJson, conditionsJson, mainJson, cloudsJson,
                visibility, windJson, rainJson, snowJson, timestamp
        );
    }
}