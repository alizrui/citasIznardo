package com.example.citasiznardo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.citasiznardo.R;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
    }

    public void authorInfo(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        String authorName = "Albert Einstein";
        String link = "https://en.wikipedia.org/wiki/Special:Search?search=";
        intent.setData(Uri.parse(link+authorName));

        startActivity(intent);
    }
}