package com.sun40.robotumblr.model;

import android.os.Parcel;

/**
 * Created by Alexander Sokol
 * on 22.09.15 14:46.
 */
public final class QuotePost extends Post {

    private final String mText;
    private final String mSource;

    QuotePost(RawPost raw) {
        super(raw);
        mText = raw.text;
        mSource = raw.source;
    }

    public String getText() {
        return mText;
    }

    public String getSource() {
        return mSource;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "QuotePost{\t" +
                super.toString() +
                "\tmText='" + mText + '\'' + separator +
                "\tmSource='" + mSource + '\'' + separator +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mText);
        dest.writeString(this.mSource);
    }

    protected QuotePost(Parcel in) {
        super(in);
        this.mText = in.readString();
        this.mSource = in.readString();
    }

    public static final Creator<QuotePost> CREATOR = new Creator<QuotePost>() {
        public QuotePost createFromParcel(Parcel source) {
            return new QuotePost(source);
        }

        public QuotePost[] newArray(int size) {
            return new QuotePost[size];
        }
    };
}


