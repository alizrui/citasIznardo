package com.example.citasiznardo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.ConnectivityDiagnosticsManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.citasiznardo.R;

import org.w3c.dom.Text;

import adapter.Quotation;
import databases.MySqliteOpenHelper;
import databases.QuotationDatabase;
import threads.WebServiceThread;

public class QuotationActivity extends AppCompatActivity {

    private boolean addVisible = false;
    private Menu optionsMenu = null;
    private MenuItem addItem = null;
    private MenuItem refreshItem = null;
    private TextView tvScroll;
    private TextView tvAuthor;
    private ProgressBar progressBar;
    private String language;
    private String httpmethod;
    private Boolean databaseMode = true; // sqlite = false, room = true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        tvScroll = findViewById(R.id.tvScroll1);
        tvAuthor = findViewById(R.id.tvAuthor);
        progressBar = findViewById(R.id.progressBar);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        databaseMode = !prefs.getString("database", "").equals(getResources().getString(R.string.sqlite));
        language = prefs.getString("language","");
        httpmethod = prefs.getString("http","");
        if(savedInstanceState != null) {
            tvScroll.setText(savedInstanceState.getString("quote_key"));
            tvAuthor.setText(savedInstanceState.getString("author_key"));
            addVisible = savedInstanceState.getBoolean("visible_add");

        } else {
            String name = prefs.getString("username", "");
            tvScroll.setText(String.format((String) tvScroll.getText(), (name == null || name.equals("")) ? "Nameless One" : name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.quotation_menu, menu);
        addItem = menu.findItem(R.id.item_add);
        refreshItem = menu.findItem(R.id.item_refresh);
        addItem.setVisible(addVisible);
        optionsMenu = menu;
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item_add:
                if(databaseMode){
                    QuotationDatabase quotationDatabase = QuotationDatabase.getInstance(this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            quotationDatabase.quotesDao().addQuote(new Quotation((String) tvScroll.getText(), (String) tvAuthor.getText()));
                        }
                    }).start();
                } else {
                    MySqliteOpenHelper mySqliteOpenHelper = MySqliteOpenHelper.getInstance(this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mySqliteOpenHelper.addQuotation((String) tvScroll.getText(), (String) tvAuthor.getText());
                        }
                    }).start();
                }
                item.setVisible(false);
                return true;
            case R.id.item_refresh:
                WebServiceThread webServiceThread = new WebServiceThread(this,language,httpmethod);
                if(isConnected()) webServiceThread.start();
                //onClicAuthor(item.getActionView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StringFormatInvalid")
    public void onClicAuthor(View view) {
        String text = getResources().getString(R.string.sample_quo);
        String author = getResources().getString(R.string.sample_aut);
        setQuote(new Quotation(text,author));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("quote_key",(String) tvScroll.getText());
        outState.putString("author_key",(String) tvAuthor.getText());
        outState.putBoolean("visible_add",addVisible);
    }

    private void setQuote(Quotation quote){
        tvScroll.setText(quote.getQuoteText());
        tvAuthor.setText(quote.getQuoteAuthor());
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
                    addVisible = !mySqliteOpenHelper.isQuotation((String) tvScroll.getText());
                    handler.post(() -> addItem.setVisible(addVisible));
                }
            }).start();
        }
    }

    /* Hides ActionBar and shows progress bar */
    public void showProgress(){
        if (addItem != null) addItem.setVisible(false);
        if (refreshItem != null) refreshItem.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    /* Shows received quote and hide progress bar */
    public void showQuote(Quotation quote){
        progressBar.setVisibility(View.INVISIBLE);
        setQuote(quote);
        refreshItem.setVisible(true);
    }

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
}