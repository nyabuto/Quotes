package Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Geofrey on 6/6/2016.
 */
public class manageDB extends DatabaseHandler {
    SQLiteDatabase db;

 public  manageDB(Context context){
        super(context);
    }

    public int getQuotes(){
        db=this.getReadableDatabase();
       Cursor quoteCursor = db.rawQuery("SELECT quote_id,genre_id,author_id,quote,timestamp FROM quotes",null);
        Log.d("row count is : ",""+quoteCursor.getCount());
        if(quoteCursor.moveToFirst()){
            String quote_id,genre_id,author_id,quote,timestamp;
            do{
               quote_id=quoteCursor.getString(quoteCursor.getColumnIndex("quote_id"));
                genre_id=quoteCursor.getString(quoteCursor.getColumnIndex("genre_id"));
                author_id=quoteCursor.getString(quoteCursor.getColumnIndex("author_id"));
                quote=quoteCursor.getString(quoteCursor.getColumnIndex("quote"));
                timestamp=quoteCursor.getString(quoteCursor.getColumnIndex("timestamp"));

                Log.d("quote data","quote id : "+quote_id+" genre_id : "+genre_id+" author_id : "+author_id+" quote : "+quote+" timestamp : "+timestamp);


            }while (quoteCursor.moveToNext());
        }

        return 1;
    }

//    LOAD ALL QUOTES
    public JSONArray loadAllQuotes(){
      db=this.getReadableDatabase();
int position=0;
        JSONArray jsonArray = new JSONArray();
        Cursor cursorGenre= db.rawQuery("SELECT genre_id,genre_name FROM genre",null);
        if(cursorGenre.moveToFirst()){

            do{
                JSONObject jsonObject = new JSONObject();

String genre_id=cursorGenre.getString(cursorGenre.getColumnIndex("genre_id"));
String genre_name=cursorGenre.getString(cursorGenre.getColumnIndex("genre_name"));

try {

    jsonObject.put("title_id", genre_id);
    jsonObject.put("title_name", genre_name);
}
catch (JSONException e){
    e.printStackTrace();
}
JSONArray jsonArrayData = new JSONArray();
// select all data as required
     Cursor cursorQuotes=db.rawQuery("SELECT quotes.quote_id AS quoteid, quotes.quote AS quotename,author_name FROM quotes LEFT JOIN authors ON quotes.author_id=authors.author_id WHERE quotes.genre_id='"+genre_id+"'",null);
//                Log.d("quote length",""+cursorQuotes.getCount());
                if(cursorQuotes.moveToFirst()){
                    int quotPos=0;
                    do{
                        quotPos++;

                        JSONObject jsonObjectData = new JSONObject();
                        String quote_id=cursorQuotes.getString(cursorQuotes.getColumnIndex("quoteid"));
                        String quote_name=cursorQuotes.getString(cursorQuotes.getColumnIndex("quotename"));
                        String author_name=cursorQuotes.getString(cursorQuotes.getColumnIndex("author_name"));
                        try {
                            jsonObjectData.put("quote_id", quote_id);
                            jsonObjectData.put("quote_name", quotPos+". "+quote_name);
                            jsonObjectData.put("author_name", author_name);
                            jsonArrayData.put(jsonObjectData);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }while (cursorQuotes.moveToNext());
                }
try {
    jsonObject.put("no_quotes", cursorQuotes.getCount());
    jsonObject.put("data", jsonArrayData);

}catch (JSONException e){
    e.printStackTrace();
}

                try {

                    jsonArray.put(position, jsonObject);
                }catch (JSONException e){
                    e.printStackTrace();
                }


                position++;

            }while(cursorGenre.moveToNext());
        }
return jsonArray;
    }



 public JSONArray loadGenreData(String genre_id){
     db=this.getReadableDatabase();
     int position=0;
     JSONArray jsonArray = new JSONArray();

     Cursor cursorAuthor= db.rawQuery("SELECT COUNT(quotes.quote_id) AS no_quotes, quotes.author_id AS authorid,author_name FROM authors JOIN quotes ON authors.author_id=quotes.author_id WHERE quotes.genre_id='"+genre_id+"' GROUP BY quotes.author_id",null);
     if(cursorAuthor.moveToFirst()){
         do{
             JSONObject jsonObject = new JSONObject();

             String no_quotes=cursorAuthor.getString(cursorAuthor.getColumnIndex("no_quotes"));
             String author_id=cursorAuthor.getString(cursorAuthor.getColumnIndex("authorid"));
             String author_name=cursorAuthor.getString(cursorAuthor.getColumnIndex("author_name"));

             try {
                 jsonObject.put("no_quotes", no_quotes);
                 jsonObject.put("title_id", author_id);
                 jsonObject.put("title_name", author_name);
             }catch (JSONException e){
                 e.printStackTrace();
             }

             JSONArray jsonArrayData = new JSONArray();
// select all data as required
             Cursor cursorQuotes=db.rawQuery("SELECT quotes.quote_id AS quoteid, quotes.quote AS quotename, authors.author_name AS author_name FROM quotes LEFT JOIN authors ON quotes.author_id=authors.author_id WHERE quotes.author_id='"+author_id+"' AND quotes.genre_id='"+genre_id+"'",null);
             if(cursorQuotes.moveToFirst()){
                 int quotPos=0;
                 do{
                     quotPos++;
                     JSONObject jsonObjectData = new JSONObject();

                     String quote_id=cursorQuotes.getString(cursorQuotes.getColumnIndex("quoteid"));
                     String quote_name=cursorQuotes.getString(cursorQuotes.getColumnIndex("quotename"));
try {
    jsonObjectData.put("quote_id", quote_id);
    jsonObjectData.put("quote_name", quotPos+". "+quote_name);
    jsonObjectData.put("author_name", "");
    jsonArrayData.put(jsonObjectData);
}catch (JSONException e){
    e.printStackTrace();
}
                 }while (cursorQuotes.moveToNext());
             }
             try {
                 jsonObject.put("data", jsonArrayData);

             }catch (JSONException e){
                 e.printStackTrace();
             }

             try {

                 jsonArray.put(position, jsonObject);
             }catch (JSONException e){
                 e.printStackTrace();
             }


             position++;

         }while(cursorAuthor.moveToNext());
     }
     return jsonArray;
 }



}
