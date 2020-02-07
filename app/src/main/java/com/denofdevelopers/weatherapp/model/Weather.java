package com.denofdevelopers.weatherapp.model;

public class Weather {

    final public Integer id;
    final public String main;
    final public String description;
    final public String icon;

    public Weather(Integer id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }
}
