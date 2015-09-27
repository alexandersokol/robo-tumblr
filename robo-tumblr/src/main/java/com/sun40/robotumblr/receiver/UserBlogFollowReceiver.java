package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;

/**
 * Created by Alexander Sokol
 * on 14.09.15 17:14.
 */
public class UserBlogFollowReceiver extends BaseResultReceiver<UserBlogFollowReceiver.UserBlogFollowListener> {

    public UserBlogFollowReceiver(Handler handler) {
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
        getListener().onUserBlogFollowFail(data.isEmpty() ? null : data.getString(QueryService.KEY_ERROR));
    }

    @Override
    protected void onFinish(Bundle data) {
        boolean follow = data.getBoolean(QueryService.KEY_FOLLOW);
        String url = data.getString(QueryService.KEY_URL);
        getListener().onUserBlogFollowSuccess(follow, url);
    }

    public interface UserBlogFollowListener extends OnTokenInvalidatedListener {
        void onUserBlogFollowFail(String error);

        /**
         * @param follow <code>true</code> if user started to follow current blog
         * @param url
         */
        void onUserBlogFollowSuccess(boolean follow, String url);
    }
}
