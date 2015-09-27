package com.sun40.robotumblr.token;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 27.08.15 10:28.
 */
public final class ConsumerToken extends Token implements Parcelable{

    public ConsumerToken(String token, String secret) {
        super(token, secret);
    }

    protected ConsumerToken(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConsumerToken> CREATOR = new Creator<ConsumerToken>() {
        @Override
        public ConsumerToken createFromParcel(Parcel in) {
            return new ConsumerToken(in);
        }

        @Override
        public ConsumerToken[] newArray(int size) {
            return new ConsumerToken[size];
        }
    };
}
