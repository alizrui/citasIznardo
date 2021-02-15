package com.example.citasiznardo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.citasiznardo.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void onClickButton(View view) {
        Intent intent = new Intent();
        int id = view.getId();
        switch (id) {
            case R.id.bGetQuotes:
                intent = new Intent(this, QuotationActivity.class);
                break;
            case R.id.bFavQuotes:
                intent = new Intent(this, FavouriteActivity.class);
                break;
            case R.id.bSettings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.bAbout:
                intent = new Intent(this, AboutActivity.class);
                break;
        }
        startActivity(intent);

    }
}