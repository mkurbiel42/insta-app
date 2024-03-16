package com.example.instaappfront.model.retrofit;

import com.example.instaappfront.model.abst.AppPlace;

import java.util.List;

public class PhotoInfo {
    private AppPlace place;
    private List<String> tags;
    private long id;

    public PhotoInfo() {
    }

    public PhotoInfo(long id, AppPlace place, List<String> tags) {
        this.id = id;
        this.place = place;
        this.tags = tags;
    }

    public AppPlace getPlace() {
        return place;
    }

    public void setPlace(AppPlace place) {
        this.place = place;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
