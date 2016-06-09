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
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 01.06.2016.
 */
public class NewsList {
    private static NewsList sNewsList;
    private TreeSet<NewsItem> mNews;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static synchronized NewsList get(Context context){
        if (sNewsList == null)
            sNewsList = new NewsList(context);
         return sNewsList;
    }

    public List<NewsItem> getNews(boolean fromDb){
        if (fromDb){
            NewsCursorWrapper cursorWrapper = queryNews(null, null);
            Log.d("info", "trying to extract news from database");
            try{
                cursorWrapper.moveToFirst();
                while(!cursorWrapper.isAfterLast()){
                    mNews.add(cursorWrapper.getNewsItem());
                    cursorWrapper.moveToNext();
                    }
            } finally {
                cursorWrapper.close();
            }
            Log.d("info", "--" + mNews.size() + " news extracted from DB");
        }
        List<NewsItem> newsItems = new ArrayList<>(mNews);
        Log.d("info", "--" + mNews.size() + " news extracted from memory");
        return newsItems;
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

    public void addNews(List<NewsItem> newsItems, boolean toDb){
        String receiver = toDb ? "DB" : "memory";
        Log.d("info", "adding " + newsItems.size() + " news to" + receiver);

        for (NewsItem newsItem : newsItems)
            addNewsItem(newsItem, toDb);
    }

    private void addNewsItem(NewsItem newsItem, boolean toDb){
        if (toDb) {
            ContentValues contentValues = getContentValues(newsItem);
            mDatabase.insert(NewsTable.NAME, null, contentValues);
        } else {
            mNews.add(newsItem);
        }

    }

    private NewsList(Context context){
        mNews = new TreeSet<>();
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
