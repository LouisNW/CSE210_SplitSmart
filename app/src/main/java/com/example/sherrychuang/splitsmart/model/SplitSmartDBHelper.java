package com.example.sherrychuang.splitsmart.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Louis on 11/22/16.
 */

public class SplitSmartDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "splitSmart.db";
    private final static int DATABASE_VERSION = 1;
    private static SQLiteDatabase database;

    public SplitSmartDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new SplitSmartDBHelper(context).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
//        db.execSQL("DROP TABLE IF EXISTS " + ItemGPS.DATABASE_TABLE);
        // Create tables again
        onCreate(db);
    }
}
