package com.example.citasiznardo.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Bundle;

import com.example.citasiznardo.R;

import databases.QuotationDatabase;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flSettings, new SettingsFragment())
                .commit();

        /* If the application has an ActionBar the enable the up navigation */
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_settings, rootKey);

            /* Destroys the mysqlite instance and changes the listener */
            Preference pref = findPreference("database");
            Preference.OnPreferenceChangeListener listener = (preference, newValue) -> {
                if(newValue.equals(getResources().getString(R.string.sqlite))){
                    QuotationDatabase.destroyInstance();
                }
                return true;
            };
            if (pref != null) {
                pref.setOnPreferenceChangeListener(listener);
            }
        }
    }
}