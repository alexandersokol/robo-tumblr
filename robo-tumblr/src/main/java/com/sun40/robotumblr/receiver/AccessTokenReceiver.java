package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;

/**
 * Created by Alexander Sokol
 * on 25.08.15 18:21.
 */
public class AccessTokenReceiver extends BaseResultReceiver<AccessTokenReceiver.OnAccessTokenListener> {


    public AccessTokenReceiver(Handler handler) {
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
        getListener().onAccessTokenFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        getListener().onAccessTokenSuccess();
    }

    public interface OnAccessTokenListener extends OnTokenInvalidatedListener {
        void onAccessTokenFail(String error);

        void onAccessTokenSuccess();
    }

}
