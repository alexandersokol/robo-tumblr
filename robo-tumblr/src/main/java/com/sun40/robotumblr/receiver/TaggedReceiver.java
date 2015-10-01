package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.Post;

import java.util.List;

/**
 * Created by Alexander Sokol
 * on 16.09.15 10:52.
 */
public class TaggedReceiver extends BaseResultReceiver<TaggedReceiver.TaggedListener> {

    public TaggedReceiver(Handler handler) {
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
        getListener().onTaggedFail(data.isEmpty() ? null : data.getString(QueryService.KEY_ERROR));
    }

    @Override
    protected void onFinish(Bundle data) {
        List<Post> posts = data.getParcelableArrayList(QueryService.KEY_POSTS);
        if (posts != null) {
            String tag = data.getString(QueryService.KEY_TAG);
            long before = data.getLong(QueryService.KEY_BEFORE, -1);
            int limit = data.getInt(QueryService.KEY_LIMIT, -1);
            String filter = data.getString(QueryService.KEY_FILTER);
            getListener().onTaggedSuccess(posts, tag, before, limit, filter);
        } else
            getListener().onTaggedFail(null);
    }

    public interface TaggedListener extends OnTokenInvalidatedListener {
        void onTaggedFail(String error);

        void onTaggedSuccess(List<Post> posts, String tag, long before, int limit, String filter);
    }
}
