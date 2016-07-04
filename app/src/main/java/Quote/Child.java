package Quote;

/**
 * Created by Geofrey on 6/6/2016.
 */
public class Child {
    private String quote;
    private String quote_id;
    private String author;

    public Child(String quote,String quote_id,String author){
        this.quote=quote;
        this.quote_id=quote_id;
        this.author=author;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
