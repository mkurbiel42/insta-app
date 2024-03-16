package com.example.instaappfront.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.instaappfront.model.LoginData;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginData> loginDataMutable;
    private LoginData loginData;

    public LoginViewModel() {
        this.loginDataMutable = new MutableLiveData<>();
        this.loginData = new LoginData("", "");

        this.loginDataMutable.setValue(this.loginData);
    }

    public MutableLiveData<LoginData> getObservedLoginData(){
        return this.loginDataMutable;
    };
}
