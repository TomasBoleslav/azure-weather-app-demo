package org.example.functions;

public class GeolocationConverter {
    public Location convertLocation(OwmLocation owmLocation) {
        return new Location(
                owmLocation.getName(),
                owmLocation.getState(),
                owmLocation.getCountry(),
                new Coordinates(
                        owmLocation.getLatitude(),
                        owmLocation.getLongitude()
                )
        );
    }
}
