package com.denofdevelopers.weatherapp.screen.home;

import com.denofdevelopers.weatherapp.di.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = MainModule.class
)
public interface MainComponent {
    MainActivity inject(MainActivity mainActivity);
}
