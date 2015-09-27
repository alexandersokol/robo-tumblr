package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 22.09.15 15:46.
 */
public final class Dialogue implements Parcelable {

    public String label;
    public String name;
    public String phrase;

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "Dialogue{" + separator +
                "\tlabel='" + label + '\'' + separator +
                "\tname='" + name + '\'' + separator +
                "\tphrase='" + phrase + '\'' + separator +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeString(this.name);
        dest.writeString(this.phrase);
    }

    public Dialogue() {
    }

    protected Dialogue(Parcel in) {
        this.label = in.readString();
        this.name = in.readString();
        this.phrase = in.readString();
    }

    public static final Parcelable.Creator<Dialogue> CREATOR = new Parcelable.Creator<Dialogue>() {
        public Dialogue createFromParcel(Parcel source) {
            return new Dialogue(source);
        }

        public Dialogue[] newArray(int size) {
            return new Dialogue[size];
        }
    };
}
