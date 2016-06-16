package com.ivpomazkov.simplerss.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ivpomazkov.simplerss.RSSChannel;
import com.ivpomazkov.simplerss.database.RSSChannelDbSchema.RSSChannelsTable;

import java.util.UUID;

/**
 * Created by Ivpomazkov on 07.06.2016.
 */
public class RSSChannelCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public RSSChannelCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public RSSChannel getChannel(){
        String UUIDString = getString(getColumnIndex(RSSChannelsTable.RSSChannelsColumns.UUID));
        String URLString = getString(getColumnIndex(RSSChannelsTable.RSSChannelsColumns.URL));
        String DescriptionString = getString(getColumnIndex(RSSChannelsTable.RSSChannelsColumns.DESCRIPTION));
        int isActive = getInt(getColumnIndex(RSSChannelsTable.RSSChannelsColumns.ISACTIVE));

        RSSChannel channel = new RSSChannel(UUID.fromString(UUIDString));
        channel.setUrl(URLString);
        channel.setDescription(DescriptionString);
        channel.setActive(isActive != 0);

        return channel;
    }
}
