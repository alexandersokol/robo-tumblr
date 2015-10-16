package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.TumblrService;

/**
 * Created by Alexander Sokol
 * on 11.09.15 13:06.
 */
public class BlogReblogReceiver extends BaseResultReceiver<BlogReblogReceiver.BlogReblogListener> {


    public BlogReblogReceiver(Handler handler) {
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
        getListener().onReblogFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        long id = data.getLong(TumblrService.KEY_ID, 0);
        if (id != 0)
            getListener().onReblogSuccess(id);
        else
            getListener().onReblogFail(null);
    }


    public interface BlogReblogListener extends OnTokenInvalidatedListener {
        void onReblogFail(String error);

        void onReblogSuccess(long id);
    }

}
