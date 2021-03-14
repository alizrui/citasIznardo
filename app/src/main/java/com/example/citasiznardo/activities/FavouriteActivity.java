package com.example.citasiznardo.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.citasiznardo.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import adapter.Quotation;
import adapter.RecyclerAux;
import databases.MySqliteOpenHelper;
import databases.QuotationDatabase;
import databases.QuotesDao;
import threads.QuotationThread;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerAux recAux; // adapter
    private Boolean databaseMode = true; // sqlite = false, room = true
    private MenuItem itemDelete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        /* Get reference to the view and set a LayoutManager */
        RecyclerView view = findViewById(R.id.idFavourite);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        view.setLayoutManager(manager);
        view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        /* Create a RecyclerAux with */
        recAux = new RecyclerAux(new ArrayList<Quotation>(), position -> {
            try {
                getWiki(position);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }, this::dialogAndRemove);

        view.setAdapter(recAux);

        /* Set the mode to access the database with the SharedPreferences */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        databaseMode = !prefs.getString("database", "").equals(getResources().getString(R.string.sqlite));

        /* Creates a thread to handle the Quotation Thread*/
        QuotationThread thread = new QuotationThread(this, databaseMode);
        thread.start();
    }

    /* Adds the list to the RecyclerAux */
    public void addAndRefreshFav(ArrayList<Quotation> list){
        recAux.addItems(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /* If there are quotes, shows the menu with the item delete */
        if(recAux.getItemCount() != 0)
            getMenuInflater().inflate(R.menu.quotation_menu_2, menu);

        /* Keeps a reference to the item delete */
        itemDelete = menu.findItem(R.id.item_delete);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.item_delete) {

            /* Shows an AlertDialog to remove all the quotes */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialogrmall_q);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {

                /* Creates a thread to remove all the quotes depending on the database mode */
                if (databaseMode) {
                    QuotationDatabase quotationDatabase = QuotationDatabase.getInstance(this);
                    new Thread(() -> quotationDatabase.quotesDao().deleteAllQuotes()).start();
                } else {
                    MySqliteOpenHelper mySqliteOpenHelper = MySqliteOpenHelper.getInstance(this);
                    new Thread(mySqliteOpenHelper::deleteAllQuotes).start();
                }

                item.setVisible(false);
                recAux.removeAllItems();
            });

            builder.setNegativeButton(R.string.no, null);
            builder.create().show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Opens the wikipedia page for the selected Quotation Author */
    private void getWiki (int pos) throws UnsupportedEncodingException {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        String authorName = recAux.getAutFromPos(pos);

        if(authorName == null || authorName.equals("")){
            Toast.makeText(this,"There is no information",Toast.LENGTH_SHORT).show();
        } else {
            String authorNameEncoded = URLEncoder.encode(authorName, "utf-8");
            String link = "https://en.wikipedia.org/wiki/Special:Search?search=";
            intent.setData(Uri.parse(link + authorNameEncoded));
            startActivity(intent);
        }
    }

    /* Dialog for removing the Quotation in the given position */
    public void dialogAndRemove(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_q);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                    /* Deletes the quote depending on the database mode */
                    if (databaseMode) {
                        QuotesDao dao = QuotationDatabase.getInstance(this).quotesDao();
                        String quote = recAux.getQuoteFromPos(pos);
                        new Thread(() -> dao.deleteQuote(dao.findQuote(quote))).start();
                    } else {
                        MySqliteOpenHelper mySqliteOpenHelper = MySqliteOpenHelper.getInstance(this);
                        String quote = recAux.getQuoteFromPos(pos);
                        new Thread(() -> mySqliteOpenHelper.deleteQuote(quote)).start();
                    }
                    recAux.removeItem(pos);
                    if(recAux.getItemCount() == 0) itemDelete.setVisible(false); // if there is no quotes, hides the item delete
                });
        builder.setNegativeButton(R.string.no, null);
        builder.create().show();
    }

    /* Creates an arraylist with sample quotations */
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
}