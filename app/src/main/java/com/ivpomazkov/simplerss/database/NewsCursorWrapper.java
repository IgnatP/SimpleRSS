package com.ivpomazkov.simplerss.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ivpomazkov.simplerss.NewsItem;
import com.ivpomazkov.simplerss.database.NewsDbSchema.NewsTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 08.06.2016.
 */
public class NewsCursorWrapper extends CursorWrapper{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public NewsCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public NewsItem getNewsItem(){
        String uuidString = getString(getColumnIndex(NewsTable.Cols.UUID));
        String titleString = getString(getColumnIndex(NewsTable.Cols.TITLE));
        String descriptionString = getString(getColumnIndex(NewsTable.Cols.DESCRIPTION));
        String urlString = getString(getColumnIndex(NewsTable.Cols.LINK));
        String imageString = getString(getColumnIndex(NewsTable.Cols.IMAGE_LINK));
        long pubDateLong = getLong(getColumnIndex(NewsTable.Cols.PUBLICATION_DATE));

        NewsItem item = new NewsItem(UUID.fromString(uuidString));
        item.setTitle(titleString);
        item.setDescription(descriptionString);
        item.setLink(urlString);
        item.setImageLink(imageString);
        item.setPubDate(new Date(pubDateLong));
        return item;
    }
}
