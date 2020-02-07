package com.musala_tech.weatherapp.application;

import com.musala_tech.weatherapp.di.modules.ApiModule;
import com.musala_tech.weatherapp.di.modules.AppModule;
import com.musala_tech.weatherapp.di.modules.DataModule;
import com.musala_tech.weatherapp.screen.home.MainComponent;
import com.musala_tech.weatherapp.screen.home.MainModule;

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

    MainComponent plus(MainModule mainModule);
}
