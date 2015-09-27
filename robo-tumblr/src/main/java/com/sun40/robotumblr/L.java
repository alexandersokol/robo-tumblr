package com.sun40.robotumblr;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Alexander Sokol
 * on 11.09.15 13:59.
 */
public final class L {

    static boolean VERBOSE = true;

    private L() {

    }

    public static void d(String tag, String text) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(text)) {
            if (VERBOSE) {
                Log.d(tag, text);
            }
        }
    }


    public static void i(String tag, String text) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(text)) {
            if (VERBOSE) {
                Log.i(tag, text);
            }
        }
    }


    public static void w(String tag, String text) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(text)) {
            if (VERBOSE) {
                Log.w(tag, text);
            }
        }
    }


    public static void e(String tag, String text) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(text)) {
            if (VERBOSE) {
                Log.e(tag, text);
            }
        }
    }


    public static void wtf(String tag, String text) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(text)) {
            if (VERBOSE) {
                Log.wtf(tag, text);
            }
        }
    }


}
