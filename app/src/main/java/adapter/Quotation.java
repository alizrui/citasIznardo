package adapter;

public class Quotation {
    private String quoteText;
    private String quoteAuthor;

    public Quotation(String quote, String auth){
        quoteText = quote;
        quoteAuthor = auth;
    }
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
