package com.sun40.robotumblr.model.posting;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.sun40.robotumblr.R;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.RawPost;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 10.09.15 17:41.
 */
public class ReblogPostCreator extends PostCreator {

    private String mReblogKey;
    private String mComment;

    public ReblogPostCreator(@NonNull @TumblrExtras.PostType String postType, long id, @NonNull String reblogKey) {
        super(postType);
        if (id <= 0)
            throw new IllegalArgumentException("wrong post id");
        if (TextUtils.isEmpty(reblogKey))
            throw new IllegalArgumentException("reblog key is empty");

        setId(id);
        mReblogKey = reblogKey;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getComment() {
        if (TextUtils.isEmpty(mComment))
            return null;
        return mComment;
    }

    public String getReblogKey() {
        return mReblogKey;
    }


    @Override
    protected boolean isValid() {
        return !TextUtils.isEmpty(mReblogKey) && getId() != null;
    }


    @Override
    protected Map<String, String> paramsMap() {

        Map<String, String> params = new HashMap<>();

        if (!TextUtils.isEmpty(mReblogKey))
            params.put(TumblrExtras.Params.REBLOG_KEY, mReblogKey);

        if (!TextUtils.isEmpty(mComment))
            params.put(TumblrExtras.Params.COMMENT, mComment);

        return params;
    }


    @Override
    protected int invalidationString() {
        if (TextUtils.isEmpty(mReblogKey))
            return R.string.creator_reblog_key_error;
        if (getId() == null)
            return R.string.creator_reblog_id_error;
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mReblogKey);
        dest.writeString(this.mComment);
    }

    protected ReblogPostCreator(Parcel in) {
        super(in);
        this.mReblogKey = in.readString();
        this.mComment = in.readString();
    }

    public static final Creator<ReblogPostCreator> CREATOR = new Creator<ReblogPostCreator>() {
        public ReblogPostCreator createFromParcel(Parcel source) {
            return new ReblogPostCreator(source);
        }

        public ReblogPostCreator[] newArray(int size) {
            return new ReblogPostCreator[size];
        }
    };
}
