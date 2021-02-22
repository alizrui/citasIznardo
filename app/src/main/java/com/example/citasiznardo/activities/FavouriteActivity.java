package com.example.citasiznardo.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.citasiznardo.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import adapter.Quotation;
import adapter.RecyclerAux;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerAux recAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        recAux = new RecyclerAux(getMockQuotations(), position -> {
            try {
                getWiki(position);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }, position -> {
            dialogAndRemove(position);
        });
        RecyclerView view = findViewById(R.id.idFavourite);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        view.setLayoutManager(manager);
        view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        view.setAdapter(recAux);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(recAux.getItemCount() != 0) getMenuInflater().inflate(R.menu.quotation_menu_2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialogrmall_q);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            recAux.removeAllItems();
            findViewById(item.getItemId()).setVisibility(View.INVISIBLE);
        });
        builder.setNegativeButton(R.string.no, null);
        builder.create().show();
        return true;
    }

    public ArrayList<Quotation> getMockQuotations(){
        ArrayList<Quotation> lista = new ArrayList<Quotation>();

        lista.add(new Quotation("“Ser rico es malo, es inhumano. Así lo digo y condeno a los ricos”", "Hugo Chávez Frías"));
        lista.add(new Quotation("“No hay camino para la paz, la paz es el camino”", "Mahatma Gandhi"));
        lista.add(new Quotation("“Me preocupa más ser buena persona que ser el mejor jugador del mundo.”","Leo Messi"));
        lista.add(new Quotation("“La mejor vida no es la más duradera, sino la más bien aquella que está repleta de buenas acciones.”","Marie Curie"));
        lista.add(new Quotation("“Me pinto a mí misma porque soy a quien mejor conozco.”","Frida Kahlo"));
        lista.add(new Quotation("“El nombre de Jesús Gil es innombrable.”","Jesús Gil"));
        lista.add(new Quotation("“La vida sería tan maravillosa si supiéramos qué hacer con ella.”","Greta Garbo"));
        lista.add(new Quotation("“No nacemos como mujer, sino que nos convertimos en una.”","Simone de Beauvoir"));
        lista.add(new Quotation("“Por ser rico, guapo y un gran jugador las personas tienen envidia de mí”","Cristiano Ronaldo"));
        lista.add(new Quotation("“Yo sólo confío en mí mismo.”","Jose María Aznar"));
        lista.add(new Quotation("“El rey es mi padre.”",""));
        return lista;
    }

    private void getWiki (int pos) throws UnsupportedEncodingException {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        String authorName = recAux.getAutFromPos(pos);

        if(authorName == null || authorName == ""){
            Toast.makeText(this,"There is no information",Toast.LENGTH_SHORT).show();
        } else {
            String authorNameEncoded = URLEncoder.encode(authorName, "utf-8");
            String link = "https://en.wikipedia.org/wiki/Special:Search?search=";
            intent.setData(Uri.parse(link + authorNameEncoded));
            startActivity(intent);
        }
    }

    public void dialogAndRemove(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_q);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> recAux.removeItem(pos));
        builder.setNegativeButton(R.string.no, null);
        builder.create().show();
    }
}