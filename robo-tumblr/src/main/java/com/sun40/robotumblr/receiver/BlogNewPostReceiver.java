package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;

/**
 * Created by Alexander Sokol
 * on 09.09.15 12:40.
 */
public class BlogNewPostReceiver extends BaseResultReceiver<BlogNewPostReceiver.BlogPostListener> {

    public BlogNewPostReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onStart(Bundle data) {
        getListener().onBlogPostStart();
    }

    @Override
    protected void onProgress(Bundle data) {
        int value = data.getInt(QueryService.KEY_PROGRESS, 0);
        getListener().onBlogPostProgress(value);
    }

    @Override
    protected void onError(Bundle data) {
        String error = data.isEmpty() ? null : data.getString(QueryService.KEY_ERROR);
        getListener().onBlogPostFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        long id = data.getLong(QueryService.KEY_ID, 0);
        if(id != 0)
            getListener().onBlogPostSuccess(id);
        else
            getListener().onBlogPostFail(null);
    }

    public interface BlogPostListener extends OnTokenInvalidatedListener {
        void onBlogPostStart();

        void onBlogPostProgress(int value);

        void onBlogPostFail(String error);

        void onBlogPostSuccess(long id);
    }
}
