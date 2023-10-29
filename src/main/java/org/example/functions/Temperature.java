package org.example.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Temperature {
    private Double main;
    private Double feelsLike;
    @JsonProperty("min")
    private Double minimum;
    @JsonProperty("max")
    private Double maximum;
}
