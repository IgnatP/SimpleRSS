package com.ivpomazkov.simplerss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ivpomazkov.simplerss.database.RSSChannelCursorWrapper;
import com.ivpomazkov.simplerss.database.RSSChannelDbHelper;
import com.ivpomazkov.simplerss.database.RSSChannelDbSchema.RSSChannelsTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivpomazkov on 01.06.2016.
 */
public class RSSChannelList {
    private static RSSChannelList sRSSChannelList;
    private List<RSSChannel> mChannels;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static RSSChannelList get(Context context){
        if (sRSSChannelList == null)
            sRSSChannelList = new RSSChannelList(context);
        return sRSSChannelList;
    }
    public void addChannel(RSSChannel channel){
        ContentValues values = getContentValues(channel);
        mDatabase.insert(RSSChannelsTable.NAME, null, values);
    }

    public void updateChannel(RSSChannel channel){
        String urlId = channel.getUrl();
        ContentValues values = getContentValues(channel);
        mDatabase.update(RSSChannelsTable.NAME, values, RSSChannelsTable.RSSChannelsColumns.URL + " = ?", new String[] {urlId});
    }

    public void deleteChannel(RSSChannel channel){
        mDatabase.delete(RSSChannelsTable.NAME, RSSChannelsTable.RSSChannelsColumns.UUID + " = ?", new String[] {channel.getUUID().toString()});
    }

    public List<RSSChannel> getChannels(){
        mChannels = new ArrayList<>();
        RSSChannelCursorWrapper cursor = queryChannels(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                mChannels.add(cursor.getChannel());
                cursor.moveToNext();
            }
        } finally{
            cursor.close();
        }
        return mChannels;
    }

    public int getSize(){
        return mChannels.size();
    }

    public List<String> getActiveURLs() {
       List<String> urls = new ArrayList<>();
        RSSChannelCursorWrapper cursor = queryChannels(RSSChannelsTable.RSSChannelsColumns.ISACTIVE + " = 1", null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                urls.add(cursor.getChannel().getUrl());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return urls;
    }

    private RSSChannelList(Context context){
        mChannels = new ArrayList<>();
        mContext = context;
        mDatabase = new RSSChannelDbHelper(mContext).getWritableDatabase();
    }

    private RSSChannelCursorWrapper queryChannels(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                RSSChannelsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new RSSChannelCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(RSSChannel channel){
        ContentValues values = new ContentValues();
        values.put(RSSChannelsTable.RSSChannelsColumns.UUID, channel.getUUID().toString());
        values.put(RSSChannelsTable.RSSChannelsColumns.DESCRIPTION, channel.getDescription());
        values.put(RSSChannelsTable.RSSChannelsColumns.URL, channel.getUrl());
        values.put(RSSChannelsTable.RSSChannelsColumns.ISACTIVE, channel.isActive() ? 1 : 0);
        return values;
    }

}
