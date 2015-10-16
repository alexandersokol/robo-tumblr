package com.sun40.robotumblr.receiver;

import android.os.Bundle;
import android.os.Handler;

import com.sun40.robotumblr.OnTokenInvalidatedListener;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 16.09.15 11:57.
 */
public class SearchReceiver extends BaseResultReceiver<SearchReceiver.SearchListener> {

    public SearchReceiver(Handler handler) {
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
        getListener().onSearchFail(data.isEmpty() ? null : data.getString(TumblrService.KEY_ERROR));
    }

    @Override
    protected void onFinish(Bundle data) {
        Blog[] blogs = (Blog[]) data.getParcelableArray(TumblrService.KEY_BLOG);
        Tag[] tags = (Tag[]) data.getParcelableArray(TumblrService.KEY_TAG);
        List<Blog> blogList;
        if (blogs != null)
            blogList = Arrays.asList(blogs);
        else
            blogList = new ArrayList<>();

        List<Tag> tagList;
        if (tags != null)
            tagList = Arrays.asList(tags);
        else
            tagList = new ArrayList<>();

        String searchType = data.getString(TumblrService.KEY_SEARCH_TYPE);
        if (searchType != null && (!searchType.equals(TumblrExtras.Search.BLOGS) || !searchType.equals(TumblrExtras.Search.TAGS))) {
            searchType = TumblrExtras.Search.ANY;
        }

        if (searchType == null)
            searchType = TumblrExtras.Search.ANY;

        //noinspection ResourceType
        getListener().onSearchSuccess(blogList, tagList, searchType);
    }

    public interface SearchListener extends OnTokenInvalidatedListener {
        void onSearchFail(String error);

        void onSearchSuccess(List<Blog> blogs, List<Tag> tags, @TumblrExtras.SearchType String searchType);
    }
}
