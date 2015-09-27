package com.sun40.robotumblr.model;

import android.os.Parcel;

/**
 * Created by Alexander Sokol
 * on 22.09.15 14:40.
 */
public final class TextPost extends Post {

    private final String mTitle;
    private final String mBody;

    TextPost(RawPost raw) {
        super(raw);
        mTitle = raw.title;
        mBody = raw.body;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "TextPost{\t" +
                super.toString() +
                "\tmTitle='" + mTitle + '\'' + separator +
                "\tmBody='" + mBody + '\'' + separator +
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
        dest.writeString(this.mBody);
    }

    protected TextPost(Parcel in) {
        super(in);
        this.mTitle = in.readString();
        this.mBody = in.readString();
    }

    public static final Creator<TextPost> CREATOR = new Creator<TextPost>() {
        public TextPost createFromParcel(Parcel source) {
            return new TextPost(source);
        }

        public TextPost[] newArray(int size) {
            return new TextPost[size];
        }
    };
}
