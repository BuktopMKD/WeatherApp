package com.denofdevelopers.weatherapp.screen.home;

import com.denofdevelopers.weatherapp.di.ActivityScope;
import com.denofdevelopers.weatherapp.network.ApiService;

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
