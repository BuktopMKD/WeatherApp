package com.musala_tech.weatherapp.screen.home;

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

    }

    @Override
    public void getCityWeather(String city) {
        request.add(service.getCityWeather(city, Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::cityWeatherSuccess, this::getCityWeatherError));
    }

    private void cityWeatherSuccess(WeatherResponse weatherResponse) {
        Timber.i("---> cityWeatherSuccess %s", weatherResponse.name);
        activity.displayWeather(weatherResponse);
        // TODO implement progress view (spinner)
    }

    private void getCityWeatherError(Throwable error) {
        Timber.i("---> getCityWeatherError %s", error.getCause());
    }

    @Override
    public void onStop() {
        cancelRequest();
    }

    private void cancelRequest() {
        request.clear();
    }
}
