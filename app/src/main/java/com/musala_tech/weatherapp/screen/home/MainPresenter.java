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
                .subscribe(this::CityWeatherSuccess, this::getCityWeatherError));
    }

    private void CityWeatherSuccess(WeatherResponse weatherResponse) {
        Timber.i("Viktor ---> getListErrorGuest %s", weatherResponse.id);
    }

    private void getCityWeatherError(Throwable error) {
        Timber.i("Viktor ---> getListErrorGuest %s", error.getCause());
    }

    @Override
    public void onStop() {
        cancelRequest();
    }

    private void cancelRequest() {
        request.clear();
    }
}
