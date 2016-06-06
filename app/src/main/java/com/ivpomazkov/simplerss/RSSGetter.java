package com.ivpomazkov.simplerss;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivpomazkov on 03.06.2016.
 */
public class RSSGetter extends Service {
    public static final String READY_TO_UPDATE = "update";
    List<String> mURLs;


    public void getNews(){
        List<NewsItem> newsItems = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<NewsItem> ni = NewsList.get(getApplicationContext()).getNews();
                List<String> activeURLs = RSSChannelList.get(getApplicationContext()).getActiveURLs();
                String rssString = "";
                for (String rssUrl : activeURLs) {
                    //get rssString from web
                    NewsItem item = parseRSSString(rssString + rssUrl);
                    ni.add(item);
                }
                Intent intent = new Intent(RSSActivity.UPDATE_ACTION);
                intent.putExtra(RSSGetter.READY_TO_UPDATE,true);
                Log.d("infoService", "intent sent");
                sendBroadcast(intent);
            }
        }).start();

    }

   private NewsItem parseRSSString(String rssString){
        NewsItem item = new NewsItem(rssString);
        return item;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("infoService", "Service->onBind()");
        return null;
    }

    @Override
    public void onCreate(){
        Log.d("infoService", "Service->onCreate()");
        mURLs = RSSChannelList.get(getApplicationContext()).getActiveURLs();
    }

    @Override
    public void onDestroy(){
        Log.d("infoService", "Service->onDestroy()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("infoService", "Service->onStartCommand()");
        getNews();
        return super.onStartCommand(intent, flags, startId);
    }

}
