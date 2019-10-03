package com.musala_tech.weatherapp.application;

import android.app.Application;

import com.devs.acr.AutoErrorReporter;
import com.musala_tech.weatherapp.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
        initCrashLibrary();
        initTimber();
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


}
