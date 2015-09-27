package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.RawPost;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 14.09.15 13:22.
 */
public class UserDashboardReceiver extends BaseResultReceiver<UserDashboardReceiver.UserDashboardListener> {


    public UserDashboardReceiver(Handler handler) {
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
        getListener().onUserDashboardFail(data.isEmpty() ? null : data.getString(QueryService.KEY_ERROR));
    }

    @Override
    protected void onFinish(Bundle data) {
        RawPost[] posts = (RawPost[]) data.getParcelableArray(QueryService.KEY_POSTS);
        if (posts != null) {
            int limit = data.getInt(QueryService.KEY_LIMIT);
            int offset = data.getInt(QueryService.KEY_OFFSET);
            String type = data.getString(QueryService.KEY_TYPE);
            long sinceId = data.getLong(QueryService.KEY_ID);
            boolean reblogInfo = data.getBoolean(QueryService.KEY_REBLOG_INFO);
            boolean notesInfo = data.getBoolean(QueryService.KEY_NOTES_INFO);
            getListener().onUserDashboardSuccess(Arrays.asList(posts), limit, offset, type, sinceId, reblogInfo, notesInfo);
        } else
            getListener().onUserDashboardFail(null);

    }

    public interface UserDashboardListener extends OnTokenInvalidatedListener {
        void onUserDashboardFail(String error);

        void onUserDashboardSuccess(List<RawPost> posts, int limit, int offset, String type, long sinceId, boolean reblogInfo, boolean notesInfo);
    }
}
