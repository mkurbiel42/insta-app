package com.example.instaappfront.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instaappfront.model.abst.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoListViewModel extends ViewModel {
    private MutableLiveData<List<Photo>> photosMutable;
    private List<Photo> photos;

    public PhotoListViewModel(){
        this.photosMutable = new MutableLiveData<>();
        this.photos = new ArrayList<Photo>();

        this.photosMutable.setValue(this.photos);
    }

    public MutableLiveData<List<Photo>> getObservedPhotos(){
        return this.photosMutable;
    }
}
