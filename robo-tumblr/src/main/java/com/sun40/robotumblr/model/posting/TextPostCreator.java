package com.sun40.robotumblr.model.posting;

import android.os.Parcel;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.TextUtils;

import com.sun40.robotumblr.R;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.RawPost;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 09.09.15 11:55.
 */
public class TextPostCreator extends PostCreator {

    private String mTitle;
    private String mBody;

    public TextPostCreator() {
        super(TumblrExtras.Post.TEXT);
    }

    public String getTitle() {
        if (TextUtils.isEmpty(mTitle))
            return null;
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getBody() {
        if (TextUtils.isEmpty(mBody))
            return null;
        return mBody;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    @Override
    protected boolean isValid() {
        return !TextUtils.isEmpty(mBody);
    }

    @Override
    protected Map<String, String> paramsMap() {

        Map<String, String> paramsMap = new HashMap<>();

        if (!TextUtils.isEmpty(mTitle))
            paramsMap.put(TumblrExtras.Params.TITLE, Html.escapeHtml(mTitle));

        if (!TextUtils.isEmpty(mBody))
            paramsMap.put(TumblrExtras.Params.BODY, mBody);

        return paramsMap;
    }

    @Override
    @StringRes
    protected int invalidationString() {
        if (TextUtils.isEmpty(mBody))
            return R.string.creator_text_body_error;
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mBody);
        dest.writeString(this.mTitle);
    }

    protected TextPostCreator(Parcel in) {
        super(in);
        this.mBody = in.readString();
        this.mTitle = in.readString();
    }

    public static final Creator<TextPostCreator> CREATOR = new Creator<TextPostCreator>() {
        public TextPostCreator createFromParcel(Parcel source) {
            return new TextPostCreator(source);
        }

        public TextPostCreator[] newArray(int size) {
            return new TextPostCreator[size];
        }
    };
}
