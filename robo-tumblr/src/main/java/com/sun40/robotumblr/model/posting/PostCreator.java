package com.sun40.robotumblr.model.posting;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;


import com.sun40.robotumblr.R;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.RawPost;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 09.09.15 10:54.
 */
public abstract class PostCreator implements Parcelable {

    private long mId = -1;
    @TumblrExtras.PostType
    private String mPostType;
    @TumblrExtras.FilterType
    private String mPostState;
    private String mTags;
    private String mTweet;
    private String mDate;
    private String mFormat;
    private String mSlug;


    public PostCreator(@NonNull @TumblrExtras.PostType String postType) {
        if (TextUtils.isEmpty(postType))
            throw new IllegalArgumentException("post type is empty");
        mPostType = postType;
    }


    public Long getId() {
        if (mId == -1)
            return null;
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getPostType() {
        if (TextUtils.isEmpty(mPostType))
            return null;
        return mPostType;
    }

    public String getPostState() {
        if (TextUtils.isEmpty(mPostState))
            return null;
        return mPostState;
    }

    public void setPostState(@TumblrExtras.StateType String postState) {
        this.mPostState = postState;
    }

    public String getTags() {
        if (TextUtils.isEmpty(mTags))
            return null;
        return mTags;
    }

    public void setTags(String tags) {
        this.mTags = tags;
    }

    public String getTweet() {
        if (TextUtils.isEmpty(mTweet))
            return null;
        return mTweet;
    }

    public void setTweet(String tweet) {
        this.mTweet = tweet;
    }

    public String getDate() {
        if (TextUtils.isEmpty(mDate))
            return null;
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getFormat() {
        if (TextUtils.isEmpty(mFormat))
            return null;
        return mFormat;
    }

    public void setFormat(String format) {
        this.mFormat = format;
    }

    public String getSlug() {
        if (TextUtils.isEmpty(mSlug))
            return null;
        return mSlug;
    }

    public void setSlug(String slug) {
        this.mSlug = slug;
    }


    public boolean valid() {
        return !TextUtils.isEmpty(mPostType) && isValid();
    }

    public String getInvalidationString(Context context) {
        if (TextUtils.isEmpty(mPostType))
            return context.getString(R.string.creator_type_error);
        else {
            int error = invalidationString();
            if (error == 0)
                return null;
            else {
                return context.getString(invalidationString());
            }
        }
    }


    public Map<String, String> params() {
        Map<String, String> paramsMap = new HashMap<>();

        if (getId() != null)
            paramsMap.put(TumblrExtras.Params.ID, Long.toString(mId));

        if (!TextUtils.isEmpty(mPostType))
            paramsMap.put(TumblrExtras.Params.TYPE, mPostType);

        if (!TextUtils.isEmpty(mPostState))
            paramsMap.put(TumblrExtras.Params.STATE, mPostState);

        if (!TextUtils.isEmpty(mTags))
            paramsMap.put(TumblrExtras.Params.TAGS, mTags);

        if (!TextUtils.isEmpty(mTweet))
            paramsMap.put(TumblrExtras.Params.TWEET, mTweet);

        if (!TextUtils.isEmpty(mDate))
            paramsMap.put(TumblrExtras.Params.DATE, mDate);

        if (!TextUtils.isEmpty(mFormat))
            paramsMap.put(TumblrExtras.Params.FORMAT, mFormat);

        if (!TextUtils.isEmpty(mSlug))
            paramsMap.put(TumblrExtras.Params.SLUG, mSlug);

        paramsMap.putAll(paramsMap());
        return paramsMap;
    }

    protected abstract boolean isValid();

    protected abstract Map<String, String> paramsMap();

    @StringRes
    protected abstract int invalidationString();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mPostType);
        dest.writeString(this.mPostState);
        dest.writeString(this.mTags);
        dest.writeString(this.mTweet);
        dest.writeString(this.mDate);
        dest.writeString(this.mFormat);
        dest.writeString(this.mSlug);
    }

    protected PostCreator(Parcel in) {
        this.mId = in.readLong();
        this.mPostType = in.readString();
        this.mPostState = in.readString();
        this.mTags = in.readString();
        this.mTweet = in.readString();
        this.mDate = in.readString();
        this.mFormat = in.readString();
        this.mSlug = in.readString();
    }

    String getFileMimeType(File file) {
        if (file != null && file.exists() && file.isFile()) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
            if (!TextUtils.isEmpty(extension))
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            else {
                if (file.getAbsolutePath().endsWith(".mp3")) {
                    return "audio/mpeg3";
                }
            }
        }
        return null;
    }
}
