package com.sun40.robotumblr;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sun40.robotumblr.R;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.token.AccessToken;
import com.sun40.robotumblr.token.ConsumerToken;

import retrofit.RestAdapter;


/**
 * Created by Alexander Sokol
 * on 25.08.15 17:24.
 */
public final class RoboTumblr {

    private static final String TAG = "RoboTumblr";
    private static final String TOKEN_FILE = "tokens";

    private static final String KEY_ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";
    private static final String KEY_ACCESS_TOKEN_SECRET = "ACCESS_TOKEN_SECRET";
    private static final String KEY_USER = "key_user";

    static RestAdapter.LogLevel LOG_LEVEL = RestAdapter.LogLevel.BASIC;

    private RoboTumblr() {
    }

    public static void setRetrofitLogLevel(RestAdapter.LogLevel level) {
        if (level != null)
            LOG_LEVEL = level;
    }

    public static void setVerbose(boolean verbose){
        L.VERBOSE = verbose;
    }

    public static void clearTokenData(Context context) {
        getTokenPreferences(context).edit().clear().apply();
    }


    public static ConsumerToken getConsumerToken(Context context) {
        String consumerKey = context.getString(R.string.consumer_key);
        String consumerSecret = context.getString(R.string.consumer_secret_key);
        if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret))
            throw new IllegalArgumentException("consumer or consumer secret keys are not defined");
        return new ConsumerToken(consumerKey, consumerSecret);
    }


    public static void setAccessToken(Context context, AccessToken accessToken) {
        getTokenPreferences(context).edit().putString(KEY_ACCESS_TOKEN_KEY, accessToken.getToken()).apply();
        getTokenPreferences(context).edit().putString(KEY_ACCESS_TOKEN_SECRET, accessToken.getSecret()).apply();
    }


    public static AccessToken getAccessToken(Context context) {
        String token = getTokenPreferences(context).getString(KEY_ACCESS_TOKEN_KEY, null);
        String secret = getTokenPreferences(context).getString(KEY_ACCESS_TOKEN_SECRET, null);
        if (token == null || secret == null)
            return null;
        else
            return new AccessToken(token, secret);
    }

    public static boolean isUserLoggedIn(Context context) {
        return getAccessToken(context) != null;
    }

    private static SharedPreferences getTokenPreferences(Context context) {
        return context.getSharedPreferences(TOKEN_FILE, Context.MODE_PRIVATE);
    }

}
