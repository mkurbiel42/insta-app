package com.example.instaappfront.model.abst;

import java.util.List;

public class Photo {
    private long id;
    private String album;
    private String originalName;
    private String url;
    private String originalUrl;
    private String lastChange;
    private List<PhotoHistoryEntry> history;
    private List<Tag> tags;
    private AppPlace place;
    private boolean isVideo;

    public Photo() {
    }

    public Photo(long id, String album, String originalName, String url, String originalUrl, String lastChange, List<PhotoHistoryEntry> history, List<Tag> tags, AppPlace place, boolean isVideo) {
        this.id = id;
        this.album = album;
        this.originalName = originalName;
        this.url = url;
        this.originalUrl = originalUrl;
        this.lastChange = lastChange;
        this.history = history;
        this.tags = tags;
        this.place = place;
        this.isVideo = isVideo;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastChange() {
        return lastChange;
    }

    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
    }

    public List<PhotoHistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(List<PhotoHistoryEntry> history) {
        this.history = history;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public AppPlace getPlace() {
        return place;
    }

    public void setPlace(AppPlace place) {
        this.place = place;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", album='" + album + '\'' +
                ", originalName='" + originalName + '\'' +
                ", url='" + url + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                ", lastChange='" + lastChange + '\'' +
                ", history=" + history +
                ", tags=" + tags +
                ", place=" + place +
                ", isVideo}" + isVideo;
    }
}
