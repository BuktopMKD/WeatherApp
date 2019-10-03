package com.musala_tech.weatherapp.application;

import com.musala_tech.weatherapp.di.modules.ApiModule;
import com.musala_tech.weatherapp.di.modules.AppModule;
import com.musala_tech.weatherapp.di.modules.DataModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                ApiModule.class
        }
)
public interface AppComponent {
    void plus(App app);
}
