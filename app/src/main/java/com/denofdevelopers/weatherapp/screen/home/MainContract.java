package com.denofdevelopers.weatherapp.screen.home;

import com.denofdevelopers.weatherapp.common.presenter.BaseActivityPresenter;

public interface MainContract {

    interface View {

        void showMessage(String message);

        void showProgress();

        void hideProgress();

    }

    interface Presenter extends BaseActivityPresenter {

        void getCityWeather(String city);

        void getWeatherByDeviceLocation(double lat, double lon);

    }
}
