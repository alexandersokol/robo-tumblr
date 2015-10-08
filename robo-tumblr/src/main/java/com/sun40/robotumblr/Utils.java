package com.sun40.robotumblr;

import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.RawPost;

import java.util.ArrayList;

/**
 * Created by Alexander Sokol
 * on 08.10.15.
 */
class Utils {

    static final int STATUS_FOUND = 301;

    private Utils() {
    }

    static String checkHostname(String hostname) {
        if (hostname != null) {
            if (!hostname.contains(TumblrExtras.HOSTNAME_SUFFIX))
                hostname += TumblrExtras.HOSTNAME_SUFFIX;
            return hostname;
        }
        return null;
    }

    static int checkOffset(int offset) {
        if (offset < 0)
            offset = 0;
        return offset;
    }


    static int checkLimit(int limit) {
        if (limit < 1 || limit > 20)
            limit = 20;
        return limit;
    }


    static long checkTimestamp(long timestamp) {
        if (timestamp < 0)
            timestamp = 0;
        return timestamp;
    }


    static ArrayList<Post> extractPosts(RawPost[] rawPosts) {
        ArrayList<Post> posts = new ArrayList<>();
        if (rawPosts != null && rawPosts.length > 0) {
            for (RawPost raw : rawPosts) {
                posts.add(raw.create());
            }
        }
        return posts;
    }

}
