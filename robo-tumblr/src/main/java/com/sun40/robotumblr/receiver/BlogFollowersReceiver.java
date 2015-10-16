package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.SimpleUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 07.09.15 11:10.
 */
public class BlogFollowersReceiver extends BaseResultReceiver<BlogFollowersReceiver.BlogFollowersListener> {

    public BlogFollowersReceiver(Handler handler) {
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
        getListener().onBlogFollowersFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        int limit = data.getInt(TumblrService.KEY_LIMIT);
        int offset = data.getInt(TumblrService.KEY_OFFSET);
        int totalFollowers = data.getInt(TumblrService.KEY_TOTAL);
        SimpleUser[] users = (SimpleUser[]) data.getParcelableArray(TumblrService.KEY_USERS);
        if(users != null) {
            List<SimpleUser> userList = Arrays.asList(users);
            getListener().onBlogFollowersSuccess(userList, totalFollowers, limit, offset);
        }
        else{
            getListener().onBlogFollowersFail(null);
        }
    }

    public interface BlogFollowersListener extends OnTokenInvalidatedListener {
        void onBlogFollowersFail(String error);
        void onBlogFollowersSuccess(List<SimpleUser> users, int totalFollowers, int limit, int offset);
    }

}
