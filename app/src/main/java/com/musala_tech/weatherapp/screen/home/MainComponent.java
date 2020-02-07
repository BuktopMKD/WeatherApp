package com.musala_tech.weatherapp.screen.home;

import com.musala_tech.weatherapp.di.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = MainModule.class
)
public interface MainComponent {
    MainActivity inject(MainActivity mainActivity);
}
