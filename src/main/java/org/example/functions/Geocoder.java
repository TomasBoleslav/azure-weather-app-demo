package org.example.functions;

import java.io.IOException;
import java.util.List;

public interface Geocoder {
    List<Location> findLocations(String locationName) throws IOException;
}
