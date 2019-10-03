package com.musala_tech.weatherapp.model;

public class Sys {

    final private Integer type;
    final private Integer id;
    final private Double message;
    final private String country;
    final private Integer sunrise;
    final private Integer sunset;

    public Sys(Integer type, Integer id, Double message, String country, Integer sunrise, Integer sunset) {
        this.type = type;
        this.id = id;
        this.message = message;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }
}
