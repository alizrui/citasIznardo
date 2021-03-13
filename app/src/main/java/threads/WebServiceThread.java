package threads;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.citasiznardo.R;
import com.example.citasiznardo.activities.QuotationActivity;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import adapter.Quotation;

public class WebServiceThread extends Thread{

    private final WeakReference<QuotationActivity> reference;
    private final String language;
    private final String httpmethod;

    public WebServiceThread(QuotationActivity reference, String language, String method){
        this.reference = new WeakReference<>(reference);
        this.language = language;
        this.httpmethod = method;
    }

    @Override
    public void run() {
        Quotation quote = new Quotation("","");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.forismatic.com");
        builder.appendPath("api");
        builder.appendPath("1.0");
        builder.appendPath("");
        if(httpmethod.equals("GET")) {
            builder.appendQueryParameter("method","getQuote");
            builder.appendQueryParameter("format","json");
            builder.appendQueryParameter("lang",language);
            try {
                URL url = new URL(builder.build().toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Gson gson = new Gson();
                    quote = gson.fromJson(reader,Quotation.class);
                    reader.close();
                }
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                String body = "method=getQuote&format=json&lang="+language;
                URL url = new URL(builder.build().toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(body);
                writer.flush();
                writer.close();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Gson gson = new Gson();
                    quote = gson.fromJson(reader,Quotation.class);
                    reader.close();
                }
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Quotation finalQuote = quote;
        reference.get().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        reference.get().showQuote(finalQuote);
                    }
                }
        );
    }
}
