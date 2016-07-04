package Quote;

import java.util.ArrayList;

/**
 * Created by Geofrey on 6/6/2016.
 */
public class Parent {
    private String genre_author;
    private String no_of_Quotes;
private ArrayList<Child> childData = new ArrayList<Child>();

    public Parent(String genre_author, String no_of_Quotes, ArrayList<Child> childData){
      this.genre_author=genre_author;
        this.no_of_Quotes=no_of_Quotes;
        this.childData=childData;
    }

    public String getGenre_author() {
        return genre_author;
    }

    public void setGenre_author(String genre_author) {
        this.genre_author = genre_author;
    }

    public String getNo_of_Quotes() {
        return no_of_Quotes;
    }

    public void setNo_of_Quotes(String no_of_Quotes) {
        this.no_of_Quotes = no_of_Quotes;
    }

    public ArrayList<Child> getChildData() {
        return childData;
    }

    public void setChildData(ArrayList<Child> childData) {
        this.childData = childData;
    }
}
