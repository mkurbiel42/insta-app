package com.example.instaappfront.helpers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentsST {
    public static void replaceFragment(FragmentManager fm, int id, Fragment fragment){
        fm.beginTransaction()
                .replace(id, fragment)
                .commit();
    }
}
