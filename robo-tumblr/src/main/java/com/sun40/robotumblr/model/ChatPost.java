package com.sun40.robotumblr.model;

import android.os.Parcel;

import java.util.Arrays;

/**
 * Created by Alexander Sokol
 * on 22.09.15 15:44.
 */
public final class ChatPost extends Post {

    private final String mTitle;
    private final String mBody;
    private final Dialogue[] mDialogue;

    ChatPost(RawPost raw) {
        super(raw);
        mTitle = raw.title;
        mBody = raw.body;
        mDialogue = raw.dialogue;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }

    public Dialogue[] getDialogue() {
        return mDialogue;
    }


    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "ChatPost{" +
                super.toString() +
                "\tmTitle='" + mTitle + '\'' + separator +
                "\tmBody='" + mBody + '\'' + separator +
                "\tmDialogue=" + Arrays.toString(mDialogue) + separator +
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
        dest.writeParcelableArray(this.mDialogue, 0);
    }

    protected ChatPost(Parcel in) {
        super(in);
        this.mTitle = in.readString();
        this.mBody = in.readString();
        this.mDialogue = (Dialogue[]) in.readParcelableArray(Dialogue.class.getClassLoader());
    }

    public static final Creator<ChatPost> CREATOR = new Creator<ChatPost>() {
        public ChatPost createFromParcel(Parcel source) {
            return new ChatPost(source);
        }

        public ChatPost[] newArray(int size) {
            return new ChatPost[size];
        }
    };
}
