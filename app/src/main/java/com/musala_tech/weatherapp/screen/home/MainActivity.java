package com.musala_tech.weatherapp.screen.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.musala_tech.weatherapp.R;
import com.musala_tech.weatherapp.application.App;
import com.musala_tech.weatherapp.common.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Inject
    MainPresenter presenter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //TODO Test Remove it later
        presenter.getCityWeather("Veles");
    }

    @Override
    protected void setupActivityComponent() {
        App.get(this).getAppComponent().plus(new MainModule(this)).inject(this);
    }
}
