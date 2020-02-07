package com.denofdevelopers.weatherapp.screen.home;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.denofdevelopers.weatherapp.application.App;
import com.denofdevelopers.weatherapp.common.BaseActivity;
import com.denofdevelopers.weatherapp.common.Constants;
import com.denofdevelopers.weatherapp.model.WeatherResponse;
import com.denofdevelopers.weatherapp.util.NetworkUtil;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.musala_tech.weatherapp.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainContract.View {

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
    @BindView(R.id.progress)
    ConstraintLayout progress;

    @Inject
    MainPresenter presenter;

    private double latitude;
    private double longitude;

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_GRANT_PERMISSION = 2;
    private static final int LOCATION_REQUEST_INTERVAL = 10000;
    private static final int LOCATION_REQUEST_FAST_INTERVAL = 5000;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private Location currentLocation;
    private LocationCallback locationCallback;


    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.w_app));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupUi();
        checkLocationPermission();
        createLocationRequest();
        settingsCheck();
        noInternetMessage();
    }

    private void setupUi() {
        handleIntent(getIntent());
        dateAndTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }

    private void checkLocationPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GRANT_PERMISSION);
            return;
        }
        if (locationCallback == null) {
            buildLocationCallback();
        }
        if (currentLocation == null) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_FAST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void settingsCheck() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, locationSettingsResponse -> {
            Timber.d("---> onSuccess: settingsCheck");
            getCurrentLocation();
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                Timber.d("---> onFailure: settingsCheck");
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(MainActivity.this,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    Timber.d(sendEx);
                }
            }
        });
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Timber.d("---> onSuccess: getLastLocation");
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation = location;
                            Timber.d("---> onSuccess:latitude %s", location.getLatitude());
                            Timber.d("---> onSuccess:longitude %s", location.getLongitude());
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            presenter.getWeatherByDeviceLocation(location.getLatitude(), location.getLongitude());
                        } else {
                            Timber.d("---> location is null");
                            buildLocationCallback();
                            hideProgress();
                        }
                    }
                });
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    currentLocation = location;
                    Timber.d("---> onLocationResult: %s ", currentLocation.getAccuracy());
                }
            }

            ;
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("---> onActivityResult: ");
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK)
            getCurrentLocation();
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_CANCELED)
            showMessage("Please enable Location settings...!!!");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GRANT_PERMISSION) {
            getCurrentLocation();
        }
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
            showMessage(getString(R.string.please_add_name));
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
        hideProgress();
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

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.myLocation)
    public void onMyLocationClick() {
        showProgress();
        getCurrentLocation();
    }

    private void noInternetMessage() {
        if (!NetworkUtil.isConnected(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }
}
