package com.example.instaappfront.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class LoginData extends BaseObservable {
    @Bindable
    private String email;
    @Bindable
    private String password;

    public LoginData(String email, String password) {
        this.setAll(email, password);
    }

    public void setAll(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
