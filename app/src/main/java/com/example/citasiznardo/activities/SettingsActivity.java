package com.example.citasiznardo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;

import com.example.citasiznardo.R;

public class SettingsActivity extends AppCompatActivity {

    EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        etUserName = findViewById(R.id.etUserName);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("username", "");
        if(name == null){} else {
            etUserName.setText(name);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        String name;
        if((name = etUserName.getText().toString()) == null || name == ""){
            editor.remove("username");
        } else {
            editor.putString("username", name);
        }
        editor.apply();
    }
}