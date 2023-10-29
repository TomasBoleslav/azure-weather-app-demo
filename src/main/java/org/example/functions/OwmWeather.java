package org.example.functions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwmWeather {
    @JsonProperty("coord")
    private Coordinates coordinates;

    @JsonProperty("weather")
    private List<WeatherCondition> weatherConditions;

    private OwmWeatherMain main;

    private OwmClouds clouds;

    private Integer visibility;

    private Wind wind;

    private Precipitation rain;

    private Precipitation snow;

    @JsonProperty("dt")
    private Long timestamp;
}
