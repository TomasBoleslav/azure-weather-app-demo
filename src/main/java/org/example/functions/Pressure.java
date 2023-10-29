package org.example.functions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pressure {
    private Integer seaLevel;
    private Integer groundLevel;
}
