package com.example.citasiznardo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.citasiznardo.R;

import java.util.ArrayList;
import adapter.Quotation;
import adapter.RecyclerAux;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        RecyclerAux recycler = new RecyclerAux(getMockQuotations());
        RecyclerView rview = findViewById(R.id.idFavourite);



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
        return lista;
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