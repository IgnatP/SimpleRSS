package com.ivpomazkov.simplerss.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ivpomazkov.simplerss.database.RSSChannelDbSchema.RSSChannelsTable;

/**
 * Created by Ivpomazkov on 07.06.2016.
 */
public class RSSChannelDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "rsschannelsBase.db";
    public RSSChannelDbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RSSChannelsTable.NAME + "("+
                    " _id integer primary key autoincrement, " +
                        RSSChannelsTable.RSSChannelsColumns.URL + ", " +
                        RSSChannelsTable.RSSChannelsColumns.DESCRIPTION + ", " +
                        RSSChannelsTable.RSSChannelsColumns.ISACTIVE + ")"
                    );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
