package org.example.functions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Temperature {
    private double main;
    private double feelsLike;
    private double minimum;
    private double maximum;
}
