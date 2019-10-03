package com.musala_tech.weatherapp.screen.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.musala_tech.weatherapp.R;
import com.musala_tech.weatherapp.screen.home.MainActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        MainActivity.start(this);
        finish();
    }
}
