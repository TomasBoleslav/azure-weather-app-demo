package org.example.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lon")
    private double longitude;
}
