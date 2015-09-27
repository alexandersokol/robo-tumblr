package com.sun40.robotumblr.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 01.09.15 17:35.
 */
public class Blog implements Parcelable {

    /**
     * The display title of the blog
     */
    public String title;
    /**
     * The total number of posts to this blog
     */
    public int posts;
    /**
     * The short blog name that appears before tumblr.com in a standard blog hostname (and before the domain in a custom blog hostname)
     */
    public String name;
    /**
     * Current blog url
     */
    public String url;
    /**
     * The time of the most recent post, in seconds since the epoch
     */
    public long updated;
    /**
     * Current blog description
     */
    public String description;
    /**
     * Indicates whether the blog allows questions
     */
    public boolean ask;
    /**
     * Indicates whether the blog allows anonymous questions
     * Returned only if ask is true
     */
    public Boolean ask_anon;
    /**
     * Number of likes for this user
     * Returned only if this is the user's primary blog and sharing of likes is enabled
     */
    public Integer likes;
    /**
     * is blog not safe for work
     */
    public Boolean is_nsfw;
    /**
     * Ask page title
     */
    public String ask_page_title;
    /**
     * can send messages
     */
    public Boolean can_message;
    /**
     * Submission page title
     */
    public String submission_page_title;
    /**
     * can share blog likes
     */
    public Boolean share_likes;
    /**
     * Indicates whether this blog has been blocked by the calling user's primary blog
     * Returned only if there is an authenticated user making this call
     */
    @Authorized
    public Boolean is_blocked_from_primary;
    /**
     * Indicate current user follows current blog
     */
    @Authorized
    public Boolean followed;
    @Authorized
    public Boolean can_send_fan_mail;
    @Authorized
    public Boolean subscribed;
    @Authorized
    public Boolean can_subscribe;

    public Blog() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.posts);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeLong(this.updated);
        dest.writeString(this.description);
        dest.writeByte(ask ? (byte) 1 : (byte) 0);
        dest.writeValue(this.ask_anon);
        dest.writeValue(this.likes);
        dest.writeValue(this.is_nsfw);
        dest.writeString(this.ask_page_title);
        dest.writeValue(this.can_message);
        dest.writeString(this.submission_page_title);
        dest.writeValue(this.share_likes);
        dest.writeValue(this.is_blocked_from_primary);
    }

    protected Blog(Parcel in) {
        this.title = in.readString();
        this.posts = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
        this.updated = in.readLong();
        this.description = in.readString();
        this.ask = in.readByte() != 0;
        this.ask_anon = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.likes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.is_nsfw = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.ask_page_title = in.readString();
        this.can_message = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.submission_page_title = in.readString();
        this.share_likes = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.is_blocked_from_primary = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        public Blog createFromParcel(Parcel source) {
            return new Blog(source);
        }

        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "Blog{" + separator +
                "\ttitle='" + title + '\'' + separator +
                "\tposts=" + posts + separator +
                "\tname='" + name + '\'' + separator +
                "\turl='" + url + '\'' + separator +
                "\tupdated=" + updated + separator +
                "\tdescription='" + description + '\'' + separator +
                "\task=" + ask + separator +
                "\task_anon=" + ask_anon + separator +
                "\tlikes=" + likes + separator +
                "\tis_nsfw=" + is_nsfw + separator +
                "\task_page_title='" + ask_page_title + '\'' + separator +
                "\tcan_message=" + can_message + separator +
                "\tsubmission_page_title='" + submission_page_title + '\'' + separator +
                "\tshare_likes=" + share_likes + separator +
                "\tis_blocked_from_primary=" + is_blocked_from_primary + separator +
                "\tfollowed=" + followed + separator +
                "\tcan_send_fan_mail=" + can_send_fan_mail + separator +
                "\tsubscribed=" + subscribed + separator +
                "\tcan_subscribe=" + can_subscribe + separator +
                '}';
    }
}
