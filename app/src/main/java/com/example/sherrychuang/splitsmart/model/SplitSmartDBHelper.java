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
    private static SQLiteDatabase database = null;

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
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventDAO.CREATE_TABLE);
        db.execSQL(PersonDAO.CREATE_TABLE);
        db.execSQL(BillDAO.CREATE_TABLE);
        db.execSQL(ItemDAO.CREATE_TABLE);
        db.execSQL(BillPersonRelation.CREATE_TABLE);
        db.execSQL(ItemPersonRelation.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + EventDAO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PersonDAO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BillDAO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemDAO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BillPersonRelation.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemPersonRelation.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }
}
