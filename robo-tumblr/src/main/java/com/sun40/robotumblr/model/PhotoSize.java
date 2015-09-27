package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 22.09.15 14:57.
 */
public final class PhotoSize implements Parcelable {

    public int width;
    public int height;
    public String url;

    public PhotoSize() {
    }

    protected PhotoSize(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.url);
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "PhotoSize{" + separator +
                "\twidth=" + width + separator +
                "\theight=" + height + separator +
                "\turl='" + url + '\'' + separator +
                '}';
    }

    public static final Parcelable.Creator<PhotoSize> CREATOR = new Parcelable.Creator<PhotoSize>() {
        public PhotoSize createFromParcel(Parcel source) {
            return new PhotoSize(source);
        }

        public PhotoSize[] newArray(int size) {
            return new PhotoSize[size];
        }
    };
}
