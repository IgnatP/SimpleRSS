package com.ivpomazkov.simplerss;

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

    @Override
    public boolean equals(Object o){
        return o instanceof NewsItem &&
                title.equals(((NewsItem)o).getTitle()) &&
                pubDate.equals( ((NewsItem)o).getPubDate() );
    }

    @Override
    public int compareTo(NewsItem another) {
        if ( title.equals(another.getTitle()) && pubDate.equals(another.getPubDate()))
            return 0;
        else
            return another.getPubDate().compareTo(pubDate);
    }
}
