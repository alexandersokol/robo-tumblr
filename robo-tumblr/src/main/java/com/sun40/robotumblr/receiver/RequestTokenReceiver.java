package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.token.RequestToken;

/**
 * Created by Alexander Sokol
 * on 25.08.15 16:52.
 */
public class RequestTokenReceiver extends BaseResultReceiver<RequestTokenReceiver.OnRequestTokenListener> {


    public RequestTokenReceiver(Handler handler) {
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
        getListener().onRequestTokenFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        RequestToken requestToken = data.getParcelable(QueryService.KEY_TOKEN);
        getListener().onRequestTokenSuccess(requestToken);
    }


    public interface OnRequestTokenListener extends OnTokenInvalidatedListener {
        void onRequestTokenSuccess(RequestToken requestToken);

        void onRequestTokenFail(String error);
    }

}
