package com.ivpomazkov.simplerss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ivpomazkov.simplerss.database.NewsCursorWrapper;
import com.ivpomazkov.simplerss.database.NewsDbHelper;
import com.ivpomazkov.simplerss.database.NewsDbSchema;
import com.ivpomazkov.simplerss.database.NewsDbSchema.NewsTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 01.06.2016.
 */
public class NewsList {
    private static NewsList sNewsList;
    private List<NewsItem> mNews;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static synchronized NewsList get(Context context){
        if (sNewsList == null)
            sNewsList = new NewsList(context);
         return sNewsList;
    }

    public List<NewsItem> getNews(){
        NewsCursorWrapper cursorWrapper = queryNews(null, null);
        Log.d("info", "trying to extract news from database");
        mNews.clear();
        try{
           cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()){
                mNews.add(cursorWrapper.getNewsItem());
                cursorWrapper.moveToNext();
                Log.d("info", "newsItem extracted from database");
            }
        } finally {
            cursorWrapper.close();
        }
        Log.d("info", "returning lit containing " + mNews.size() + " news");
        return mNews;
    }

    public NewsItem getNewsItem(UUID uuid){
        NewsItem item = new NewsItem();
        NewsCursorWrapper cursorWrapper = queryNews(NewsTable.Cols.UUID + " = ?", new String[]{uuid.toString()});
        try{
            cursorWrapper.moveToFirst();
            item = cursorWrapper.getNewsItem();
        } finally {
            cursorWrapper.close();
        }

        return item;
    }

    public void addNewsItem(NewsItem newsItem){
        ContentValues contentValues = getContentValues(newsItem);
        mDatabase.insert(NewsTable.NAME,null,contentValues);
    }

    public void addNews(List<NewsItem> newsItems){
        for (NewsItem newsItem : newsItems)
            addNewsItem(newsItem);
    }

    private NewsList(Context context){
        mNews = new ArrayList<>();
        mContext = context;
        mDatabase = new NewsDbHelper(mContext).getWritableDatabase();
    }

    private ContentValues getContentValues(NewsItem item){
        ContentValues values = new ContentValues();
        values.put(NewsTable.Cols.UUID, item.getId().toString());
        values.put(NewsTable.Cols.TITLE, item.getTitle());
        values.put(NewsTable.Cols.DESCRIPTION, item.getDescription());
        values.put(NewsTable.Cols.LINK, item.getLink());
        values.put(NewsTable.Cols.PUBLICATION_DATE, item.getPubDate().getTime());
        return values;
    }

    private NewsCursorWrapper queryNews(String selection, String[] selectionArgs){
        Cursor cursor = mDatabase.query(
                NewsTable.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return  new NewsCursorWrapper(cursor);
    }


}
