package com.example.instaappfront.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class RegisterData extends BaseObservable {
    @Bindable
    private String email;
    @Bindable
    private String password;
    @Bindable
    private String confirmPassword;
    @Bindable
    private String firstName;
    @Bindable
    private String lastName;

    @Bindable
    private String username;

    public RegisterData(String email, String username, String password, String confirmPassword, String firstName, String lastName) {
        this.setAll(email, username, password, confirmPassword, firstName, lastName);
    }

    public void setAll(String email, String username, String password, String confirmPassword, String firstName, String lastName){
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
