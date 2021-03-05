package adapter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quotation_table")
public class Quotation {

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_ID")
    private int id;
    @ColumnInfo(name = "quote_col") @NonNull
    private String quoteText;
    @ColumnInfo(name = "author_col")
    private String quoteAuthor;

    public Quotation(){
    }
    public Quotation(String quote, String auth){
        quoteText = quote;
        quoteAuthor = auth;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }


}
