package com.example.instaappfront.helpers;

import android.content.Context;

import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.model.abst.Profile;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    public static String token = "";
    public static List<Photo> photoList;
    public static List<Photo> userPhotoList;
//    public static String serverAddress = "192.168.119.111:4242";
    public static String serverAddress = "192.168.1.111:4242";
    public static Profile profile;
}
