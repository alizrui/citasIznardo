package com.example.citasiznardo.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

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


        final ActionBar actionBar = getSupportActionBar();
        // If the application has an ActionBar the enable the up navigation
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

            /* Esto no haría falta hacerlo normalmente, acceso solo a través de uno de los mecanismos */
            Preference pref = findPreference("database");
            Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(((String) newValue).equals(getResources().getString(R.string.sqlite))){
                        QuotationDatabase.destroyInstance();
                    }
                    return true;
                }
            };
            if (pref != null) {
                pref.setOnPreferenceChangeListener(listener);
            }
        }
    }
}