package com.musala_tech.weatherapp.screen.home;

import com.musala_tech.weatherapp.R;
import com.musala_tech.weatherapp.common.Constants;
import com.musala_tech.weatherapp.model.WeatherResponse;
import com.musala_tech.weatherapp.network.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainPresenter implements MainContract.Presenter {

    private ApiService service;
    private final MainActivity activity;
    private final CompositeDisposable request;

    public MainPresenter(MainActivity activity, ApiService apiService) {
        this.request = new CompositeDisposable();
        this.service = apiService;
        this.activity = activity;
    }

    @Override
    public void onStart() {
        //TODO
    }

    @Override
    public void getCityWeather(String city) {
        request.add(service.getCityWeather(city, Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::cityWeatherSuccess, this::getCityWeatherError));
    }


    private void cityWeatherSuccess(WeatherResponse weatherResponse) {
        Timber.d("---> cityWeatherSuccess %s", weatherResponse.name);
        activity.displayWeather(weatherResponse);
    }

    private void getCityWeatherError(Throwable error) {
        Timber.d("---> getCityWeatherError %s", error.getMessage());
        activity.hideProgress();
        activity.showMessage(activity.getString(R.string.city_not_found));
    }

    @Override
    public void getWeatherByDeviceLocation(double lat, double lon) {
        request.add(service.getWeatherByDeviceLocation(lat, lon, Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::weatherByDeviceLocationSuccess, this::weatherByDeviceLocationError));
    }

    private void weatherByDeviceLocationSuccess(WeatherResponse weatherResponse) {
        Timber.d("---> weatherByDeviceLocationSuccess %s", weatherResponse.name);
        activity.displayWeather(weatherResponse);
    }

    private void weatherByDeviceLocationError(Throwable error) {
        Timber.d("---> weatherByDeviceLocationError %s", error.getMessage());
        activity.hideProgress();
        activity.showMessage(activity.getString(R.string.generic_error));
    }

    @Override
    public void onStop() {
        cancelRequest();
    }

    private void cancelRequest() {
        request.clear();
    }
}
