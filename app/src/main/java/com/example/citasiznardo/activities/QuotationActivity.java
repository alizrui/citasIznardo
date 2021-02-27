package com.example.citasiznardo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.citasiznardo.R;

import org.w3c.dom.Text;

import databases.MySqliteOpenHelper;

public class QuotationActivity extends AppCompatActivity {

    private int numCitas = 0;
    private boolean addVisible = false;
    private Menu optionsMenu = null;
    private TextView tvScroll;
    private TextView tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        tvScroll = findViewById(R.id.tvScroll1);
        tvAuthor = findViewById(R.id.tvAuthor);
        if(savedInstanceState != null) {
            numCitas = savedInstanceState.getInt("citas_num");
            tvScroll.setText(savedInstanceState.getString("quote_key"));
            tvAuthor.setText(savedInstanceState.getString("author_key"));
            addVisible = savedInstanceState.getBoolean("visible_add");

        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String name = prefs.getString("username", "");
            tvScroll.setText(String.format((String) tvScroll.getText(), (name == null || name == "") ? "Nameless One" : name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.quotation_menu, menu);
        menu.findItem(R.id.item_add).setVisible(addVisible);
        optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item_add:
                MySqliteOpenHelper.getInstance(this).addQuotation((String) tvScroll.getText(), (String) tvAuthor.getText());
                item.setVisible(false);
                addVisible = false;
                return true;
            case R.id.item_refresh:
                onClicAuthor(item.getActionView());
                optionsMenu.findItem(R.id.item_add).setVisible(addVisible ? true : false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StringFormatInvalid")
    public void onClicAuthor(View view) {
        numCitas++;
        String quote = getResources().getString(R.string.sample_quo);
        String author = getResources().getString(R.string.sample_aut);
        tvScroll.setText(String.format(quote,numCitas));
        tvAuthor.setText(String.format(author,numCitas));

        addVisible = !MySqliteOpenHelper.getInstance(this).isQuotation((String) tvScroll.getText());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("quote_key",(String) tvScroll.getText());
        outState.putString("author_key",(String) tvAuthor.getText());
        outState.putInt("citas_num", numCitas);
        outState.putBoolean("visible_add",addVisible);
    }
}