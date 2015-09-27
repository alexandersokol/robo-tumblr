package com.sun40.robotumblr.model.posting;

import android.os.Parcel;
import android.text.TextUtils;

import com.sun40.robotumblr.R;
import com.sun40.robotumblr.TumblrExtras;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 09.09.15 12:17.
 */
public class PhotoPostCreator extends PostCreator {

    private String mCaption;
    private String mLink;
    private String mSource;
    private List<File> mData = new ArrayList<>();


    public PhotoPostCreator() {
        super(TumblrExtras.Post.PHOTO);
    }


    public String getCaption() {
        if (TextUtils.isEmpty(mCaption))
            return null;
        return mCaption;
    }


    public void setCaption(String caption) {
        mCaption = caption;
    }


    public String getLink() {
        if (TextUtils.isEmpty(mLink))
            return null;
        return mLink;
    }


    public void setLink(String link) {
        mLink = link;
    }


    public void setSource(String source) {
        if (!TextUtils.isEmpty(source)) {
            mData.clear();
            mSource = source;
        }
    }


    public void addFile(File file) {
        if (file != null && file.exists()) {
            mSource = null;
            mData.add(file);
        }
    }


    public void removeFile(File file) {
        if (mData.contains(file))
            mData.remove(file);
    }


    public String getSource() {
        if (TextUtils.isEmpty(mSource))
            return null;
        return mSource;
    }


    public void removeFile(int position) {
        if (position >= 0 && position < mData.size())
            mData.remove(position);
    }


    public void clearFiles() {
        mData.clear();
    }


    public int getDataSize() {
        return mData.size();
    }


    public String getMimeTypeAt(int position) {
        return getFileMimeType(getDataFileAt(position));
    }


    public File getDataFileAt(int position) {
        if (position >= 0 && position < mData.size())
            return mData.get(position);
        return null;
    }


    @Override
    protected boolean isValid() {
        return !(TextUtils.isEmpty(mSource) && mData.size() == 0);
    }


    @Override
    protected Map<String, String> paramsMap() {

        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(mCaption))
            params.put(TumblrExtras.Params.CAPTION, mCaption);


        if (!TextUtils.isEmpty(mLink))
            params.put(TumblrExtras.Params.LINK, mLink);

        if (!TextUtils.isEmpty(mSource)) {
            params.put(TumblrExtras.Params.SOURCE, mSource);
        }

        return params;
    }


    @Override
    protected int invalidationString() {
        if (!isValid())
            return R.string.creator_photo_source_error;
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
        dest.writeString(this.mLink);
        dest.writeString(this.mSource);
        dest.writeList(this.mData);
    }

    protected PhotoPostCreator(Parcel in) {
        super(in);
        this.mCaption = in.readString();
        this.mLink = in.readString();
        this.mSource = in.readString();
        this.mData = new ArrayList<File>();
        in.readList(this.mData, List.class.getClassLoader());
    }

    public static final Creator<PhotoPostCreator> CREATOR = new Creator<PhotoPostCreator>() {
        public PhotoPostCreator createFromParcel(Parcel source) {
            return new PhotoPostCreator(source);
        }

        public PhotoPostCreator[] newArray(int size) {
            return new PhotoPostCreator[size];
        }
    };
}
