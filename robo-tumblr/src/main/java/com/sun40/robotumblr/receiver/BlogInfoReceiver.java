package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Blog;

/**
 * Created by Alexander Sokol
 * on 01.09.15 17:13.
 */
public class BlogInfoReceiver extends BaseResultReceiver<BlogInfoReceiver.OnBlogInfoListener> {


    public BlogInfoReceiver(Handler handler) {
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
        getListener().onBlogInfoFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        Blog blog = data.getParcelable(TumblrService.KEY_BLOG);
        if (blog == null)
            getListener().onBlogInfoFail(null);
        else
            getListener().onBlogInfoSuccess(blog);
    }

    public interface OnBlogInfoListener extends OnTokenInvalidatedListener {
        void onBlogInfoFail(String error);

        void onBlogInfoSuccess(Blog blog);
    }

}
