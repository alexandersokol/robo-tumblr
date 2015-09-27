package com.sun40.robotumblr.model;

import android.os.Parcel;

import java.util.Arrays;

/**
 * Created by Alexander Sokol
 * on 22.09.15 14:50.
 */
public final class LinkPost extends Post {

    private final String mTitle;
    private final Photo[] mPhotos;
    private final String mUrl;
    private final String mAuthor;
    private final String mExperpt;
    private final String mPublisher;
    private final String mDescription;

    LinkPost(RawPost raw) {
        super(raw);
        mTitle = raw.title;
        mPhotos = raw.photos;
        mUrl = raw.url;
        mAuthor = raw.author;
        mExperpt = raw.excerpt;
        mPublisher = raw.publisher;
        mDescription = raw.description;
    }

    public String getTitle() {
        return mTitle;
    }

    public Photo[] getPhotos() {
        return mPhotos;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getExperpt() {
        return mExperpt;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");

        return "LinkPost{" + separator +
                super.toString() +
                "\tmTitle='" + mTitle + '\'' + separator +
                "\tmPhotos=" + Arrays.toString(mPhotos) + separator +
                "\tmUrl='" + mUrl + '\'' + separator +
                "\tmAuthor='" + mAuthor + '\'' + separator +
                "\tmExperpt='" + mExperpt + '\'' + separator +
                "\tmPublisher='" + mPublisher + '\'' + separator +
                "\tmDescription='" + mDescription + '\'' + separator +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mTitle);
        dest.writeParcelableArray(this.mPhotos, 0);
        dest.writeString(this.mUrl);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mExperpt);
        dest.writeString(this.mPublisher);
        dest.writeString(this.mDescription);
    }

    protected LinkPost(Parcel in) {
        super(in);
        this.mTitle = in.readString();
        this.mPhotos = (Photo[]) in.readParcelableArray(Photo.class.getClassLoader());
        this.mUrl = in.readString();
        this.mAuthor = in.readString();
        this.mExperpt = in.readString();
        this.mPublisher = in.readString();
        this.mDescription = in.readString();
    }

    public static final Creator<LinkPost> CREATOR = new Creator<LinkPost>() {
        public LinkPost createFromParcel(Parcel source) {
            return new LinkPost(source);
        }

        public LinkPost[] newArray(int size) {
            return new LinkPost[size];
        }
    };
}
