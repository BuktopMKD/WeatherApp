package com.musala_tech.weatherapp.model;

public class Sys {

    final public Integer type;
    final public Integer id;
    final public Double message;
    final public String country;
    final public Integer sunrise;
    final public Integer sunset;

    public Sys(Integer type, Integer id, Double message, String country, Integer sunrise, Integer sunset) {
        this.type = type;
        this.id = id;
        this.message = message;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }
}
