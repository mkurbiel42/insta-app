package com.example.instaappfront.helpers;

import com.example.instaappfront.api.FilesAPI;
import com.example.instaappfront.api.TagsAPI;
import com.example.instaappfront.api.UserAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitST {
    private static Retrofit instance;
    private static HttpLoggingInterceptor interceptor;
    private static OkHttpClient client;
    private static UserAPI userAPI;
    private static FilesAPI filesAPI;
    private static TagsAPI tagsAPI;

    public static Retrofit getInstance(){
        if(instance == null){
            interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            instance = new Retrofit.Builder()
                    .baseUrl("http://" + GlobalData.serverAddress)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return instance;
    }

    public static UserAPI getUserAPI(){
        if(userAPI == null){
            userAPI = getInstance().create(UserAPI.class);
        }

        return userAPI;
    }

    public static FilesAPI getFilesAPI(){
        if(filesAPI == null){
            filesAPI = getInstance().create(FilesAPI.class);
        }

        return filesAPI;
    }

    public static TagsAPI getTagsAPI(){
        if(tagsAPI == null){
            tagsAPI = getInstance().create(TagsAPI.class);
        }

        return tagsAPI;
    }
}
