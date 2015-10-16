package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.User;

/**
 * Created by Alexander Sokol
 * on 14.09.15 12:06.
 */
public class UserInfoReceiver extends BaseResultReceiver<UserInfoReceiver.UserInfoListener> {

    public UserInfoReceiver(Handler handler) {
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
        getListener().onUserInfoFail(data.isEmpty() ? null : data.getString(TumblrService.KEY_ERROR));
    }

    @Override
    protected void onFinish(Bundle data) {
        User user = data.getParcelable(TumblrService.KEY_USER);
        if (user != null)
            getListener().onUserInfoSuccess(user);
        else
            getListener().onUserInfoFail(null);
    }

    public interface UserInfoListener extends OnTokenInvalidatedListener {
        void onUserInfoFail(String error);

        void onUserInfoSuccess(User user);
    }
}
