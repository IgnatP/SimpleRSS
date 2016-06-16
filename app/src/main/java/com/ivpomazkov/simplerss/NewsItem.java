package com.ivpomazkov.simplerss;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 01.06.2016.
 */
public class NewsItem implements Comparable<NewsItem>{
    private UUID id;
    private String title;
    private String description;
    private String link;
    private String imageLink;
    private Date pubDate;

    public NewsItem(){
        this(UUID.randomUUID());
    }

    public NewsItem(UUID uuid){
        id = uuid;
        pubDate = new Date();
    }

    public NewsItem(String descr){
        id = UUID.randomUUID();
        title = "title";
        description = descr;
        link = "link";
        pubDate = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public UUID getId() {
        return id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public int compareTo(NewsItem another) {
        Log.d("info", "comparing " + title + "with " + another.getTitle());
        Log.d("info", "comparing " + pubDate + "with " + another.getPubDate());
        int titleCompare = title.compareTo(another.getTitle());
        int dateCompare = another.getPubDate().compareTo(pubDate);
        if (( titleCompare == dateCompare) && (titleCompare == 0) )
            return 0;
        else
            if  ( dateCompare != 0)
                return  dateCompare;
            else return titleCompare;
       //return title.compareTo(another.getTitle());
    }
}
