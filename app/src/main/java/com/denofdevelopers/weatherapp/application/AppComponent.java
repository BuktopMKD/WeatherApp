package com.denofdevelopers.weatherapp.application;

import com.denofdevelopers.weatherapp.screen.home.MainComponent;
import com.denofdevelopers.weatherapp.di.modules.ApiModule;
import com.denofdevelopers.weatherapp.di.modules.AppModule;
import com.denofdevelopers.weatherapp.di.modules.DataModule;
import com.denofdevelopers.weatherapp.screen.home.MainModule;

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
