package com.denofdevelopers.weatherapp.screen.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.denofdevelopers.weatherapp.screen.home.MainActivity;
import com.musala_tech.weatherapp.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        MainActivity.start(this);
        finish();
    }
}
