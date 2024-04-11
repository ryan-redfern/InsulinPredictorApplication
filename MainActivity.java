package uk.ac.stir.cs.insulinpredictorapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    DBHelper DB;
    List<SettingsModel> settings = new ArrayList<>();
    List<InputModel> data = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // activity started first time
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AddFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_add);
        }
        defaultSettings();
        checkApplicationState();


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddFragment()).commit();
                break;
            case R.id.nav_logbook:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LogbookFragment()).commit();
                break;
            case R.id.nav_getprediction:
                data = checkData(DB);
                if (data.size() > 50) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GetPredictionFragment()).commit();

                } else {
                    Toast.makeText(MainActivity.this, "Can't Access Get Prediction - Not Enough Data", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_previousprediction:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PreviousPredictionsFragment()).commit();

                break;
            case R.id.nav_faq:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FAQFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * When the backpress is selected on the navigation pane
     * the navigation pane is closed
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }
    /**
     * Method to check the settings
     * if settings are found, then output message to the user
     * if settings are not found, then set default settings
     */
    public void defaultSettings() {
        DB = new DBHelper(this);
        settings = checkSettings(DB);
        if (settings.isEmpty()) {
            DB.InsertSettings(7.1, 7, 4, 3.9);
            Toast.makeText(MainActivity.this, "Default Settings Enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Settings already exist", Toast.LENGTH_SHORT).show();


        }
    }
    /**
     * Check application to see whether the application is ready for predictions
     *
     */
    public void checkApplicationState() {
        DB = new DBHelper(this);
        data = checkData(DB);
        if (data.size() > 50) {
            setTitle("PIA - Prediction Ready Mode");

        } else {
            setTitle("PIA - Data Entry Mode");




        }
    }
    /**
     * Method to check settings
     */
    public List<SettingsModel> checkSettings(DBHelper DB){
        return DB.getSettings();
    }
    /**
     * Method
     */
    public List<InputModel> checkData(DBHelper DB){
        return DB.getData();
    }


}