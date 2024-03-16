package com.example.instaappfront.helpers;

import android.content.Context;

import java.util.Random;

public class Util {
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static Random random = new Random();
}
