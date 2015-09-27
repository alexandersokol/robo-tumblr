package com.robotumblr.sample.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sun40.robotumblr.model.User;

/**
 * Created by sokol.a
 * on 9/17/2015.
 */
public class StorageUtils {

    private static final String USER_FILE = "user";
    private static final String KEY_USER = "USER";

    public static void clearCurrentUser(Context context) {
        getUserPreferences(context).edit().clear().apply();
    }

    public static void setCurrentUser(Context context, User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        getUserPreferences(context).edit().putString(KEY_USER, json).apply();
    }


    public static User getCurrentUser(Context context) {
        String json = getUserPreferences(context).getString(KEY_USER, null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, User.class);
        }
        return null;
    }

    private static SharedPreferences getUserPreferences(Context context) {
        return context.getSharedPreferences(USER_FILE, Context.MODE_PRIVATE);
    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
