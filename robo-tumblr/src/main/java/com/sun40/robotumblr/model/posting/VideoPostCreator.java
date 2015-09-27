package com.sun40.robotumblr.model.posting;

import android.os.Parcel;
import android.text.TextUtils;

import com.sun40.robotumblr.R;
import com.sun40.robotumblr.TumblrExtras;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 10.09.15 16:17.
 */
public class VideoPostCreator extends PostCreator {

    private static final long FILE_SIZE_LIMIT = 100 * 1024 * 1024;

    private String mCaption;
    private String mEmbed;
    private File mData;

    public VideoPostCreator() {
        super(TumblrExtras.Post.VIDEO);
    }


    public void setCaption(String caption) {
        mCaption = caption;
    }


    public void setEmbed(String url) {
        mData = null;
        mEmbed = url;
    }


    public void setData(File data) {
        mEmbed = null;
        mData = data;
    }


    public void setData(String path) {
        mEmbed = null;
        mData = new File(path);
    }


    public String getEmbed() {
        if (TextUtils.isEmpty(mEmbed))
            return null;
        return mEmbed;
    }


    public String getCaption() {
        if (TextUtils.isEmpty(mCaption))
            return null;
        return mCaption;
    }


    public String getDataMimeType() {
        if (mData != null && mData.exists()) {
            return getFileMimeType(mData);
        }
        return null;
    }


    public File getData() {
        if (mData != null && !mData.exists())
            return null;

        return mData;
    }


    @Override
    protected boolean isValid() {
        if (TextUtils.isEmpty(mEmbed) && mData == null)
            return false;

        if (mData != null && !mData.exists() && !mData.isFile())
            return false;

        if (mData != null && mData.length() >= FILE_SIZE_LIMIT)
            return false;


        if (!TextUtils.isEmpty(mEmbed)) {
            try {
                new URL(mEmbed);
            } catch (MalformedURLException e) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected Map<String, String> paramsMap() {
        Map<String, String> params = new HashMap<>();

        if (!TextUtils.isEmpty(mCaption))
            params.put(TumblrExtras.Params.CAPTION, mCaption);

        if (!TextUtils.isEmpty(mEmbed))
            params.put(TumblrExtras.Params.EMBED, mEmbed);

        return params;
    }

    @Override
    protected int invalidationString() {
        if (TextUtils.isEmpty(mEmbed) && mData == null)
            return R.string.creator_audio_error;

        if (mData != null && !mData.exists() && !mData.isFile())
            return R.string.creator_audio_data_exists_error;

        if (mData != null && mData.length() >= FILE_SIZE_LIMIT)
            return R.string.creator_audio_data_size_error;

        if (!TextUtils.isEmpty(mEmbed)) {
            try {
                new URL(mEmbed);
            } catch (MalformedURLException e) {
                return R.string.creator_audio_external_error;
            }
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
        dest.writeString(this.mCaption);
        dest.writeString(this.mEmbed);
        dest.writeSerializable(this.mData);
    }

    protected VideoPostCreator(Parcel in) {
        super(in);
        this.mCaption = in.readString();
        this.mEmbed = in.readString();
        this.mData = (File) in.readSerializable();
    }

    public static final Creator<VideoPostCreator> CREATOR = new Creator<VideoPostCreator>() {
        public VideoPostCreator createFromParcel(Parcel source) {
            return new VideoPostCreator(source);
        }

        public VideoPostCreator[] newArray(int size) {
            return new VideoPostCreator[size];
        }
    };
}
