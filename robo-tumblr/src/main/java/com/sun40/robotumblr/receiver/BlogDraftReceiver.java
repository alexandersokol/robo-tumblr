package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Post;

import java.util.List;

/**
 * Created by Alexander Sokol
 * on 09.09.15 10:01.
 */
public class BlogDraftReceiver extends BaseResultReceiver<BlogDraftReceiver.BlogDraftListener> {

    public BlogDraftReceiver(Handler handler) {
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
        getListener().onBlogDraftFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        List<Post> posts = data.getParcelableArrayList(TumblrService.KEY_POSTS);
        if (posts != null) {
            long beforeId = data.getLong(TumblrService.KEY_ID, -1);
            String filter = data.getString(TumblrService.KEY_FILTER);
            getListener().onBlogDraftSuccess(posts, beforeId, filter);
        } else {
            getListener().onBlogDraftFail(null);
        }
    }

    public interface BlogDraftListener extends OnTokenInvalidatedListener {
        void onBlogDraftFail(String error);

        void onBlogDraftSuccess(List<Post> posts, long beforeId, String filter);
    }
}
