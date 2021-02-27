package databases;

import android.provider.BaseColumns;

public class QuotationContract {

    private QuotationContract(){}

    static class MyTableEntry implements BaseColumns {
        static final String TABLE_NAME = "quotation_table";
        static final String _ID = "_ID";
        static final String AUTHOR_NAME = "author_col";
        static final String QUOTE_TEXT = "quote_col";
    }
}
