package com.ivpomazkov.simplerss;

import android.content.Context;
import android.util.Log;

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

    public static NewsList get(Context context){
        if (sNewsList == null)
            sNewsList = new NewsList(context);
         return sNewsList;
    }

    public List<NewsItem> getNews(){
        return mNews;
    }

    public NewsItem getNewsItem(UUID uuid){
        for (NewsItem ni : mNews)
            if (ni.getId().equals(uuid))
                return ni;
        return null;
    }

    public void addNews(NewsItem newsItem){
        mNews.add(newsItem);
    }

    private NewsList(Context context){
        mNews = new ArrayList<>();
       /* for (int i = 0; i < 100; i++){
            NewsItem nItem = new NewsItem();
            nItem.setTitle("Title " + i);
            nItem.setDescription("Desription " + i);
            nItem.setLink("Link " + i);
            nItem.setPubDate(new Date(190000000 + i));
            mNews.add(nItem);
        }*/
    }



}
