package com.example.instaappfront.api;

import com.example.instaappfront.model.abst.AppPlace;
import com.example.instaappfront.model.abst.FilterData;
import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.model.retrofit.FilterCall;
import com.example.instaappfront.model.retrofit.PhotoInfo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FilesAPI {
    @GET("/api/photos")
    Call<List<Photo>> getAllPhotos(@Header("Authorization") String token);

    @GET("/api/photos/user")
    Call<List<Photo>> getUserPhotos(@Header("Authorization") String token);

    @Multipart
    @POST("api/photos")
    Call<Photo> uploadFile(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );

    @PATCH("/api/photos")
    Call<Photo> patchPhoto(@Header("Authorization") String token, @Body() PhotoInfo photoInfo);

    @POST("/api/photos/test")
    Call<String> test(@Header("Authorization") String token, @Body() PhotoInfo photoInfo);

    @PATCH("/api/filters")
    Call<Photo> filterPhoto(@Header("Authorization") String token, @Body() FilterCall filterCall);

    @PATCH("/api/photos/test")
    Call<String> testFilter(@Header("Authorization") String token, @Body() FilterCall filterCall);
}
