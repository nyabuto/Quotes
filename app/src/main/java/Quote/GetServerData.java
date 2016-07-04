package Quote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Database.DatabaseHandler;
import softikoda.com.quotes.AllQuotes;

/**
 * Created by Geofrey on 6/7/2016.
 */
public class GetServerData extends DatabaseHandler{
SQLiteDatabase db;
 public Context context;
    JSONArray newData=null;
public GetServerData(Context context){
 super(context);
}

public void checkServerData(){}


    public String getLastSync(){
        db=this.getReadableDatabase();
        String timestamp="";

        Cursor cursor = db.rawQuery("SELECT timestamp FROM last_sync WHERE id='1'",null);
        if(cursor.moveToFirst()){
         timestamp=cursor.getString(cursor.getColumnIndex("timestamp"));
         }

        return timestamp;
    }

    public void updateSync(String timestamp){
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timestamp",timestamp);

        db.update("last_sync",values,"id=?",new String[]{"1"});

    }

    public int checkAuthor(String author_name){
        db = this.getReadableDatabase();
        int rows=db.query(false,"authors",new String[]{"author_id"},"author_name=?", new String[]{author_name},null,null,null,null).getCount();
  return rows;
    }

    public void addAuthors(String author_id,String author_name,String timestamp){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("author_id",author_id);
        values.put("author_name",author_name);
        values.put("timestamp",timestamp);

        db.insert("authors",null,values);
    }

    public int checkGenre(String genre_name){
        db=this.getReadableDatabase();
        int rows=db.query(false,"genre",new String[]{"genre_id"},"genre_name=?", new String[]{genre_name},null,null,null,null).getCount();
        return rows;
    }

    public void addGenre(String genre_id,String genre_name,String timestamp){
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("genre_id",genre_id);
        values.put("genre_name",genre_name);
        values.put("timestamp",timestamp);

        db.insert("genre",null,values);
    }

    public int checkQuote(String quote_id){
        db = this.getReadableDatabase();
        String whereClause="quote_id=?";
        String[] whereArgs={quote_id};

        int rows=db.query(false,"quotes",new String[]{"quote_id"},whereClause,whereArgs,null,null,null,null).getCount();
        return rows;
    }

    public void addQuote(String quote_id,String genre_id,String author_id,String quote,String timestamp){
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quote_id",quote_id);
        values.put("genre_id",genre_id);
        values.put("author_id",author_id);
        values.put("quote",quote);
        values.put("timestamp",timestamp);

        db.insert("quotes",null,values);
    }

public int serverQuotes(final Context context) {
    String finalUrl = "http://www.softikoda.com/quotes/getNewQuotes.php?timestamp="+getLastSync().replace(" ","%20");
    Log.d("url : ",finalUrl);
    RequestQueue queue = Volley.newRequestQueue(context);
    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, finalUrl, null, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            try {
                if (response.getInt("success") > 0) {
                    newData = response.getJSONArray("new_quotes");
                    for (int i = 0; i < newData.length(); i++) {
                        JSONObject objData = newData.getJSONObject(i);

                        String quote_id = objData.getString("quote_id");
                        String genre_id = objData.getString("genre_id");
                        String author_id = objData.getString("author_id");
                        String quote = objData.getString("quote");
                        String timestamp = objData.getString("timestamp");

                        if (checkQuote(quote_id) == 0) {
                            addQuote(quote_id, genre_id, author_id, quote, timestamp);
                        }

                    }

                }
                serverAuthors(context);

                }catch(JSONException e){
                    e.printStackTrace();
                }

        }
    }
    , new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });
    queue.add(request);
return 1;
}

    public void serverAuthors(final Context context) {
        String finalUrl = "http://www.softikoda.com/quotes/getNewAuthors.php?timestamp="+getLastSync().replace(" ","%20");

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, finalUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("success") > 0) {
                        newData = response.getJSONArray("new_authors");
                        for (int i = 0; i < newData.length(); i++) {
                            JSONObject objData = newData.getJSONObject(i);

                            String author_id = objData.getString("author_id");
                            String author_name = objData.getString("author_name");
                            String timestamp = objData.getString("timestamp");

                            if (checkAuthor(author_name) == 0) {
                                addAuthors(author_id, author_name, timestamp);
                            }

                        }


                    }
                    serverGenres(context);
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);

    }

    public void serverGenres(final Context context) {
        String finalUrl = "http://www.softikoda.com/quotes/getNewGenres.php?timestamp="+getLastSync().replace(" ","%20");

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, finalUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("success") > 0) {
                        newData = response.getJSONArray("new_genres");
                        for (int i = 0; i < newData.length(); i++) {
                            JSONObject objData = newData.getJSONObject(i);

                            String genre_id = objData.getString("genre_id");
                            String genre_name = objData.getString("genre_name");
                            String timestamp = objData.getString("timestamp");

                            if (checkGenre(genre_name) == 0) {
                                addGenre(genre_id, genre_name, timestamp);
                            }

                        }

                    }
                    updateTimestamp(context);
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);

    }

    public void updateTimestamp(final Context context) {
        String finalUrl = "http://www.softikoda.com/quotes/lastDownload.php";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
updateSync(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        );
        queue.add(request);

    }

}
