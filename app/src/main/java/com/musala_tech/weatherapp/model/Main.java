package com.musala_tech.weatherapp.model;

public class Main {

    final private Double temp;
    final private Integer pressure;
    final private Integer humidity;
    final private Double tempMin;
    final private Double tempMax;

    public Main(Double temp, Integer pressure, Integer humidity, Double tempMin, Double tempMax) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }
}
