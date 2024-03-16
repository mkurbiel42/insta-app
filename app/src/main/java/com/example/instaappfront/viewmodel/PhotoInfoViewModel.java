package com.example.instaappfront.viewmodel;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.LoginData;
import com.example.instaappfront.model.abst.AppPlace;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoInfoViewModel extends ViewModel {
    private MutableLiveData<List<String>> mutableTagsList;
    private List<String> tagsList;
    private MutableLiveData<AppPlace> mutablePlace;
    private AppPlace place;

    public PhotoInfoViewModel() {
        this.mutableTagsList = new MutableLiveData<>();
        this.mutablePlace = new MutableLiveData<>();
    }

    public MutableLiveData<List<String>> getObservedTagsList(){
        return this.mutableTagsList;
    };

    public MutableLiveData<AppPlace> getObservedPlace() {
        return this.mutablePlace;
    }

    public void setObservedTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
        this.mutableTagsList.setValue(this.tagsList);
    }

    public void setObservedPlace(AppPlace place){
        this.place = place;
        this.mutablePlace.setValue(this.place);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFromTagsList(String tagName){
        this.tagsList = tagsList.stream().filter(s -> s != tagName).collect(Collectors.toList());
        this.mutableTagsList.setValue(tagsList);
    }

    public void addToTagsList(String tagName){
        if(!this.tagsList.contains(tagName)){
            this.tagsList.add(tagName);
            this.mutableTagsList.setValue(this.tagsList);
        }
    }
}
