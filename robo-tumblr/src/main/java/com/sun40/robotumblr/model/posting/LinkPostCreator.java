package com.sun40.robotumblr.model.posting;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;

import com.sun40.robotumblr.R;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.RawPost;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 10.09.15 13:59.
 */
public class LinkPostCreator extends PostCreator {

    private String mTitle;
    private String mUrl;
    private String mDescription;

    public LinkPostCreator() {
        super(TumblrExtras.Post.LINK);
    }


    public String getTitle() {
        if (TextUtils.isEmpty(mTitle))
            return null;
        else
            return mTitle;
    }


    public String getUrl() {
        if (TextUtils.isEmpty(mUrl))
            return null;
        try {
            new URL(mUrl);
        } catch (MalformedURLException e) {
            return null;
        }
        return mUrl;
    }


    public String getDescription() {
        if (TextUtils.isEmpty(mDescription))
            return null;
        return mDescription;
    }


    public void setTitle(String title) {
        mTitle = title;
    }


    public void setUrl(String url) {
        mUrl = url;
    }


    public void setDescription(String description) {
        mDescription = description;
    }


    @Override
    protected boolean isValid() {
        if (TextUtils.isEmpty(mUrl))
            return false;

        try {
            new URL(mUrl);
        } catch (MalformedURLException e) {
            return false;
        }

        return true;
    }

    @Override
    protected Map<String, String> paramsMap() {
        Map<String, String> params = new HashMap<>();

        if (!TextUtils.isEmpty(mTitle))
            params.put(TumblrExtras.Params.TITLE, Html.escapeHtml(mTitle));

        if (!TextUtils.isEmpty(mUrl))
            params.put(TumblrExtras.Params.URL, mUrl);

        if (!TextUtils.isEmpty(mDescription))
            params.put(TumblrExtras.Params.DESCRIPTION, mDescription);

        return params;
    }

    @Override
    protected int invalidationString() {
        if (TextUtils.isEmpty(mUrl))
            return R.string.creator_link_url_empty;

        try {
            new URL(mUrl);
        } catch (MalformedURLException e) {
            return R.string.creator_link_url_error;
        }

        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mTitle);
        dest.writeString(this.mUrl);
        dest.writeString(this.mDescription);
    }

    protected LinkPostCreator(Parcel in) {
        super(in);
        this.mTitle = in.readString();
        this.mUrl = in.readString();
        this.mDescription = in.readString();
    }

    public static final Parcelable.Creator<LinkPostCreator> CREATOR = new Parcelable.Creator<LinkPostCreator>() {
        public LinkPostCreator createFromParcel(Parcel source) {
            return new LinkPostCreator(source);
        }

        public LinkPostCreator[] newArray(int size) {
            return new LinkPostCreator[size];
        }
    };
}
