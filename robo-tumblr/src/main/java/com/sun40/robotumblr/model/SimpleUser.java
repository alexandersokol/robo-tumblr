package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 07.09.15 16:52.
 */
public class SimpleUser implements Parcelable {

    public String name;
    public boolean following;
    public String url;
    long updated;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(following ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
        dest.writeLong(this.updated);
    }

    public SimpleUser() {
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "SimpleUser{" + separator +
                "\tname='" + name + '\'' + separator +
                "\tfollowing=" + following + separator +
                "\turl='" + url + '\'' + separator +
                "\tupdated=" + updated + separator +
                '}';
    }

    protected SimpleUser(Parcel in) {
        this.name = in.readString();
        this.following = in.readByte() != 0;
        this.url = in.readString();
        this.updated = in.readLong();
    }

    public static final Creator<SimpleUser> CREATOR = new Creator<SimpleUser>() {
        public SimpleUser createFromParcel(Parcel source) {
            return new SimpleUser(source);
        }

        public SimpleUser[] newArray(int size) {
            return new SimpleUser[size];
        }
    };
}
