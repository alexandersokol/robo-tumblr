package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.sun40.robotumblr.TumblrExtras;

import java.util.Arrays;

/**
 * Created by Alexander Sokol
 * on 22.09.15 14:25.
 */
public abstract class Post implements Parcelable {

    private final long mId;
    private final String mBlogName;
    private final String mPostUrl;
    @TumblrExtras.PostType
    private final String mType;
    private final String mDate;
    private final long mTimestamp;
    @TumblrExtras.StateType
    private final String mState;
    private final String mReblogKey;
    private final String[] mTags;
    private final int mNoteCount;
    private final boolean mIsBookmarkletCreated;
    private final boolean mIsMobileCreated;
    private final String mSourceUrl;
    private final String mSourceTitle;

    private final String mReblogComment;
    private final String mReblogHTMLTree;

    private final Trail[] mTrails;

    private final Note[] mNotes;

    private final long mRebloggedFromId;
    private final String mRebloggedFromUrl;
    private final String mRebloggedFromName;
    private final String mRebloggedFromTitle;
    private final long mRebloggedRootId;
    private final String mRebloggedRootUrl;
    private final String mRebloggedRootName;
    private final String mRebloggedRootTitle;


    Post(RawPost raw) {
        mId = raw.id;
        mBlogName = raw.blog_name;
        mPostUrl = raw.post_url;
        mType = raw.type;
        mDate = raw.date;
        mTimestamp = raw.timestamp;
        mState = raw.state;
        mReblogKey = raw.reblog_key;
        mTags = raw.tags;
        mNoteCount = raw.note_count;
        mIsBookmarkletCreated = raw.bookmarklet;
        mIsMobileCreated = raw.mobile;
        mSourceUrl = raw.source_url;
        mSourceTitle = raw.source_title;
        mTrails = raw.trail;
        mNotes = raw.notes;
        mRebloggedFromId = raw.reblogged_from_id;
        mRebloggedFromUrl = raw.reblogged_from_url;
        mRebloggedFromName = raw.reblogged_from_name;
        mRebloggedFromTitle = raw.reblogged_from_title;
        mRebloggedRootId = raw.reblogged_root_id;
        mRebloggedRootUrl = raw.reblogged_root_url;
        mRebloggedRootName = raw.reblogged_root_name;
        mRebloggedRootTitle = raw.reblogged_root_title;

        if (raw.reblog != null) {
            mReblogComment = raw.reblog.comment;
            mReblogHTMLTree = raw.reblog.tree_html;
        } else {
            mReblogComment = null;
            mReblogHTMLTree = null;
        }
    }


    public long getId() {
        return mId;
    }

    public String getBlogName() {
        return mBlogName;
    }

    public String getPostUrl() {
        return mPostUrl;
    }

    public String getType() {
        return mType;
    }

