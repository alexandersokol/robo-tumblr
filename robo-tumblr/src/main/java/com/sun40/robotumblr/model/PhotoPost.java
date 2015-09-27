package com.sun40.robotumblr.model;

import android.os.Parcel;

import java.util.Arrays;

/**
 * Created by Alexander Sokol
 * on 22.09.15 15:16.
 */
public final class PhotoPost extends Post {

    private final Photo[] mPhotos;
    private final String mCaption;
    private final String mImagePermalink;
    private final String mLinkUrl;

    PhotoPost(RawPost raw) {
        super(raw);
        mPhotos = raw.photos;
        mCaption = raw.caption;
        mImagePermalink = raw.image_permalink;
        mLinkUrl = raw.link_url;
    }


    public Photo[] getPhotos() {
        return mPhotos;
    }

    public String getCaption() {
        return mCaption;
    }

    public String getImagePermalink() {
        return mImagePermalink;
    }

    public String getLinkUrl() {
        return mLinkUrl;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "PhotoPost{" +
                super.toString() +
                "\tmPhotos=" + Arrays.toString(mPhotos) + separator +
                "\tmCaption='" + mCaption + '\'' + separator +
                "\tmImagePermalink='" + mImagePermalink + '\'' + separator +
                "\tmLinkUrl='" + mLinkUrl + '\'' + separator +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelableArray(this.mPhotos, 0);
        dest.writeString(this.mCaption);
        dest.writeString(this.mImagePermalink);
        dest.writeString(mLinkUrl);
    }


    protected PhotoPost(Parcel in) {
        super(in);
        this.mPhotos = (Photo[]) in.readParcelableArray(Photo.class.getClassLoader());
        this.mCaption = in.readString();
        this.mImagePermalink = in.readString();
        this.mLinkUrl = in.readString();
    }


    public static final Creator<PhotoPost> CREATOR = new Creator<PhotoPost>() {
        public PhotoPost createFromParcel(Parcel source) {
            return new PhotoPost(source);
        }

        public PhotoPost[] newArray(int size) {
            return new PhotoPost[size];
        }
    };
}
