package com.musala_tech.weatherapp.model;

public class Weather {

    final private Integer id;
    final private String main;
    final private String description;
    final private String icon;

    public Weather(Integer id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }
}
