package org.example.functions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Precipitation {
    private Double volumeLastHour;
    private Double volumeLastThreeHours;
}

