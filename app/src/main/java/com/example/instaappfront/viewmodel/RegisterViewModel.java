package com.example.instaappfront.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instaappfront.model.RegisterData;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterData> registerDataMutable;
    private RegisterData registerData;

    public RegisterViewModel() {
        this.registerDataMutable = new MutableLiveData<>();
        this.registerData = new RegisterData("", "","", "", "", "");

        this.registerDataMutable.setValue(this.registerData);
    }

    public MutableLiveData<RegisterData> getObservedRegisterData(){
        return this.registerDataMutable;
    }
}
