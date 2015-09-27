package com.sun40.robotumblr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander Sokol
 * on 24.09.15 11:34.
 */
public class Trail implements Parcelable {

    public TrailBlog blog;
    public TrailPost post;
    public String content_raw;
    public String content;
    public boolean is_root_item;


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return "Trail{" + separator +
                "\tblog=" + blog + separator +
                "\tpost=" + post + separator +
                "\tcontent_raw='" + content_raw + '\'' + separator +
                "\tcontent='" + content + '\'' + separator +
                "\tis_root_item=" + is_root_item + separator +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.blog, 0);
        dest.writeParcelable(this.post, 0);
        dest.writeString(this.content_raw);
        dest.writeString(this.content);
        dest.writeByte(is_root_item ? (byte) 1 : (byte) 0);
    }

    public Trail() {
    }

    protected Trail(Parcel in) {
        this.blog = in.readParcelable(TrailBlog.class.getClassLoader());
        this.post = in.readParcelable(TrailPost.class.getClassLoader());
        this.content_raw = in.readString();
        this.content = in.readString();
        this.is_root_item = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Trail> CREATOR = new Parcelable.Creator<Trail>() {
        public Trail createFromParcel(Parcel source) {
            return new Trail(source);
        }

        public Trail[] newArray(int size) {
            return new Trail[size];
        }
    };


    public static class TrailBlog implements Parcelable {
        public String name;
        public boolean active;
        public TrailTheme theme;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            String separator = System.getProperty("line.separator");
            return "TrailBlog{" + separator +
                    "\tname='" + name + '\'' + separator +
                    "\tactive=" + active + separator +
                    "\ttheme=" + theme.toString() + separator +
                    '}';
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeByte(active ? (byte) 1 : (byte) 0);
            dest.writeParcelable(this.theme, 0);
        }

        public TrailBlog() {
        }

        protected TrailBlog(Parcel in) {
            this.name = in.readString();
            this.active = in.readByte() != 0;
            this.theme = in.readParcelable(TrailTheme.class.getClassLoader());
        }

        public static final Parcelable.Creator<TrailBlog> CREATOR = new Parcelable.Creator<TrailBlog>() {
            public TrailBlog createFromParcel(Parcel source) {
                return new TrailBlog(source);
            }

            public TrailBlog[] newArray(int size) {
                return new TrailBlog[size];
            }
        };
    }

    public static class TrailPost implements Parcelable {
        public long id;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            String separator = System.getProperty("line.separator");
            return "TrailPost{" + separator +
                    "\tid=" + id + separator +
                    '}';
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
        }

        public TrailPost() {
        }

        protected TrailPost(Parcel in) {
            this.id = in.readLong();
        }

        public static final Parcelable.Creator<TrailPost> CREATOR = new Parcelable.Creator<TrailPost>() {
            public TrailPost createFromParcel(Parcel source) {
                return new TrailPost(source);
            }

            public TrailPost[] newArray(int size) {
                return new TrailPost[size];
            }
        };
    }


    public static class TrailTheme implements Parcelable {
        public int header_full_width;
        public int header_full_height;
        public int header_focus_width;
        public int header_focus_height;
        public String avatar_shape;
        public String background_color;
        public String body_font;
        public String header_bounds;
        public String header_image;
        public String header_image_focused;
        public String header_image_scaled;
        public boolean header_stretch;
        public String link_color;
        public boolean show_avatar;
        public boolean show_description;
        public boolean show_header_image;
        public boolean show_title;
        public String title_color;
        public String title_font;
        public String title_font_weight;

        @Override
        public String toString() {
            String separator = System.getProperty("line.separator");
            return "TrailTheme{" + separator +
                    "\theader_full_width=" + header_full_width + separator +
                    "\theader_full_height=" + header_full_height + separator +
                    "\theader_focus_width=" + header_focus_width + separator +
                    "\theader_focus_height=" + header_focus_height + separator +
                    "\tavatar_shape='" + avatar_shape + '\'' + separator +
                    "\tbackground_color='" + background_color + '\'' + separator +
                    "\tbody_font='" + body_font + '\'' + separator +
                    "\theader_bounds='" + header_bounds + '\'' + separator +
                    "\theader_image='" + header_image + '\'' + separator +
                    "\theader_image_focused='" + header_image_focused + '\'' + separator +
                    "\theader_image_scaled='" + header_image_scaled + '\'' + separator +
                    "\theader_stretch=" + header_stretch + separator +
                    "\tlink_color='" + link_color + '\'' + separator +
                    "\tshow_avatar=" + show_avatar + separator +
                    "\tshow_description=" + show_description + separator +
                    "\tshow_header_image=" + show_header_image + separator +
                    "\tshow_title=" + show_title + separator +
                    "\ttitle_color='" + title_color + '\'' + separator +
                    "\ttitle_font='" + title_font + '\'' + separator +
                    "\ttitle_font_weight='" + title_font_weight + '\'' + separator +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.header_full_width);
            dest.writeInt(this.header_full_height);
            dest.writeInt(this.header_focus_width);
            dest.writeInt(this.header_focus_height);
            dest.writeString(this.avatar_shape);
            dest.writeString(this.background_color);
            dest.writeString(this.body_font);
            dest.writeString(this.header_bounds);
            dest.writeString(this.header_image);
            dest.writeString(this.header_image_focused);
            dest.writeString(this.header_image_scaled);
            dest.writeByte(header_stretch ? (byte) 1 : (byte) 0);
            dest.writeString(this.link_color);
            dest.writeByte(show_avatar ? (byte) 1 : (byte) 0);
            dest.writeByte(show_description ? (byte) 1 : (byte) 0);
            dest.writeByte(show_header_image ? (byte) 1 : (byte) 0);
            dest.writeByte(show_title ? (byte) 1 : (byte) 0);
            dest.writeString(this.title_color);
            dest.writeString(this.title_font);
            dest.writeString(this.title_font_weight);
        }

        public TrailTheme() {
        }

        protected TrailTheme(Parcel in) {
            this.header_full_width = in.readInt();
            this.header_full_height = in.readInt();
            this.header_focus_width = in.readInt();
            this.header_focus_height = in.readInt();
            this.avatar_shape = in.readString();
            this.background_color = in.readString();
            this.body_font = in.readString();
            this.header_bounds = in.readString();
            this.header_image = in.readString();
            this.header_image_focused = in.readString();
            this.header_image_scaled = in.readString();
            this.header_stretch = in.readByte() != 0;
            this.link_color = in.readString();
            this.show_avatar = in.readByte() != 0;
            this.show_description = in.readByte() != 0;
            this.show_header_image = in.readByte() != 0;
            this.show_title = in.readByte() != 0;
            this.title_color = in.readString();
            this.title_font = in.readString();
            this.title_font_weight = in.readString();
        }

        public static final Parcelable.Creator<TrailTheme> CREATOR = new Parcelable.Creator<TrailTheme>() {
            public TrailTheme createFromParcel(Parcel source) {
                return new TrailTheme(source);
            }

            public TrailTheme[] newArray(int size) {
                return new TrailTheme[size];
            }
        };
    }

}
