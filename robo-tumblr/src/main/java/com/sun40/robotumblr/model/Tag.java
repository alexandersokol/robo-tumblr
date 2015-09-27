package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 16.09.15 16:57.
 */
public class Tag implements Parcelable {

    public String tag;
    public String thumb_url;
    public boolean featured;
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag);
        dest.writeString(this.thumb_url);
        dest.writeByte(featured ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
    }

    public Tag() {
    }

    protected Tag(Parcel in) {
        this.tag = in.readString();
        this.thumb_url = in.readString();
        this.featured = in.readByte() != 0;
        this.url = in.readString();
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}
