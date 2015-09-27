package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.RawPost;

import java.util.List;

/**
 * Created by Alexander Sokol
 * on 08.09.15 13:45.
 */
public class BlogPostByIdReceiver extends BaseResultReceiver<BlogPostByIdReceiver.BlogPostByIdListener> {


    public BlogPostByIdReceiver(Handler handler) {
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
        getListener().onBlogPostByIdFail(error);
    }

    @Override
    protected void onFinish(Bundle data) {
        Blog blog = data.getParcelable(QueryService.KEY_BLOG);
        List<Post> posts = data.getParcelableArrayList(QueryService.KEY_POSTS);
        if (posts != null && !posts.isEmpty())
            getListener().onBlogPostByIdSuccess(blog, posts.get(0));
        else
            getListener().onBlogPostByIdFail(null);
    }

    public interface BlogPostByIdListener extends OnTokenInvalidatedListener {
        void onBlogPostByIdFail(String error);

        void onBlogPostByIdSuccess(Blog blog, Post post);
    }

}
