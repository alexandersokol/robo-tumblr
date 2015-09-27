package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 24.09.15 11:27.
 */
public class Note implements Parcelable {

    public static final String TYPE_LIKE = "like";
    public static final String TYPE_REBLOG = "reblog";

    public long timestamp;
    public String blog_name;
    public String blog_url;
    public String type;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "Note{" + separator +
                "\ttimestamp=" + timestamp + separator +
                "\tblog_name='" + blog_name + '\'' + separator +
                "\tblog_url='" + blog_url + '\'' + separator +
                "\ttype='" + type + '\'' + separator +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.timestamp);
        dest.writeString(this.blog_name);
        dest.writeString(this.blog_url);
        dest.writeString(this.type);
    }

    public Note() {
    }

    protected Note(Parcel in) {
        this.timestamp = in.readLong();
        this.blog_name = in.readString();
        this.blog_url = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
