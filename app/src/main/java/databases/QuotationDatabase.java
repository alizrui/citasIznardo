package databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import adapter.Quotation;

@Database(entities = {Quotation.class}, version = 1, exportSchema = false)
public abstract class QuotationDatabase extends RoomDatabase {

    public abstract QuotesDao quotesDao();
    private static QuotationDatabase quotationDatabase;

    public synchronized static QuotationDatabase getInstance(Context context){
        if (quotationDatabase == null){
            quotationDatabase = Room
                    .databaseBuilder(context, QuotationDatabase.class, "quotation_database")
                    .build();
        }
        return quotationDatabase;
    }

    public synchronized static void destroyInstance(){
        if(quotationDatabase != null && quotationDatabase.isOpen()){
            quotationDatabase.close();
            quotationDatabase = null;
        }
    }
}
