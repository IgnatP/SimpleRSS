package com.ivpomazkov.simplerss;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 01.06.2016.
 */
public class NewsItem {
    private UUID id;
    private String title;
    private String description;
    private String link;
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
        link = "http://news.tut.by/economics/498655.html?utm_campaign=news-feed&#x26;utm_medium=rss&#x26;utm_source=rss-news";
        pubDate = new Date(2015,12,12);
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

}
