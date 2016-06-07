package com.ivpomazkov.simplerss.database;

/**
 * Created by Ivpomazkov on 07.06.2016.
 */
public class RSSChannelDbSchema {
    public static final class RSSChannelsTable{
        public static final String NAME = "RSSChannelsTable";
        public static final class RSSChannelsColumns{
            public static final String URL = "URL";
            public static final String DESCRIPTION = "Desciption";
            public static final String ISACTIVE = "IsActive";
        }
    }
}
