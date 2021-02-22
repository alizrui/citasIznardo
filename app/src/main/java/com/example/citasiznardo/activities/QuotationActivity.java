package com.example.citasiznardo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.citasiznardo.R;

public class QuotationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        final TextView tvScroll = findViewById(R.id.tvScroll1);

        String tvText = (String) tvScroll.getText();
        tvScroll.setText(tvText.replace("%1s","Alejandro"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.quotation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item_add:
                return true;
            case R.id.item_refresh:
                onClicAuthor(item.getActionView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onClicAuthor(View view) {
        final TextView tvScroll = findViewById(R.id.tvScroll1);
        final TextView tvAuthor = findViewById(R.id.tvAuthor);

        tvScroll.setText(getResources().getString(R.string.sample_quo));
        tvAuthor.setText(getResources().getString(R.string.sample_aut));
    }
}