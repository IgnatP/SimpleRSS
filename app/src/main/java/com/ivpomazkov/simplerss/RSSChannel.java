package com.ivpomazkov.simplerss;

import java.util.UUID;

/**
 * Created by Ivpomazkov on 02.06.2016.
 */
public class RSSChannel {
    private UUID mUUID;
    private String mUrl;
    private String mDescription;
    private Boolean mIsActive;

    public RSSChannel(){
        mUUID = UUID.randomUUID();
    }
    public RSSChannel(UUID uuid){
        mUUID = uuid;
    }
    @Override
    public String toString(){
        return "Description: " + getDescription() + ", Url: " + getUrl();
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Boolean isActive() {
        return mIsActive;
    }

    public void setActive(Boolean active) {
        mIsActive = active;
    }

    public UUID getUUID() {
        return mUUID;
    }
}
