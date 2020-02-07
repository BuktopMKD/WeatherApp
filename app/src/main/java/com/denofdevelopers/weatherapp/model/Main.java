package com.denofdevelopers.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class Main {

    final public double temp;
    final public Integer pressure;
    final public Integer humidity;
    @SerializedName("temp_min")
    final public Double tempMin;
    @SerializedName("temp_max")
    final public Double tempMax;

    public Main(double temp, Integer pressure, Integer humidity, Double tempMin, Double tempMax) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }
}
