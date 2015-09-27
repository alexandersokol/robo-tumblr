package com.sun40.robotumblr;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Alexander Sokol
 * on 25.08.15 17:43.
 */
public interface TumblrExtras {

    String CALLBACK_URL = "http://www.tumblr.com/connect/login_success.html";

    String HOSTNAME_SUFFIX = ".tumblr.com";

    interface Params {
        String ID = "id";
        String TYPE = "type";
        String STATE = "state";
        String TAGS = "tags";
        String TWEET = "tweet";
        String DATE = "date";
        String FORMAT = "format";
        String SLUG = "slug";
        String TITLE = "title";
        String BODY = "body";
        String CAPTION = "caption";
        String LINK = "link";
        String QUOTE = "quote";
        String SOURCE = "source";
        String URL = "url";
        String DESCRIPTION = "description";
        String CONVERSATION = "conversation";
        String EXTERNAL_URL = "external_url";
        String EMBED = "embed";
        String DATA = "data";
        String REBLOG_KEY = "reblog_key";
        String COMMENT = "comment";
    }


    interface Size {
        int SIZE_16 = 16;
        int SIZE_24 = 24;
        int SIZE_30 = 30;
        int SIZE_40 = 40;
        int SIZE_48 = 48;
        int SIZE_64 = 64;
        int SIZE_96 = 96;
        int SIZE_128 = 128;
        int SIZE_512 = 512;
        int SIZE_UNDEFINED = -1;
    }


    interface Post {
        String TEXT = "text";
        String QUOTE = "quote";
        String LINK = "link";
        String ANSWER = "answer";
        String VIDEO = "video";
        String AUDIO = "audio";
        String PHOTO = "photo";
        String CHAT = "chat";
    }


    interface Format {
        String FORMAT_HTML = "html";
        String FORMAT_MARKDOWN = "markdown";
    }


    interface Search {
        String BLOGS = "blogs";
        String TAGS = "tags";
        String ANY = "Any";
    }


    interface State {
        String PUBLISHED = "published";
        String QUEUED = "queue";
        String DRAFT = "draft";
        String PRIVATE = "private";
    }


    interface Video {
        String TUMBLR = "tumblr";
        String YOUTUBE = "youtube";
        String VIMEO = "vimeo";
        String VINE = "vine";
        String UNKNOWN = "unknown";
    }

    interface Audio {
        String TUMBLR = "tumblr";
        String SPOTIFY = "spotify";
        String UNKNOWN = "unknown";
    }


    interface Filter {
        String RAW = "raw";
        String TEXT = "text";
        String HTML = "html";
    }


    @StringDef({Filter.RAW, Filter.TEXT})
    @Retention(RetentionPolicy.SOURCE)
    @interface FilterType {

    }


    @StringDef({Audio.TUMBLR, Audio.SPOTIFY, Audio.UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    @interface AudioType {

    }


    @StringDef({Video.TUMBLR, Video.YOUTUBE, Video.VIMEO, Video.VINE, Video.UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    @interface VideoType {

    }


    @StringDef({State.PUBLISHED, State.QUEUED, State.DRAFT, State.PRIVATE})
    @Retention(RetentionPolicy.SOURCE)
    @interface StateType {

    }


    @StringDef({Format.FORMAT_HTML, Format.FORMAT_MARKDOWN})
    @Retention(RetentionPolicy.SOURCE)
    @interface FormatType {

    }


    @StringDef({Post.TEXT, Post.QUOTE, Post.LINK, Post.LINK, Post.ANSWER, Post.VIDEO, Post.AUDIO, Post.PHOTO, Post.CHAT})
    @Retention(RetentionPolicy.SOURCE)
    @interface PostType {

    }

    @StringDef({Search.ANY, Search.BLOGS, Search.TAGS})
    @Retention(RetentionPolicy.SOURCE)
    @interface SearchType {

    }

    @IntDef({Size.SIZE_16,
            Size.SIZE_24,
            Size.SIZE_30,
            Size.SIZE_40,
            Size.SIZE_48,
            Size.SIZE_64,
            Size.SIZE_96,
            Size.SIZE_128,
            Size.SIZE_512,
            Size.SIZE_UNDEFINED})
    @Retention(RetentionPolicy.SOURCE)
    @interface TumblrSize {
    }
}
