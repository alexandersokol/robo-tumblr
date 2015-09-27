package com.sun40.robotumblr.model.posting;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.sun40.robotumblr.R;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.RawPost;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 10.09.15 14:55.
 */
public class ChatPostCreator extends PostCreator {

    private String mTitle;
    private String mConversation;

    public ChatPostCreator() {
        super(TumblrExtras.Post.CHAT);
    }


    public void setTitle(String title) {
        mTitle = title;
    }


    public void setConversation(String conversation) {
        mConversation = conversation;
    }


    public String getTitle() {
        if (TextUtils.isEmpty(mTitle))
            return null;
        return mTitle;
    }


    public String getConversation() {
        if (TextUtils.isEmpty(mConversation))
            return null;
        return mConversation;
    }


    @Override
    protected boolean isValid() {
        return !TextUtils.isEmpty(mConversation);
    }


    @Override
    protected Map<String, String> paramsMap() {
        Map<String, String> params = new HashMap<>();

        if (!TextUtils.isEmpty(mTitle))
            params.put(TumblrExtras.Params.TITLE, mTitle);

        if (!TextUtils.isEmpty(mConversation))
            params.put(TumblrExtras.Params.CONVERSATION, mConversation);

        return params;
    }


    @Override
    protected int invalidationString() {
        if (!isValid())
            return R.string.creator_chat_conversation_error;
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mTitle);
        dest.writeString(this.mConversation);
    }

    protected ChatPostCreator(Parcel in) {
        super(in);
        this.mTitle = in.readString();
        this.mConversation = in.readString();
    }

    public static final Parcelable.Creator<ChatPostCreator> CREATOR = new Parcelable.Creator<ChatPostCreator>() {
        public ChatPostCreator createFromParcel(Parcel source) {
            return new ChatPostCreator(source);
        }

        public ChatPostCreator[] newArray(int size) {
            return new ChatPostCreator[size];
        }
    };
}
