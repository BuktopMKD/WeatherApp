package com.denofdevelopers.weatherapp.screen.home;

import com.denofdevelopers.weatherapp.common.presenter.BaseActivityPresenter;
import com.denofdevelopers.weatherapp.model.ForecastResponse;
import com.denofdevelopers.weatherapp.model.WeatherResponse;

public interface MainContract {

    interface View {

        void showMessage(String message);

        void showProgress();

        void hideProgress();

        void displayWeather(WeatherResponse weatherResponse);

        void displayForecast(ForecastResponse forecastResponse);

    }

    interface Presenter extends BaseActivityPresenter {

        void getCityWeather(String city);

        void getWeatherByDeviceLocation(double lat, double lon);

    }
}
