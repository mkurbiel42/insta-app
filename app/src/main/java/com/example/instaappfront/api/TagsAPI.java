package com.example.instaappfront.api;

import com.example.instaappfront.model.abst.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TagsAPI {
    @GET("/api/tags/raw")
    Call<List<String>> getTags(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/tags")
    Call<Tag> postNewTag(@Header("Authorization") String token,
                         @Field("name") String name,
                         @Field("popularity") int popularity);
}
