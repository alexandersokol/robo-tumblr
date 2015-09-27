package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.Post;

import java.util.List;

/**
 * Created by Alexander Sokol
 * on 03.09.15 18:28.
 */
public class BlogLikesReceiver extends BaseResultReceiver<BlogLikesReceiver.BlogLikesListener> {

    public BlogLikesReceiver(Handler handler) {
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
        String error = data.isEmpty() ? null : data.getString(QueryService.KEY_ERROR);
        getListener().onBlogLikesFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        List<Post> posts = data.getParcelableArrayList(QueryService.KEY_POSTS);
        if (posts == null) {
            getListener().onBlogLikesFail(null);
            return;
        }

        int likedCount = data.getInt(QueryService.KEY_LIKED_COUNT);
        int limit = data.getInt(QueryService.KEY_LIMIT);
        int offset = data.getInt(QueryService.KEY_OFFSET);
        long timestamp = data.getLong(QueryService.KEY_TIMESTAMP);

        getListener().onBlogLikesSuccess(posts, likedCount, limit, offset, timestamp);
    }

    public interface BlogLikesListener extends OnTokenInvalidatedListener {
        void onBlogLikesFail(String error);

        void onBlogLikesSuccess(List<Post> posts, int likedCount, int limit, int offset, long timestamp);
    }

}
