package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 14.09.15 16:46.
 */
public class SimpleBlog implements Parcelable {

    public String name;
    public String title;
    public String description;
    public String url;
    public long updated;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeLong(this.updated);
    }

    public SimpleBlog() {
    }

    protected SimpleBlog(Parcel in) {
        this.name = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.updated = in.readLong();
    }

    public static final Creator<SimpleBlog> CREATOR = new Creator<SimpleBlog>() {
        public SimpleBlog createFromParcel(Parcel source) {
            return new SimpleBlog(source);
        }

        public SimpleBlog[] newArray(int size) {
            return new SimpleBlog[size];
        }
    };
}
