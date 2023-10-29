package org.example.functions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Weather {
    @JsonProperty("conditions")
    private List<WeatherCondition> weatherConditions;
    private Temperature temperature;
    private Pressure pressure;
    private Integer humidity;
    private Integer clouds;
    private Wind wind;
    private Precipitation rain;
    private Precipitation snow;
    private Integer visibility;
    @JsonProperty("dt")
    private Long timestamp;
}
