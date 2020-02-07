package com.denofdevelopers.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

    @SerializedName("coord")
    final public Coordinates coordinates;
    final public List<Weather> weather;
    final public String base;
    final public Main main;
    final public Integer visibility;
    final public Wind wind;
    final public Clouds clouds;
    final public Integer dt;
    final public Sys sys;
    final public Integer timezone;
    final public Integer id;
    final public String name;
    final public Integer cod;

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
