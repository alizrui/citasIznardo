package databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import adapter.Quotation;

@Dao
public interface QuotesDao {

    @Query("SELECT * FROM quotation_table")
    List<Quotation> getQuotes();

    @Insert
    void addQuote(Quotation quote);

    @Delete
    void deleteQuote(Quotation quote);

    @Query("SELECT * FROM quotation_table WHERE quote_col = :textQuote")
    Quotation findQuote(String textQuote);

    @Query("DELETE FROM quotation_table")
    void deleteAllQuotes();
}
