package com.musala_tech.weatherapp.application;

import android.app.Application;
import android.content.Context;

import com.devs.acr.AutoErrorReporter;
import com.musala_tech.weatherapp.BuildConfig;
import com.musala_tech.weatherapp.di.modules.AppModule;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class App extends Application {

    private AppComponent appComponent;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
        initCrashLibrary();
        initTimber();
        initAppComponent();
    }

    private void initLeakCanary() {
        LeakCanary.install(this);
    }

    private void initCrashLibrary() {
        if (BuildConfig.DEBUG) {
            AutoErrorReporter.get(this)
                    .setEmailAddresses("buktopmkd@gmail.com")
                    .setEmailSubject("WeatherApp Crash Report")
                    .start();
        }
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
