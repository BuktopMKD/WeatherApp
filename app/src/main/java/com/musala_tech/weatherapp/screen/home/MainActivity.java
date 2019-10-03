package com.musala_tech.weatherapp.screen.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.musala_tech.weatherapp.R;
import com.musala_tech.weatherapp.application.App;
import com.musala_tech.weatherapp.common.BaseActivity;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

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
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    protected void setupActivityComponent() {
        App.get(this).getAppComponent().plus(new MainModule(this)).inject(this);
    }
}
