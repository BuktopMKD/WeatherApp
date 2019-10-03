package com.musala_tech.weatherapp.screen.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.musala_tech.weatherapp.R;
import com.musala_tech.weatherapp.application.App;
import com.musala_tech.weatherapp.common.BaseActivity;
import com.musala_tech.weatherapp.common.Constants;
import com.musala_tech.weatherapp.model.WeatherResponse;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    @BindView(R.id.dateAndTime)
    TextView dateAndTime;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.pressure)
    TextView pressure;
    @BindView(R.id.tempMin)
    TextView tempMin;
    @BindView(R.id.tempMax)
    TextView tempMax;
    @BindView(R.id.outside)
    TextView outside;

    @Inject
    MainPresenter presenter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.w_app));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupUi();
    }

    private void setupUi() {
        handleIntent(getIntent());
        dateAndTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchResult(query);
        }
    }

    private void searchResult(String city) {
        Timber.i("---> searchResult %s", city);
        if (!TextUtils.isEmpty(city)) {
            presenter.getCityWeather(city);
        } else {
            Toast.makeText(this, getString(R.string.please_add_name), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    protected void setupActivityComponent() {
        App.get(this).getAppComponent().plus(new MainModule(this)).inject(this);
    }

    public void displayWeather(WeatherResponse weatherResponse) {
        if (weatherResponse != null) {
            if (!TextUtils.isEmpty(weatherResponse.name)) {
                city.setText(weatherResponse.name);
            } else {
                displayNoData(city);
            }

            if (weatherResponse.main != null) {
                if (!TextUtils.isEmpty(String.valueOf(weatherResponse.main.temp))) {
                    temperature.setText(getString(R.string.celsius, String.valueOf(Math.round(((weatherResponse.main.temp - Constants.KELVIN_CELSIUS_DIFFERENCE) * 100) / 100D))));
                } else {
                    displayNoData(temperature);
                }
                if (!TextUtils.isEmpty(String.valueOf(weatherResponse.main.humidity))) {
                    humidity.setText(getString(R.string.hum, String.valueOf(weatherResponse.main.humidity)));
                } else {
                    displayNoData(humidity);
                }
                if (!TextUtils.isEmpty(String.valueOf(weatherResponse.main.pressure))) {
                    pressure.setText(getString(R.string.hPa, String.valueOf(weatherResponse.main.pressure)));
                } else {
                    displayNoData(pressure);
                }
                if (!TextUtils.isEmpty(String.valueOf(weatherResponse.main.tempMin))) {
                    tempMin.setText(getString(R.string.celsius, String.valueOf(Math.round(((weatherResponse.main.tempMin - Constants.KELVIN_CELSIUS_DIFFERENCE) * 100) / 100D))));
                } else {
                    displayNoData(tempMin);
                }
                if (!TextUtils.isEmpty(String.valueOf(weatherResponse.main.tempMax))) {
                    tempMax.setText(getString(R.string.celsius, String.valueOf(Math.round(((weatherResponse.main.tempMax - Constants.KELVIN_CELSIUS_DIFFERENCE) * 100) / 100D))));
                } else {
                    displayNoData(tempMax);
                }
            }

            if (weatherResponse.weather != null && weatherResponse.weather.get(0) != null) {
                if (!TextUtils.isEmpty(weatherResponse.weather.get(0).description)) {
                    if (!TextUtils.isEmpty(weatherResponse.weather.get(0).description)) {
                        outside.setText(weatherResponse.weather.get(0).description);
                    } else {
                        displayNoData(outside);
                    }

                }
            }
        }
    }

    private void displayNoData(TextView textView) {
        textView.setText(R.string.no_data);
    }
}
