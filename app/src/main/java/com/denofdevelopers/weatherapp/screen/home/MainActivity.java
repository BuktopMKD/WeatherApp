package com.denofdevelopers.weatherapp.screen.home;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.denofdevelopers.weatherapp.application.App;
import com.denofdevelopers.weatherapp.common.BaseActivity;
import com.denofdevelopers.weatherapp.common.Constants;
import com.denofdevelopers.weatherapp.model.ForecastItem;
import com.denofdevelopers.weatherapp.model.ForecastResponse;
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
import com.denofdevelopers.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.mainRoot)
    ConstraintLayout mainRoot;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.currentTime)
    TextView currentTime;
    @BindView(R.id.currentDate)
    TextView currentDate;
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
    @BindView(R.id.forecastContainer)
    LinearLayout forecastContainer;
    @BindView(R.id.extendedForecastContainer)
    LinearLayout extendedForecastContainer;
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

    private final Handler timeHandler = new Handler(Looper.getMainLooper());
    private final Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            timeHandler.postDelayed(this, 1000 * 60); // Update every minute
        }
    };


    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        applyWindowInsets();
        setupUi();
        checkLocationPermission();
        createLocationRequest();
        settingsCheck();
        noInternetMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeHandler.post(timeRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeHandler.removeCallbacks(timeRunnable);
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(view.getPaddingLeft(), systemBars.top, view.getPaddingRight(), view.getPaddingBottom());
            return windowInsets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(mainRoot, (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), systemBars.bottom);
            return windowInsets;
        });
    }

    private void setupUi() {
        handleIntent(getIntent());
        updateTime();
    }

    private void updateTime() {
        Date now = new Date();
        currentTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now));
        currentDate.setText(new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(now));
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
        searchView.setMaxWidth(Integer.MAX_VALUE);
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

    public void displayForecast(ForecastResponse forecastResponse) {
        forecastContainer.removeAllViews();
        extendedForecastContainer.removeAllViews();
        if (forecastResponse != null && forecastResponse.forecastList != null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            
            // 3-Day Forecast (Indices 8, 16, 24)
            int[] mainIndices = {8, 16, 24};
            for (int index : mainIndices) {
                if (index < forecastResponse.forecastList.size()) {
                    ForecastItem item = forecastResponse.forecastList.get(index);
                    View view = inflater.inflate(R.layout.item_forecast, forecastContainer, false);
                    populateForecastView(view, item);
                    forecastContainer.addView(view);
                }
            }

            // Extended Forecast (All other distinct days)
            // The free API gives 5 days / 3 hours (40 items). 
            // We'll show one item per day for the full range available.
            for (int i = 0; i < forecastResponse.forecastList.size(); i += 8) {
                ForecastItem item = forecastResponse.forecastList.get(i);
                View view = inflater.inflate(R.layout.item_forecast_small, extendedForecastContainer, false);
                populateForecastView(view, item);
                extendedForecastContainer.addView(view);
            }
        }
    }

    private void populateForecastView(View view, ForecastItem item) {
        TextView dayText = view.findViewById(R.id.forecastDay);
        TextView tempText = view.findViewById(R.id.forecastTemp);
        TextView descText = view.findViewById(R.id.forecastDesc);
        
        Date date = new Date(item.dateTime * 1000);
        dayText.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(date));
        
        tempText.setText(getString(R.string.celsius, String.valueOf(Math.round(((item.main.temp - Constants.KELVIN_CELSIUS_DIFFERENCE) * 100) / 100D))));
        
        if (descText != null && item.weather != null && !item.weather.isEmpty()) {
            descText.setText(item.weather.get(0).description);
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
