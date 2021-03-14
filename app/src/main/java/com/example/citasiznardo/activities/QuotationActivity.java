package com.example.citasiznardo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.citasiznardo.R;

import adapter.Quotation;
import databases.MySqliteOpenHelper;
import databases.QuotationDatabase;
import threads.WebServiceThread;

public class QuotationActivity extends AppCompatActivity {

    private boolean addVisible = false;
    private MenuItem addItem = null;
    private MenuItem refreshItem = null;

    private TextView tvScroll;
    private TextView tvAuthor;
    private ProgressBar progressBar;

    private String language;
    private String http_method;
    private Boolean databaseMode = true; // sqlite = false, room = true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        /*
        * Keep a reference to:
        *  the TextView displaying the quote
        *  the TextView displaying the quote's author
        *  the progressBar showing the progress of getting a quotes
        */
        tvScroll = findViewById(R.id.tvScroll1);
        tvAuthor = findViewById(R.id.tvAuthor);
        progressBar = findViewById(R.id.progressBar);

        /*
        * Gets the SharedPreferences to get:
        *  database mode: room or mysqlite
        *  language of the requested quotations
        *  the http method for getting the quotations
        *  the name of the user
        */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        databaseMode = !prefs.getString("database", "").equals(getResources().getString(R.string.sqlite));
        language = prefs.getString("language","");
        http_method = prefs.getString("http","");
        String name = prefs.getString("username", "");

        /* Recovers the saved state */
        if(savedInstanceState != null) {
            tvScroll.setText(savedInstanceState.getString("quote_key"));
            tvAuthor.setText(savedInstanceState.getString("author_key"));
            addVisible = savedInstanceState.getBoolean("visible_add");
        } else {
            tvScroll.setText(String.format((String) tvScroll.getText(), (name == null || name.equals("")) ? "Nameless One" : name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.quotation_menu, menu);

        /* Keeps a reference of the add item and the refresh item */
        addItem = menu.findItem(R.id.item_add);
        refreshItem = menu.findItem(R.id.item_refresh);
        addItem.setVisible(addVisible);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item_add:
                if(databaseMode){
                    QuotationDatabase quotationDatabase = QuotationDatabase.getInstance(this);
                    new Thread(() -> quotationDatabase.quotesDao().addQuote(new Quotation((String) tvScroll.getText(), (String) tvAuthor.getText()))).start();
                } else {
                    MySqliteOpenHelper mySqliteOpenHelper = MySqliteOpenHelper.getInstance(this);
                    new Thread(() -> mySqliteOpenHelper.addQuote((String) tvScroll.getText(), (String) tvAuthor.getText())).start();
                }
                item.setVisible(false);
                return true;
            case R.id.item_refresh:
                WebServiceThread webServiceThread = new WebServiceThread(this,language, http_method);
                if(isConnected()) webServiceThread.start();
                //onClicAuthor(item.getActionView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Saves the quote, the author and the visibility of the add item */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("quote_key",(String) tvScroll.getText());
        outState.putString("author_key",(String) tvAuthor.getText());
        outState.putBoolean("visible_add",addVisible);
    }

    /* Shows the quote received */
    private void setQuote(Quotation quote){
        tvScroll.setText(quote.getQuoteText());
        tvAuthor.setText(quote.getQuoteAuthor());

        /* Checks if the received quotation is already on the favourite quotations */
        if(databaseMode){
            QuotationDatabase quotationDatabase = QuotationDatabase.getInstance(this);
            new Thread(new Runnable() {
                final Handler handler = new Handler(Looper.getMainLooper());
                @Override
                public void run() {
                    Quotation aux = quotationDatabase.quotesDao().findQuote((String) tvScroll.getText());
                    addVisible = (aux == null);
                    handler.post(() -> addItem.setVisible(addVisible));
                }
            }).start();
        } else {
            MySqliteOpenHelper mySqliteOpenHelper = MySqliteOpenHelper.getInstance(this);
            new Thread(new Runnable() {
                final Handler handler = new Handler(Looper.getMainLooper());
                @Override
                public void run() {
                    addVisible = !mySqliteOpenHelper.isQuote((String) tvScroll.getText());
                    handler.post(() -> addItem.setVisible(addVisible));
                }
            }).start();
        }
    }

    /* Hides ActionBar, TextViews and shows progress bar */
    public void showProgress(){
        if (addItem != null) addItem.setVisible(false);
        if (refreshItem != null) refreshItem.setVisible(false);
        tvScroll.setVisibility(View.INVISIBLE);
        tvAuthor.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    /* Shows received quote and hide progress bar */
    public void showQuote(Quotation quote){
        tvScroll.setVisibility(View.VISIBLE);
        tvAuthor.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        setQuote(quote);
        refreshItem.setVisible(true);
    }

    /* Checks if the connexion is on */
    public boolean isConnected(){
        boolean res = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < 23) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            res = ((info != null) && (info.isConnected()));
        } else {
            final Network activeNetwork = manager.getActiveNetwork();
            if (activeNetwork != null) {
                final NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(activeNetwork);
                res = networkCapabilities != null &&
                        (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            }
        }
        return res;
    }

    /* Sets a quotation sample */
    @SuppressLint("StringFormatInvalid")
    public void onClicAuthor(View view) {
        String text = getResources().getString(R.string.sample_quo);
        String author = getResources().getString(R.string.sample_aut);
        setQuote(new Quotation(text,author));
    }
}