package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by Alexander Sokol
 * on 22.09.15 14:58.
 */
public class Photo implements Parcelable {

    public String caption;
    public PhotoSize[] alt_sizes;
    public PhotoSize original_size;


    public Photo() {
    }

    protected Photo(Parcel in) {
        this.caption = in.readString();
        this.alt_sizes = (PhotoSize[]) in.readParcelableArray(PhotoSize.class.getClassLoader());
        this.original_size = in.readParcelable(PhotoSize.class.getClassLoader());
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "Photo{" + separator +
                "\tcaption='" + caption + '\'' + separator +
                "\talt_sizes=" + Arrays.toString(alt_sizes) + separator +
                "\toriginal_size=" + original_size + separator +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.caption);
        dest.writeParcelableArray(this.alt_sizes, 0);
        dest.writeParcelable(this.original_size, 0);
    }


    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
