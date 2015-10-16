package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Post;

import java.util.List;

/**
 * Created by Alexander Sokol
 * on 09.09.15 9:37.
 */
public class BlogQueueReceiver extends BaseResultReceiver<BlogQueueReceiver.BlogQueueListener> {


    public BlogQueueReceiver(Handler handler) {
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
        String error = data.isEmpty() ? null : data.getString(TumblrService.KEY_ERROR);
        getListener().onBlogQueuedFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        List<Post> posts = data.getParcelableArrayList(TumblrService.KEY_POSTS);
        if (posts == null) {
            getListener().onBlogQueuedFail(null);
        } else {
            int offset = data.getInt(TumblrService.KEY_OFFSET);
            int limit = data.getInt(TumblrService.KEY_LIMIT);
            String filter = data.getString(TumblrService.KEY_FILTER);
            getListener().onBlogQueuedSuccess(posts, limit, offset, filter);
        }
    }

    public interface BlogQueueListener extends OnTokenInvalidatedListener {
        void onBlogQueuedFail(String error);

        void onBlogQueuedSuccess(List<Post> posts, int limit, int offser, String filter);
    }
}
