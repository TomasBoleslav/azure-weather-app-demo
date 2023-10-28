package org.example.functions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherCondition {
    private long id;
    private String main;
    private String description;
    private String icon;
}


