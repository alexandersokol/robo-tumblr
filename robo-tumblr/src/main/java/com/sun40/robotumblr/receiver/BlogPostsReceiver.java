package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.RawPost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 08.09.15 10:05.
 */
public class BlogPostsReceiver extends BaseResultReceiver<BlogPostsReceiver.BlogPostsListener> {


    public BlogPostsReceiver(Handler handler) {
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
        getListener().onBlogPostsFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        Blog blog = data.getParcelable(QueryService.KEY_BLOG);
        ArrayList<Post> posts = data.getParcelableArrayList(QueryService.KEY_POSTS);
        if(posts == null){
            getListener().onBlogPostsFail(null);
            return;
        }
        int limit = data.getInt(QueryService.KEY_LIMIT);
        int offset = data.getInt(QueryService.KEY_OFFSET);
        String type = data.getString(QueryService.KEY_TYPE);
        String tag = data.getString(QueryService.KEY_TAG);
        boolean reblogInfo = data.getBoolean(QueryService.KEY_REBLOG_INFO);
        boolean notesInfo = data.getBoolean(QueryService.KEY_NOTES_INFO);
        String filter = data.getString(QueryService.KEY_FILTER);
        int total = data.getInt(QueryService.KEY_TOTAL, 0);
        getListener().onBlogPostsSuccess(blog, posts, limit, offset, tag, type, reblogInfo, notesInfo, filter, total);
    }

    public interface BlogPostsListener extends OnTokenInvalidatedListener {
        void onBlogPostsFail(String error);
        void onBlogPostsSuccess(Blog blog, List<Post> posts, int limit, int offset, String tag, String type, boolean reblogInfo, boolean notesInfo, String filter, int total);
    }
}
