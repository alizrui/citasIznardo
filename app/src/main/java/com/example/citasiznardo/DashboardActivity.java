package com.example.citasiznardo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.bGetQuotes:
                break;
            case R.id.bFavQuotes:
                break;
            case R.id.bSettings:
                break;
            case R.id.bAbout:
                break;
        }
        //startActivity(intent);

    }
}