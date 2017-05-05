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
    public dbhelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    static {
        count++;
    }

    @Override

    public void onCreate(SQLiteDatabase database) {

        database.execSQL("CREATE TABLE list ( _id INTEGER PRIMARY KEY AUTOINCREMENT, placeId TEXT, description TEXT);");

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS list");

        onCreate(db);

    }

    public void addlist(String placeId,String description)

    {

        ContentValues values=new ContentValues(2);

        values.put("placeId", placeId);

        values.put("description", description);

        getWritableDatabase().insert("list",null, values);

        //udgvsufwev

        System.out.println(placeId);
        System.out.println(description);

    }

    public Cursor getList()

    {
        System.out.println("inside get of database");
        Cursor cursor = getReadableDatabase().rawQuery("SELECT placeId FROM list", null);
        return cursor;

    }



    public void deleteHalt(String placeId){
       // Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM list",null);
        //String id= cursor.getString(0);
        System.out.println("inside delete" + placeId);
        getWritableDatabase().delete("list","placeId=?",new String[]{placeId});
    }

}
