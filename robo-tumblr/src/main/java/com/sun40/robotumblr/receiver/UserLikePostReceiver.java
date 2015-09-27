package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;

/**
 * Created by Alexander Sokol
 * on 14.09.15 17:39.
 */
public class UserLikePostReceiver extends BaseResultReceiver<UserLikePostReceiver.UserLikePostListener> {

    public UserLikePostReceiver(Handler handler) {
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
        getListener().onUserLikePostFail(data.isEmpty() ? null : data.getString(QueryService.KEY_ERROR));
    }

    @Override
    protected void onFinish(Bundle data) {
        boolean like = data.getBoolean(QueryService.KEY_LIKE);
        long id = data.getLong(QueryService.KEY_ID);
        String reblogKey = data.getString(QueryService.KEY_REBLOG);
        getListener().onUserLikePostSuccess(like, id, reblogKey);
    }

    public interface UserLikePostListener extends OnTokenInvalidatedListener {
        void onUserLikePostFail(String error);

        void onUserLikePostSuccess(boolean like, long postId, String reblogKey);
    }
}
