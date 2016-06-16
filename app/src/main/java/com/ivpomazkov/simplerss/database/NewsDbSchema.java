package com.ivpomazkov.simplerss.database;

/**
 * Created by Ivpomazkov on 08.06.2016.
 */
public class NewsDbSchema
{
    public static final class NewsTable{
        public static final String NAME = "NewsTable";
        public static final class Cols{
            public static final String UUID = "UUID";
            public static final String TITLE = "Title";
            public static final String DESCRIPTION = "Description";
            public static final String LINK = "Link";
            public static final String IMAGE_LINK = "ImageLink";
            public static final String PUBLICATION_DATE = "PublicationDate";
        }
    }
}
