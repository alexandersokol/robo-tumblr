package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.TumblrExtras;

/**
 * Created by Alexander Sokol
 * on 02.09.15 12:04.
 */
public class BlogAvatarReceiver extends BaseResultReceiver<BlogAvatarReceiver.BlogAvatarListener> {


    public BlogAvatarReceiver(Handler handler) {
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
        getListener().onBlogAvatarFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        String avatarUrl = data.getString(QueryService.KEY_BLOG_AVATAR);
        if (!TextUtils.isEmpty(avatarUrl)) {
            int size = data.getInt(QueryService.KEY_SIZE, TumblrExtras.Size.SIZE_64);
            if(size == -1)
                size = TumblrExtras.Size.SIZE_64;
            getListener().onBlogAvatarSuccess(avatarUrl, size);
        }
        else
            getListener().onBlogAvatarFail(null);
    }


    public interface BlogAvatarListener extends OnTokenInvalidatedListener {
        void onBlogAvatarFail(String error);

        void onBlogAvatarSuccess(String avatarUrl, int size);
    }

}
