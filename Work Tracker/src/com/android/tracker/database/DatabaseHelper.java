package com.android.tracker.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author vlad
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper 
{
	
	ArrayList<String> tables;
	
    DatabaseHelper(Context context, String databaseName, int databaseVersion, ArrayList<String> tables) 
    {
        super(context, databaseName, null, databaseVersion);
        this.tables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) 
    {
    	for (String table : tables)
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, 
    int newVersion) 
    {
        Log.w("Database", "Upgrading database from version " + oldVersion 
                + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS titles");
        onCreate(db);
    }
}
