package com.musala_tech.weatherapp.screen.home;

import com.musala_tech.weatherapp.common.presenter.BaseActivityPresenter;

public interface MainContract {

    public interface View {

        void showMessage(String message);

    }

    public interface Presenter extends BaseActivityPresenter {

        void getCityWeather();

    }
}
