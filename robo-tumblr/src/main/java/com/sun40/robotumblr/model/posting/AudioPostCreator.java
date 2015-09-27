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
 * on 10.09.15 15:27.
 */
public class AudioPostCreator extends PostCreator {

    private static final long FILE_SIZE_LIMIT = 10 * 1024 * 1024;

    private String mCaption;
    private String mExternalUrl;
    private File mData;

    public AudioPostCreator() {
        super(TumblrExtras.Post.AUDIO);
    }


    public String getCaption(){
        if(TextUtils.isEmpty(mCaption))
            return null;
        return mCaption;
    }


    public String getExternalUrl(){
        if(TextUtils.isEmpty(mExternalUrl))
            return null;
        return mExternalUrl;
    }


    public File getDataFile(){
        return mData;
    }


    public void setCaption(String caption){
        mCaption = caption;
    }


    public void setExternalUrl(String externalUrl){
        mData = null;
        mExternalUrl = externalUrl;
    }


    public void setData(File dataFile){
        mData = dataFile;
    }


    public void setData(String path){
        mExternalUrl = null;
        mData = new File(path);
    }


    public String getDataMimeType() {
        if (mData != null && mData.exists()) {
            return getFileMimeType(mData);
        }
        return null;
    }


    @Override
    protected boolean isValid() {
        if(TextUtils.isEmpty(mExternalUrl) && mData == null)
            return false;

        if(mData != null && !mData.exists() && !mData.isFile())
            return false;

        if(mData != null && mData.length() >= FILE_SIZE_LIMIT)
            return false;


        if(!TextUtils.isEmpty(mExternalUrl)) {
            try {
                new URL(mExternalUrl);
            } catch (MalformedURLException e) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected Map<String, String> paramsMap() {
        Map<String, String> params = new HashMap<>();

        if(!TextUtils.isEmpty(mCaption))
            params.put(TumblrExtras.Params.CAPTION, mCaption);

        if(!TextUtils.isEmpty(mExternalUrl))
            params.put(TumblrExtras.Params.EXTERNAL_URL, mExternalUrl);

        return params;
    }

    @Override
    protected int invalidationString() {
        if(TextUtils.isEmpty(mExternalUrl) && mData == null)
            return R.string.creator_audio_error;

        if(mData != null && !mData.exists() && !mData.isFile())
            return R.string.creator_audio_data_exists_error;

        if(mData != null && mData.length() >= FILE_SIZE_LIMIT)
            return R.string.creator_audio_data_size_error;

        if(!TextUtils.isEmpty(mExternalUrl)) {
            try {
                new URL(mExternalUrl);
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
        dest.writeString(this.mExternalUrl);
        dest.writeSerializable(this.mData);
    }

    protected AudioPostCreator(Parcel in) {
        super(in);
        this.mCaption = in.readString();
        this.mExternalUrl = in.readString();
        this.mData = (File) in.readSerializable();
    }

    public static final Creator<AudioPostCreator> CREATOR = new Creator<AudioPostCreator>() {
        public AudioPostCreator createFromParcel(Parcel source) {
            return new AudioPostCreator(source);
        }

        public AudioPostCreator[] newArray(int size) {
            return new AudioPostCreator[size];
        }
    };
}
