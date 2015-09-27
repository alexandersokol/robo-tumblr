package com.sun40.robotumblr.model;

import android.os.Parcel;

/**
 * Created by Alexander Sokol
 * on 22.09.15 16:56.
 */
public final class VideoPost extends Post {

    private final String mVideoUrl;
    private final String mThumbnailUrl;
    private final int mThumbnailWidth;
    private final int mThumbnailHeight;
    private final int mDuration;
    private final String mVideoType;
    private final String mPlacementId;
    private final String mPermalinkUrl;

    VideoPost(RawPost raw) {
        super(raw);
        mVideoUrl = raw.videoUrl;
        mThumbnailUrl = raw.thumbnail_url;
        mThumbnailWidth = raw.thumbnail_width;
        mThumbnailHeight = raw.thumbnail_height;
        mDuration = raw.duration;
        mVideoType = raw.video_type;
        mPlacementId = raw.placement_id;
        mPermalinkUrl = raw.permalink_url;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public int getThumbnailWidth() {
        return mThumbnailWidth;
    }

    public int getThumbnailHeight() {
        return mThumbnailHeight;
    }

    public int getDuration() {
        return mDuration;
    }

    public String getVideoType() {
        return mVideoType;
    }

    public String getPlacementId() {
        return mPlacementId;
    }

    public String getPermalinkUrl() {
        return mPermalinkUrl;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "VideoPost{" + separator +
                super.toString() +
                "\tmVideoUrl='" + mVideoUrl + '\'' + separator +
                "\tmThumbnailUrl='" + mThumbnailUrl + '\'' + separator +
                "\tmThumbnailWidth=" + mThumbnailWidth + separator +
                "\tmThumbnailHeight=" + mThumbnailHeight + separator +
                "\tmDuration=" + mDuration + separator +
                "\tmVideoType='" + mVideoType + '\'' + separator +
                "\tmPlacementId='" + mPlacementId + '\'' + separator +
                "\tmPermalinkUrl='" + mPermalinkUrl + '\'' + separator +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mVideoUrl);
        dest.writeString(this.mThumbnailUrl);
        dest.writeInt(this.mThumbnailWidth);
        dest.writeInt(this.mThumbnailHeight);
        dest.writeInt(this.mDuration);
        dest.writeString(this.mVideoType);
        dest.writeString(this.mPlacementId);
        dest.writeString(this.mPermalinkUrl);
    }

    protected VideoPost(Parcel in) {
        super(in);
        this.mVideoUrl = in.readString();
        this.mThumbnailUrl = in.readString();
        this.mThumbnailWidth = in.readInt();
        this.mThumbnailHeight = in.readInt();
        this.mDuration = in.readInt();
        this.mVideoType = in.readString();
        this.mPlacementId = in.readString();
        this.mPermalinkUrl = in.readString();
    }

    public static final Creator<VideoPost> CREATOR = new Creator<VideoPost>() {
        public VideoPost createFromParcel(Parcel source) {
            return new VideoPost(source);
        }

        public VideoPost[] newArray(int size) {
            return new VideoPost[size];
        }
    };
}
