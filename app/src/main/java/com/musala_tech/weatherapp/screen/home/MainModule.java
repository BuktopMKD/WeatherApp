package com.musala_tech.weatherapp.screen.home;

import com.musala_tech.weatherapp.di.ActivityScope;
import com.musala_tech.weatherapp.network.ApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    MainActivity mainActivity;

    public MainModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivity provideMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    MainPresenter provideHomePresenter(ApiService apiService) {
        return new MainPresenter(mainActivity, apiService);
    }
}
