package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Post;

import java.util.List;

/**
 * Created by Alexander Sokol
 * on 14.09.15 16:02.
 */
public class UserLikesReceiver extends BaseResultReceiver<UserLikesReceiver.UserLikesListener> {

    public UserLikesReceiver(Handler handler) {
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
        getListener().onUserLikesFail(data.isEmpty() ? null : data.getString(TumblrService.KEY_ERROR));
    }

    @Override
    protected void onFinish(Bundle data) {
        List<Post> posts = data.getParcelableArrayList(TumblrService.KEY_POSTS);
        if (posts != null) {
            int total = data.getInt(TumblrService.KEY_TOTAL);
            int limit = data.getInt(TumblrService.KEY_LIMIT);
            int offset = data.getInt(TumblrService.KEY_OFFSET);
            long before = data.getLong(TumblrService.KEY_BEFORE);
            long after = data.getLong(TumblrService.KEY_AFTER);
            getListener().onUserLikesSuccess(posts, total, limit, offset, before, after);

        } else
            getListener().onUserLikesFail(null);
    }

    public interface UserLikesListener extends OnTokenInvalidatedListener {
        void onUserLikesFail(String error);

        void onUserLikesSuccess(List<Post> posts, int total, int limit, int offset, long before, long after);
    }
}
