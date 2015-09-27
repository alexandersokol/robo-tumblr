package com.sun40.robotumblr.token;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 27.08.15 10:28.
 */
public final class RequestToken extends Token implements Parcelable{
    public RequestToken(String token, String secret) {
        super(token, secret);
    }

    protected RequestToken(Parcel in) {
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

    public static final Creator<RequestToken> CREATOR = new Creator<RequestToken>() {
        @Override
        public RequestToken createFromParcel(Parcel in) {
            return new RequestToken(in);
        }

        @Override
        public RequestToken[] newArray(int size) {
            return new RequestToken[size];
        }
    };
}
