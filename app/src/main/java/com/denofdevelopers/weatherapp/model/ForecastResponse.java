package com.denofdevelopers.weatherapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {
    @SerializedName("list")
    public List<ForecastItem> forecastList;
}