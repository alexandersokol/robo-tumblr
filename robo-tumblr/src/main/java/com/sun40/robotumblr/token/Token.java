package com.sun40.robotumblr.token;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by Alexander Sokol
 * on 27.08.15 10:27.
 */
public class Token implements Parcelable {

    private final String mToken;
    private final String mSecret;

    public Token(String token, String secret) {
        if(token == null)
            token = "";

        if(secret == null)
            secret = "";

        mToken = token;
        mSecret = secret;
    }


    public final String getToken() {
        return mToken;
    }

    public final String getSecret() {
        return mSecret;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(mToken) || TextUtils.isEmpty(mSecret);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mToken);
        dest.writeString(this.mSecret);
    }

    protected Token(Parcel in) {
        this.mToken = in.readString();
        this.mSecret = in.readString();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}
