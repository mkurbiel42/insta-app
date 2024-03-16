package com.example.instaappfront.api;


import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.model.abst.Profile;
import com.example.instaappfront.model.retrofit.LoginResponse;
import com.example.instaappfront.model.retrofit.RegisterResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserAPI {
    @FormUrlEncoded
    @POST("/api/users/register")
    Call<RegisterResponse> registerUser(
            @Field("email") String email,
            @Field("username") String username,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("/api/users/login")
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("/api/profile")
    Call<Profile> getProfile(
            @Header("Authorization") String token
    );

    @GET("/api/profile/username/{id}")
    Call<String> getUsername(@Path("id") String id, @Header("Authorization") String token);

    @Multipart
    @POST("/api/profile")
    Call<Profile> uploadPfp(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @PATCH("/api/profile")
    Call<Profile> editProfile(
            @Field("username") String username,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @PATCH("/api/profile")
    Call<Profile> changePassword(
            @Field("oldPassword") String username,
            @Field("password") String firstName,
            @Header("Authorization") String token
    );
}
