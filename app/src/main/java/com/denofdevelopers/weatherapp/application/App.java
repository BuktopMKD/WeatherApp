package com.denofdevelopers.weatherapp.application;

import android.app.Application;
import android.content.Context;
import com.denofdevelopers.weatherapp.BuildConfig;
import com.denofdevelopers.weatherapp.application.DaggerAppComponent;
import com.denofdevelopers.weatherapp.di.modules.AppModule;

import timber.log.Timber;

public class App extends Application {

    private AppComponent appComponent;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initTimber();
        initAppComponent();
    }

    private void initTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.plus(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
