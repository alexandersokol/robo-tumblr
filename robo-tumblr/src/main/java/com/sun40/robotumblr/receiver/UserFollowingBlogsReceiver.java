package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.SimpleBlog;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 14.09.15 16:54.
 */
public class UserFollowingBlogsReceiver extends BaseResultReceiver<UserFollowingBlogsReceiver.UserFollowingBlogsListener> {

    public UserFollowingBlogsReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onStart(Bundle data) {

    }

    @Override
    protected void onProgress(Bundle data) {

    }

    @Override
    protected void onError(Bundle data) {
        getListener().onUserFollowingBlogsFail(data.isEmpty() ? null : data.getString(QueryService.KEY_ERROR));
    }

    @Override
    protected void onFinish(Bundle data) {
        SimpleBlog[] blogs = (SimpleBlog[]) data.getParcelableArray(QueryService.KEY_BLOG);
        if (blogs != null) {
            int total = data.getInt(QueryService.KEY_TOTAL);
            int limit = data.getInt(QueryService.KEY_LIMIT);
            int offset = data.getInt(QueryService.KEY_OFFSET);
            getListener().onUserFollowingBlogsSuccess(Arrays.asList(blogs), total, limit, offset);
        } else
            getListener().onUserFollowingBlogsFail(null);
    }

    public interface UserFollowingBlogsListener extends OnTokenInvalidatedListener {
        void onUserFollowingBlogsFail(String error);

        void onUserFollowingBlogsSuccess(List<SimpleBlog> blogs, int total, int limit, int offset);
    }
}
