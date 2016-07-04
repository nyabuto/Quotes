package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Geofrey on 2/3/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "Quotes187";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    String TABLE_AUTHORS = "CREATE TABLE authors ( author_id INTEGER PRIMARY KEY AUTOINCREMENT,author_name VARCHAR(100),timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
    db.execSQL(TABLE_AUTHORS);

    String TABLE_GENRE = "CREATE TABLE genre ( genre_id INTEGER PRIMARY KEY AUTOINCREMENT,genre_name VARCHAR(100),timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(TABLE_GENRE);

        String TABLE_QUOTES = "CREATE TABLE quotes ( quote_id INTEGER PRIMARY KEY AUTOINCREMENT,genre_id INTEGER, author_id INTEGER,quote TEXT, timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(TABLE_QUOTES);

        String TABLE_SYNC = "CREATE TABLE last_sync ( id INTEGER PRIMARY KEY AUTOINCREMENT,timestamp TIMESTAMP)";
        db.execSQL(TABLE_SYNC);

        String addLastSync="INSERT INTO last_sync(timestamp) VALUES('2016-06-06 11:53:16')";
        db.execSQL(addLastSync);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sqlAuthors = "DROP TABLE IF EXISTS authors";
        db.execSQL(sqlAuthors);

        String sqlGenre = "DROP TABLE IF EXISTS genre";
        db.execSQL(sqlGenre);

        String sqlQuotes = "DROP TABLE IF EXISTS quotes";
        db.execSQL(sqlQuotes);

        String sqlSync= "DROP TABLE IF EXISTS last_sync";
        db.execSQL(sqlSync);

onCreate(db);


    }
	
	

}
