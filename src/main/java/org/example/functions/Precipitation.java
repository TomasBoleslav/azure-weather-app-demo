package org.example.functions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Precipitation {
    @JsonProperty("oneHour")
    private Double volumeLastHour;

    @JsonProperty("threeHours")
    private Double volumeLastThreeHours;
}
