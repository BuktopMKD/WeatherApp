package com.denofdevelopers.weatherapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastItem {
    @SerializedName("dt")
    public long dateTime;
    
    @SerializedName("main")
    public Main main;
    
    @SerializedName("weather")
    public List<Weather> weather;
    
    @SerializedName("dt_txt")
    public String dateTimeText;
}