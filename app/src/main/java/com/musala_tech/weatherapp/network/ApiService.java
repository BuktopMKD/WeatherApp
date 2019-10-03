package com.musala_tech.weatherapp.network;

import com.musala_tech.weatherapp.model.WeatherResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("weather")
    Single<WeatherResponse> getCityWeather(@Query("q") String city, @Query("APPID") String API_KEY);
}
