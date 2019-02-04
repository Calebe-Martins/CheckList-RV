package com.cgm.checklist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_FOLDER = "folder_table";
    private final String TABLE_NAME = "";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";

    public DBHelper(Context context) {
        super(context, TABLE_FOLDER, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "
                + TABLE_FOLDER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDER);
        onCreate(db);
    }
}
