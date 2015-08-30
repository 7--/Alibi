package sql;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locations";
    private static final String TABLE_NAME = "locate";
 
    // Locates Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LOG = "log";
    private static final String KEY_SPEED = "speed";
    private static final String KEY_ALT = "alt";
    private static final String KEY_ACC = "acc";

    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LocateS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TIME + " INTEGER,"
                + KEY_LAT + " REAL," + KEY_LOG + " REAL," + KEY_SPEED + " REAL," + 
                KEY_ALT + " REAL," + KEY_ACC + " REAL" + ")";
        db.execSQL(CREATE_LocateS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    
    public void addLocate(Locate locate) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, locate.getTime()); // Locate Name
        values.put(KEY_LAT, locate.getLat()); 
        values.put(KEY_LOG, locate.getLog()); 
        values.put(KEY_SPEED, locate.getSpeed()); 
        values.put(KEY_ALT, locate.getAlt()); 
        values.put(KEY_ACC, locate.getAcc()); 
 
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
    
    public List<Locate> getAllLocates() {
        List<Locate> LocateList = new ArrayList<Locate>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Locate Locate = new Locate(Integer.parseInt(cursor.getString(0)),
                		Long.parseLong(cursor.getString(1)),
                		Double.parseDouble(cursor.getString(2)),
                        Double.parseDouble(cursor.getString(3)),
                		Float.parseFloat(cursor.getString(4)),
                		Double.parseDouble(cursor.getString(5)),
                		Float.parseFloat(cursor.getString(6))
                		);
                // Adding Locate to list
                LocateList.add(Locate);
            } while (cursor.moveToNext());
        }
 
        // return Locate list
        return LocateList;
    }
    
}