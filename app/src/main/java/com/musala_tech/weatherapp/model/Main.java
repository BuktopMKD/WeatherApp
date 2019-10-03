package com.musala_tech.weatherapp.model;

public class Main {

    final public Double temp;
    final public Integer pressure;
    final public Integer humidity;
    final public Double tempMin;
    final public Double tempMax;

    public Main(Double temp, Integer pressure, Integer humidity, Double tempMin, Double tempMax) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }
}
