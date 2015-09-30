package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by Alexander Sokol
 * on 14.09.15 12:01.
 */
public class User implements Parcelable {

    /**
     * The number of blogs the user is following
     */
    public int following;
    /**
     * The default posting format - html, markdown or raw
     */
    public String default_post_format;
    /**
     * The user's tumblr short name
     */
    public String name;
    /**
     * The total count of the user's likes
     */
    public int likes;
    /**
     * Each item is a blog the user has permissions to post to, containing these fields:
     * name – string: the short name of the blog
     * url – string: the URL of the blog
     * title – string: the title of the blog
     * primary – boolean: indicates if this is the user's primary blog
     * followers – number: total count of followers for this blog
     * tweet – string: indicate if posts are tweeted auto, Y, N
     * facebook – indicate if posts are sent to facebook Y, N
     * type – indicates whether a blog is public or private
     */
    public Blog[] blogs;


    public User() {
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        builder.append("User{" + separator +
                "\tfollowing=" + following + separator +
                "\tdefault_post_format='" + default_post_format + '\'' + separator +
                "\tname='" + name + '\'' + separator +
                "\tlikes=" + likes + separator +
                "\tblogs count=").append((blogs != null) ? blogs.length : 0);

        builder.append(separator).append(separator);
        if (blogs != null) {
            for (Blog blog : blogs) {
                builder.append(blog.toString());
                builder.append(separator);
            }
        }

        builder.append('}');

        return builder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.following);
        dest.writeString(this.default_post_format);
        dest.writeString(this.name);
        dest.writeInt(this.likes);
        dest.writeParcelableArray(this.blogs, 0);
    }

    protected User(Parcel in) {
        this.following = in.readInt();
        this.default_post_format = in.readString();
        this.name = in.readString();
        this.likes = in.readInt();
        this.blogs = (Blog[]) in.readParcelableArray(Blog.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
