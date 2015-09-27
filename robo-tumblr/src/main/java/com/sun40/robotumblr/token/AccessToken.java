package com.sun40.robotumblr.token;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 27.08.15 10:27.
 */
public final class AccessToken extends Token implements Parcelable {

    public AccessToken(String token, String secret) {
        super(token, secret);
    }

    protected AccessToken(Parcel in) {
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

    public static final Creator<AccessToken> CREATOR = new Creator<AccessToken>() {
        @Override
        public AccessToken createFromParcel(Parcel in) {
            return new AccessToken(in);
        }

        @Override
        public AccessToken[] newArray(int size) {
            return new AccessToken[size];
        }
    };
}
