package databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import adapter.Quotation;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    private static MySqliteOpenHelper mySOH;

    private MySqliteOpenHelper(Context context){
        super(context,"quotation_database",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(
                    "CREATE TABLE quotation_table (_ID INTEGER PRIMARY KEY AUTOINCREMENT " +
                            "NOT NULL, quote_col TEXT NOT NULL, author_col TEXT);");
        } catch (SQLiteException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static synchronized MySqliteOpenHelper getInstance(Context context){
        if(mySOH == null) mySOH = new MySqliteOpenHelper(context);
        return mySOH;
    }

    public ArrayList<Quotation> getQuotations(){
        SQLiteDatabase db = mySOH.getReadableDatabase();
        Cursor cursor = db.query("quotation_table", new String[]{"quote_col","author_col"},
                null,null,null,null,null);
        ArrayList<Quotation> listQ = new ArrayList<Quotation>();
        while (cursor.moveToNext()){
            listQ.add(new Quotation(cursor.getString(0), cursor.getString(1)));
        }
        cursor.close(); db.close();
        return listQ;
    }

    public boolean isQuotation(String quote){
        Boolean res = false;
        SQLiteDatabase db = mySOH.getReadableDatabase();
        Cursor cursor = db.query("quotation_table", null, "quote_col=?",
                new String[]{quote}, null, null, null, null);
        if(cursor.getCount() > 0){
            res = true;
        }
        db.close(); cursor.close();
        return res;
    }

    public void addQuotation(String quote, String author){
        ContentValues values = new ContentValues();
        values.put("quote_col", quote);
        values.put("author_col",author);

        SQLiteDatabase db = mySOH.getWritableDatabase();
        db.insert("quotation_table",null, values);

        db.close();
    }

    public void removeAllQuotes(){
        SQLiteDatabase db = mySOH.getWritableDatabase();
        db.delete("quotation_table",null,null);
        db.close();
    }

    public void removeQuote(String quote){
        SQLiteDatabase db = mySOH.getWritableDatabase();
        db.delete("quotation_table","quote_col=?",new String[]{quote});
        db.close();
    }
}
