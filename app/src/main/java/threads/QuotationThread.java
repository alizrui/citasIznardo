package threads;


import android.os.Handler;
import android.os.Looper;
import com.example.citasiznardo.activities.FavouriteActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import adapter.Quotation;
import databases.MySqliteOpenHelper;
import databases.QuotationDatabase;

public class QuotationThread extends Thread {

    private final WeakReference<FavouriteActivity> reference;
    private final boolean databaseMode; // sqlite = false, room = true

    public QuotationThread(FavouriteActivity reference, boolean databaseMode) {
        this.reference = new WeakReference<>(reference);
        this.databaseMode = databaseMode;
    }

    @Override
    public void run() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final FavouriteActivity activity = reference.get();

        /* Gets the quotes from the database depending on the database mode */
        if(activity != null){
            ArrayList<Quotation> arrayQuotes = (databaseMode) ?
                    (ArrayList<Quotation>) QuotationDatabase.getInstance(activity).quotesDao().getQuotes() :
                    MySqliteOpenHelper.getInstance(activity).getQuotes();
            handler.post(()-> activity.addAndRefreshFav(arrayQuotes));
        }
    }
}
