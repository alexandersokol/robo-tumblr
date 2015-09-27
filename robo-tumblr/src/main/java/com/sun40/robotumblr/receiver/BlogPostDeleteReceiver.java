package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;

/**
 * Created by Alexander Sokol
 * on 11.09.15 13:09.
 */
public class BlogPostDeleteReceiver extends BaseResultReceiver<BlogPostDeleteReceiver.BlogPostDeleteListener> {


    public BlogPostDeleteReceiver(Handler handler) {
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
        getListener().onBlogPostDeleteFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        long id = data.getLong(QueryService.KEY_ID, 0);
        if (id != 0)
            getListener().onBlogPostDeleteSuccess(id);
        else
            getListener().onBlogPostDeleteFail(null);
    }

    public interface BlogPostDeleteListener extends OnTokenInvalidatedListener {
        void onBlogPostDeleteFail(String error);

        void onBlogPostDeleteSuccess(long id);
    }
}
