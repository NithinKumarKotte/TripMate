package com.example.d27sa.tripmatetry1;

/**
 * Created by d27sa on 22-04-2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 4/8/2017.
 */


public class dbhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="listDatabase";
    private static int count = 0;
    private static String listName;
    SQLiteDatabase db;
    public dbhelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    static {
        count++;
    }

    @Override

    public void onCreate(SQLiteDatabase database) {

        database.execSQL("CREATE TABLE "+listName+"( _id INTEGER PRIMARY KEY AUTOINCREMENT, placeId TEXT, description TEXT);");

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS"+listName);

        onCreate(db);

    }

    public void addlist(String placeId,String description,String list)

    {
        listName=list;

        ContentValues values=new ContentValues(2);

        values.put("placeId", placeId);

        values.put("description", description);

        getWritableDatabase().insert(listName,null, values);

        //udgvsufwev

        System.out.println(placeId);
        System.out.println(description);

    }

    public Cursor getList(String list)

    {
        listName=list;
        System.out.println("inside get of database");
        Cursor cursor = getReadableDatabase().rawQuery("SELECT placeId FROM "+listName, null);
        return cursor;

    }



    public void deleteHalt(String placeId,String list){
       // Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM list",null);
        //String id= cursor.getString(0);
        listName=list;
        System.out.println("inside delete" + placeId);
        getWritableDatabase().delete(listName,"placeId=?",new String[]{placeId});
    }

    public void createlist(String list) {
        listName=list;
        db = getWritableDatabase();
        String CREATE_TABLE = "CREATE TABLE "+list+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, placeId TEXT, description TEXT)";
        db.execSQL(CREATE_TABLE);
        db.close();
    }

}
