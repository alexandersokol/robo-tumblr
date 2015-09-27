package com.sun40.robotumblr.model;

import android.os.Parcel;

/**
 * Created by Alexander Sokol
 * on 22.09.15 16:04.
 */
public final class AudioPost extends Post {

    private final String mCaption;
    private final int mPlays;
    private final String mAudioUrl;
    private final String mAudioSourceUrl;
    private final boolean mIsExternal;
    private final String mAudioType;
    private final String mAlbumArt;
    private final String mArtist;
    private final String mAlbum;
    private final String mTrackName;
    private final int mYear;

    AudioPost(RawPost raw) {
        super(raw);
        mCaption = raw.caption;
        mPlays = raw.plays;
        mAudioUrl = raw.audio_url;
        mAudioSourceUrl = raw.audio_source_url;
        mIsExternal = raw.is_external;
        mAudioType = raw.audio_type;
        mAlbumArt = raw.album_art;
        mArtist = raw.artist;
        mAlbum = raw.album;
        mTrackName = raw.track_name;
        mYear = raw.year;
    }


    public String getCaption() {
        return mCaption;
    }

    public int getPlays() {
        return mPlays;
    }

    public String getAudioUrl() {
        return mAudioUrl;
    }

    public String getAudioSourceUrl() {
        return mAudioSourceUrl;
    }

    public boolean isIsExternal() {
        return mIsExternal;
    }

    public String getAudioType() {
        return mAudioType;
    }

    public String getAlbumArt() {
        return mAlbumArt;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getTrackName() {
        return mTrackName;
    }

    public int getYear() {
        return mYear;
    }


    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "AudioPost{" + separator +
                super.toString() +
                "\tmCaption='" + mCaption + '\'' + separator +
                "\tmPlays=" + mPlays + separator +
                "\tmAudioUrl='" + mAudioUrl + '\'' + separator +
                "\tmAudioSourceUrl='" + mAudioSourceUrl + '\'' + separator +
                "\tmIsExternal=" + mIsExternal + separator +
                "\tmAudioType='" + mAudioType + '\'' + separator +
                "\tmAlbumArt='" + mAlbumArt + '\'' + separator +
                "\tmArtist='" + mArtist + '\'' + separator +
                "\tmAlbum='" + mAlbum + '\'' + separator +
                "\tmTrackName='" + mTrackName + '\'' + separator +
                "\tmYear=" + mYear + separator +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mCaption);
        dest.writeInt(this.mPlays);
        dest.writeString(this.mAudioUrl);
        dest.writeString(this.mAudioSourceUrl);
        dest.writeByte(mIsExternal ? (byte) 1 : (byte) 0);
        dest.writeString(this.mAudioType);
        dest.writeString(this.mAlbumArt);
        dest.writeString(this.mArtist);
        dest.writeString(this.mAlbum);
        dest.writeString(this.mTrackName);
        dest.writeInt(this.mYear);
    }

    protected AudioPost(Parcel in) {
        super(in);
        this.mCaption = in.readString();
        this.mPlays = in.readInt();
        this.mAudioUrl = in.readString();
        this.mAudioSourceUrl = in.readString();
        this.mIsExternal = in.readByte() != 0;
        this.mAudioType = in.readString();
        this.mAlbumArt = in.readString();
        this.mArtist = in.readString();
        this.mAlbum = in.readString();
        this.mTrackName = in.readString();
        this.mYear = in.readInt();
    }

    public static final Creator<AudioPost> CREATOR = new Creator<AudioPost>() {
        public AudioPost createFromParcel(Parcel source) {
            return new AudioPost(source);
        }

        public AudioPost[] newArray(int size) {
            return new AudioPost[size];
        }
    };
}