    public String getDate() {
        return mDate;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public String getState() {
        return mState;
    }

    public String getReblogKey() {
        return mReblogKey;
    }

    public String[] getTags() {
        return mTags;
    }

    public int getNoteCount() {
        return mNoteCount;
    }

    public boolean isIsBookmarkletCreated() {
        return mIsBookmarkletCreated;
    }

    public boolean isIsMobileCreated() {
        return mIsMobileCreated;
    }

    public String getSourceUrl() {
        return mSourceUrl;
    }

    public String getSourceTitle() {
        return mSourceTitle;
    }

    public Trail[] getTrails() {
        return mTrails;
    }

    public Note[] getNotes() {
        return mNotes;
    }

    public String getReblogComment() {
        return mReblogComment;
    }

    public String getReblogHTMLTree() {
        return mReblogHTMLTree;
    }

    public long getRebloggedFromId() {
        return mRebloggedFromId;
    }

    public String getRebloggedFromUrl() {
        return mRebloggedFromUrl;
    }

    public String getRebloggedFromName() {
        return mRebloggedFromName;
    }

    public String getRebloggedFromTitle() {
        return mRebloggedFromTitle;
    }

    public long getRebloggedRootId() {
        return mRebloggedRootId;
    }

    public String getRebloggedRootUrl() {
        return mRebloggedRootUrl;
    }

    public String getRebloggedRootName() {
        return mRebloggedRootName;
    }

    public String getRebloggedRootTitle() {
        return mRebloggedRootTitle;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        builder.append("\tmId=" + mId + separator +
                "\tmBlogName='" + mBlogName + '\'' + separator +
                "\tmPostUrl='" + mPostUrl + '\'' + separator +
                "\tmType='" + mType + '\'' + separator +
                "\tmDate='" + mDate + '\'' + separator +
                "\tmTimestamp=" + mTimestamp + separator +
                "\tmState='" + mState + '\'' + separator +
                "\tmReblogKey='" + mReblogKey + '\'' + separator +
                "\tmTags=" + Arrays.toString(mTags) + separator +
                "\tmNoteCount=" + mNoteCount + separator +
                "\tmIsBookmarkletCreated=" + mIsBookmarkletCreated + separator +
                "\tmIsMobileCreated=" + mIsMobileCreated + separator +
                "\tmSourceUrl='" + mSourceUrl + '\'' + separator +
                "\tmSourceTitle='" + mSourceTitle + '\'' + separator +
                "\tmReblogComment='" + mReblogComment + '\'' + separator +
                "\tmReblogHTMLTree='" + mReblogHTMLTree + '\'' + separator);

        if (mTrails != null && mTrails.length > 0) {
            builder.append(separator);
            for (Trail trail : mTrails) {
                builder.append("\t").append(trail.toString()).append(separator);
            }
            builder.append(separator);
        }

        if (mNotes != null && mNotes.length > 0) {
            builder.append(separator);
            for (Note note : mNotes) {
                builder.append("\t").append(note.toString()).append(separator);
            }
            builder.append(separator);
        }

        if (mRebloggedFromUrl != null) {
            builder.append("\tmRebloggedFromId=" + mRebloggedFromId + separator +
                    "\tmRebloggedFromUrl='" + mRebloggedFromUrl + '\'' + separator +
                    "\tmRebloggedFromName='" + mRebloggedFromName + '\'' + separator +
                    "\tmRebloggedFromTitle='" + mRebloggedFromTitle + '\'' + separator +
                    "\tmRebloggedRootId=" + mRebloggedRootId + separator +
                    "\tmRebloggedRootUrl='" + mRebloggedRootUrl + '\'' + separator +
                    "\tmRebloggedRootName='" + mRebloggedRootName + '\'' + separator +
                    "\tmRebloggedRootTitle='" + mRebloggedRootTitle + '\'' + separator);
        }

        return builder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mBlogName);
        dest.writeString(this.mPostUrl);
        dest.writeString(this.mType);
        dest.writeString(this.mDate);
        dest.writeLong(this.mTimestamp);
        dest.writeString(this.mState);
        dest.writeString(this.mReblogKey);
        dest.writeStringArray(this.mTags);
        dest.writeInt(this.mNoteCount);
        dest.writeByte(mIsBookmarkletCreated ? (byte) 1 : (byte) 0);
        dest.writeByte(mIsMobileCreated ? (byte) 1 : (byte) 0);
        dest.writeString(this.mSourceUrl);
        dest.writeString(this.mSourceTitle);
        dest.writeString(this.mReblogComment);
        dest.writeString(this.mReblogHTMLTree);
        dest.writeParcelableArray(this.mTrails, 0);
        dest.writeParcelableArray(this.mNotes, 0);
        dest.writeLong(this.mRebloggedFromId);
        dest.writeString(this.mRebloggedFromUrl);
        dest.writeString(this.mRebloggedFromName);
        dest.writeString(this.mRebloggedFromTitle);
        dest.writeLong(this.mRebloggedRootId);
        dest.writeString(this.mRebloggedRootUrl);
        dest.writeString(this.mRebloggedRootName);
        dest.writeString(this.mRebloggedRootTitle);
    }

    protected Post(Parcel in) {
        this.mId = in.readLong();
        this.mBlogName = in.readString();
        this.mPostUrl = in.readString();
        this.mType = in.readString();
        this.mDate = in.readString();
        this.mTimestamp = in.readLong();
        this.mState = in.readString();
        this.mReblogKey = in.readString();
        this.mTags = in.createStringArray();
        this.mNoteCount = in.readInt();
        this.mIsBookmarkletCreated = in.readByte() != 0;
        this.mIsMobileCreated = in.readByte() != 0;
        this.mSourceUrl = in.readString();
        this.mSourceTitle = in.readString();
        this.mReblogComment = in.readString();
        this.mReblogHTMLTree = in.readString();
        this.mTrails = (Trail[]) in.readParcelableArray(Trail.class.getClassLoader());
        this.mNotes = (Note[]) in.readParcelableArray(Note.class.getClassLoader());
        this.mRebloggedFromId = in.readLong();
        this.mRebloggedFromUrl = in.readString();
        this.mRebloggedFromName = in.readString();
        this.mRebloggedFromTitle = in.readString();
        this.mRebloggedRootId = in.readLong();
        this.mRebloggedRootUrl = in.readString();
        this.mRebloggedRootName = in.readString();
        this.mRebloggedRootTitle = in.readString();
    }
}
