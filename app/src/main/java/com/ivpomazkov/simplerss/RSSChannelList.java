package com.ivpomazkov.simplerss;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivpomazkov on 01.06.2016.
 */
public class RSSChannelList {
    private static RSSChannelList sRSSChannelList;
    private List<RSSChannel> mChannels;

    public static RSSChannelList get(Context context){
        if (sRSSChannelList == null)
            sRSSChannelList = new RSSChannelList(context);
        return sRSSChannelList;
    }
    public void addChannel(RSSChannel channel){
        mChannels.add(channel);
    }
    public List<RSSChannel> getChannels(){
        return mChannels;
    }

    public List<String> getActiveURLs() {
        List<String> urls = new ArrayList<>();
        for (RSSChannel channel : mChannels){
            if ( channel.isActive() )
                urls.add(channel.getUrl());
        }
        return urls;
    }

    private RSSChannelList(Context context){
        mChannels = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            RSSChannel channel = new RSSChannel();
            String s = "url# " + i;
            channel.setUrl(s);
            s = "description# " + i;
            channel.setDescription(s);
            channel.setActive(i % 2 == 0);
            mChannels.add(channel);
        }

    }

}
