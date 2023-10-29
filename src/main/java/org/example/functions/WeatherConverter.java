package org.example.functions;

import java.util.List;

public class WeatherConverter {
    public Weather convertWeather(OwmWeather owmWeather) {
        return new Weather(
                convertWeatherConditions(owmWeather),
                convertTemperature(owmWeather),
                convertPressure(owmWeather),
                convertHumidity(owmWeather),
                convertClouds(owmWeather),
                convertWind(owmWeather),
                convertRain(owmWeather),
                convertSnow(owmWeather),
                convertVisibility(owmWeather),
                convertTimestamp(owmWeather)
        );
    }

    private List<WeatherCondition> convertWeatherConditions(OwmWeather owmWeather) {
        return owmWeather.getWeatherConditions();
    }

    private Temperature convertTemperature(OwmWeather owmWeather) {
        return new Temperature(
                owmWeather.getMain().getTemp(),
                owmWeather.getMain().getFeelsLike(),
                owmWeather.getMain().getTempMin(),
                owmWeather.getMain().getTempMax()
        );
    }

    private Pressure convertPressure(OwmWeather owmWeather) {
        return new Pressure(
                owmWeather.getMain().getPressure(),
                owmWeather.getMain().getGrndLevel()
        );
    }

    private Integer convertHumidity(OwmWeather owmWeather) {
        return owmWeather.getMain().getHumidity();
    }

    private Integer convertClouds(OwmWeather owmWeather) {
        OwmClouds clouds = owmWeather.getClouds();
        if (clouds == null) {
            return null;
        }
        return owmWeather.getClouds().getAll();
    }

    private Wind convertWind(OwmWeather owmWeather) {
        return owmWeather.getWind();
    }

    private Precipitation convertRain(OwmWeather owmWeather) {
        return convertPrecipitation(owmWeather.getRain());
    }

    private Precipitation convertSnow(OwmWeather owmWeather) {
        return convertPrecipitation(owmWeather.getSnow());
    }

    private Integer convertVisibility(OwmWeather owmWeather) {
        return owmWeather.getVisibility();
    }

    private Long convertTimestamp(OwmWeather owmWeather) {
        Long timestamp = owmWeather.getTimestamp();
        if (timestamp == null) {
            return System.currentTimeMillis() / 1000L;
        }
        return timestamp;
    }

    private Precipitation convertPrecipitation(OwmPrecipitation owmPrecipitation) {
        if (owmPrecipitation == null) {
            return null;
        }
        return new Precipitation(
                owmPrecipitation.getVolumeLastHour(),
                owmPrecipitation.getVolumeLastThreeHours()
        );
    }
}
