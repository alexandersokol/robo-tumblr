package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;

import java.util.Stack;

/**
 * Created by Alexander Sokol
 * on 25.08.15 16:53.
 */
abstract class BaseResultReceiver<T extends OnTokenInvalidatedListener> extends ResultReceiver {
    private T mListener;
    private Stack<ResultBundle> mResultStack = new Stack<>();

    public BaseResultReceiver(Handler handler) {
        super(handler);
    }

    public void attach(T listener) {
        mListener = listener;
        if (mListener != null) {
            while (!mResultStack.isEmpty()) {
                ResultBundle resultBundle = mResultStack.pop();
                onReceiveResult(resultBundle.resultCode, resultBundle.resultData);
            }
        }
    }

    public void detach() {
        mListener = null;
    }


    protected T getListener() {
        return mListener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mListener != null) {
            switch (resultCode) {
                case QueryService.CODE_START:
                    onStart(resultData);
                    break;
                case QueryService.CODE_PROGRESS:
                    onProgress(resultData);
                    break;
                case QueryService.CODE_ERROR:
                    onError(resultData);
                    break;
                case QueryService.CODE_SUCCESS:
                    onFinish(resultData);
                    break;
                case QueryService.CODE_TOKEN_EXPIRED:
                    mListener.onTokenInvalidated();
                    break;
                case QueryService.CODE_NOT_AUTHORIZED:
                    mListener.onUserNotAuthorized();
                    break;
            }
        } else {
            mResultStack.push(new ResultBundle(resultCode, resultData));
        }
    }

    protected abstract void onStart(Bundle data);

    protected abstract void onProgress(Bundle data);

    protected abstract void onError(Bundle data);

    protected abstract void onFinish(Bundle data);

    private static class ResultBundle {
        int resultCode;
        Bundle resultData;

        ResultBundle(int resultCode, Bundle resultData) {
            this.resultCode = resultCode;
            this.resultData = resultData;
        }
    }
}
