package com.denofdevelopers.weatherapp.di.modules;

import android.app.Application;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }
}
