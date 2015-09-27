package com.sun40.robotumblr.model;

import android.os.Parcel;

/**
 * Created by Alexander Sokol
 * on 22.09.15 15:37.
 */
public final class AnswerPost extends Post {

    private final String mAskingName;
    private final String mAskingUrl;
    private final String mQuestion;
    private final String mAnswer;

    AnswerPost(RawPost raw) {
        super(raw);
        mAskingName = raw.asking_name;
        mAskingUrl = raw.asking_url;
        mQuestion = raw.question;
        mAnswer = raw.answer;
    }

    public String getAskingName() {
        return mAskingName;
    }

    public String getAskingUrl() {
        return mAskingUrl;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getAnswer() {
        return mAnswer;
    }


    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "AnswerPost{" +
                super.toString() +
                "\tmAskingName='" + mAskingName + '\'' + separator +
                "\tmAskingUrl='" + mAskingUrl + '\'' + separator +
                "\tmQuestion='" + mQuestion + '\'' + separator +
                "\tmAnswer='" + mAnswer + '\'' + separator +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mAskingName);
        dest.writeString(this.mAskingUrl);
        dest.writeString(this.mQuestion);
        dest.writeString(this.mAnswer);
    }

    protected AnswerPost(Parcel in) {
        super(in);
        this.mAskingName = in.readString();
        this.mAskingUrl = in.readString();
        this.mQuestion = in.readString();
        this.mAnswer = in.readString();
    }

    public static final Creator<AnswerPost> CREATOR = new Creator<AnswerPost>() {
        public AnswerPost createFromParcel(Parcel source) {
            return new AnswerPost(source);
        }

        public AnswerPost[] newArray(int size) {
            return new AnswerPost[size];
        }
    };
}
