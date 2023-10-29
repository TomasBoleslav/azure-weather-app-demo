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
public class OwmPrecipitation {
    @JsonProperty("1h")
    private Double volumeLastHour;

    @JsonProperty("3h")
    private Double volumeLastThreeHours;
}
