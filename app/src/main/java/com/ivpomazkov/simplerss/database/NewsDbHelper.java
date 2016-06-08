package com.ivpomazkov.simplerss.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ivpomazkov.simplerss.database.NewsDbSchema.NewsTable;

/**
 * Created by Ivpomazkov on 08.06.2016.
 */
public class NewsDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "newsBase.db";
    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NewsTable.NAME + "(" +
        "_id integer primary key autoincrement, " +
        NewsTable.Cols.UUID + ", " +
        NewsTable.Cols.TITLE + ", " +
        NewsTable.Cols.DESCRIPTION + ", " +
        NewsTable.Cols.LINK + ", " +
        NewsTable.Cols.PUBLICATION_DATE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
