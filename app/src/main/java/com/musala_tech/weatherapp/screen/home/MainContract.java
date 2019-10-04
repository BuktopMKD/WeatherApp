package com.musala_tech.weatherapp.screen.home;

import com.musala_tech.weatherapp.common.presenter.BaseActivityPresenter;

public interface MainContract {

    interface View {

        void showMessage(String message);

    }

    interface Presenter extends BaseActivityPresenter {

        void getCityWeather(String city);

        void getWeatherByDeviceLocation(double lat, double lon);

    }
}
