package com.sun40.robotumblr.model.posting;

import android.os.Parcel;
import android.text.Html;
import android.text.TextUtils;

import com.sun40.robotumblr.R;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.RawPost;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 10.09.15 13:21.
 */
public class QuotePostCreator extends PostCreator {

    private String mQuote;
    private String mSource;

    public QuotePostCreator() {
        super(TumblrExtras.Post.QUOTE);
    }


    public void setQuote(String quote) {
        mQuote = quote;
    }


    public String getQuote() {
        if (TextUtils.isEmpty(mQuote))
            return null;
        return mQuote;
    }


    public void setSource(String source) {
        mSource = source;
    }


    public String getSource() {
        if (TextUtils.isEmpty(mSource))
            return null;
        return mSource;
    }

    @Override
    protected boolean isValid() {
        return !TextUtils.isEmpty(mQuote);
    }

    @Override
    protected Map<String, String> paramsMap() {
        Map<String, String> params = new HashMap<>();

        if (!TextUtils.isEmpty(mQuote))
            params.put(TumblrExtras.Params.QUOTE, Html.escapeHtml(mQuote));

        if (!TextUtils.isEmpty(mSource))
            params.put(TumblrExtras.Params.SOURCE, mSource);

        return params;
    }

    @Override
    protected int invalidationString() {
        if (!isValid())
            return R.string.creator_quote_source_error;
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mQuote);
        dest.writeString(this.mSource);
    }

    protected QuotePostCreator(Parcel in) {
        super(in);
        this.mQuote = in.readString();
        this.mSource = in.readString();
    }

    public static final Creator<QuotePostCreator> CREATOR = new Creator<QuotePostCreator>() {
        public QuotePostCreator createFromParcel(Parcel source) {
            return new QuotePostCreator(source);
        }

        public QuotePostCreator[] newArray(int size) {
            return new QuotePostCreator[size];
        }
    };
}
