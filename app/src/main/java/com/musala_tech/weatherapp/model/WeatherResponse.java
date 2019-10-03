package com.musala_tech.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

    @SerializedName("coord")
    final private Coordinates coordinates;
    final private List<Weather> weather;
    final private String base;
    final private Main main;
    final private Integer visibility;
    final private Wind wind;
    final private Clouds clouds;
    final private Integer dt;
    final private Sys sys;
    final private Integer timezone;
    final private Integer id;
    final private String name;
    final private Integer cod;

    public WeatherResponse(Coordinates coordinates, List<Weather> weatherList, String base, Main main
            , Integer visibility, Wind wind, Clouds clouds, Integer dt, Sys sys, Integer timezone
            , Integer id, String name, Integer cod) {
        this.coordinates = coordinates;
        this.weather = weatherList;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.timezone = timezone;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }
}
