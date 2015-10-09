package com.sun40.robotumblr;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.token.AccessToken;
import com.sun40.robotumblr.token.ConsumerToken;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by Alexander Sokol
 * on 09.10.15.
 */
class RequestCore {

    private ConsumerToken mConsumerToken;
    private AccessToken mAccessToken;
    private ApiService mApiService;
    private OAuthClient mOAuthClient;
    private OAuthService mOAuthService;


    RequestCore(ConsumerToken consumerToken) {
        mConsumerToken = consumerToken;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiService.API_ENDPOINT)
                .setLogLevel(RoboTumblr.LOG_LEVEL)
                .build();

        mApiService = restAdapter.create(ApiService.class);
    }


    void setAccessToken(AccessToken accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            mOAuthService = null;
            mOAuthClient = null;
        } else {
            boolean needNewInstance = false;
            if (mAccessToken != null && !mAccessToken.equals(accessToken)) {
                mAccessToken = accessToken;
                needNewInstance = true;
            }

            if (mAccessToken == null) {
                mAccessToken = accessToken;
                needNewInstance = true;
            }

            if (mAccessToken != null && (needNewInstance || mOAuthService == null)) {
                mOAuthClient = new OAuthClient(mConsumerToken, mAccessToken);
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ApiService.API_ENDPOINT)
                        .setClient(mOAuthClient)
                        .setLogLevel(RoboTumblr.LOG_LEVEL)
                        .build();

                mOAuthService = restAdapter.create(OAuthService.class);
            }
        }
    }


    Blog blogInfo(@NonNull String hostname) throws RetrofitError {
        hostname = Utils.checkHostname(hostname);

        ResponseContainer.BlogContainer container;
        if (mOAuthService != null)
            container = mOAuthService.blogInfo(hostname, mConsumerToken.getToken());
        else
            container = mApiService.blogInfo(hostname, mConsumerToken.getToken());

        if (container != null && container.response != null)
            return container.response.blog;

        return null;
    }


    String blogAvatar(@NonNull String hostname, @TumblrExtras.TumblrSize int size) throws RetrofitError {
        hostname = Utils.checkHostname(hostname);

        try {
            if (size == TumblrExtras.Size.SIZE_UNDEFINED)
                mApiService.blogAvatar(hostname);
            else
                mApiService.blogAvatar(hostname, size);

        } catch (RetrofitError error) {
            if (error.getResponse() != null && error.getResponse().getStatus() == Utils.STATUS_FOUND && !TextUtils.isEmpty(error.getResponse().getUrl()))
                return error.getResponse().getUrl();
            else
                throw error;
        }

        return null;
    }


    ResponseContainer.BlogLikesContainer blogLikes(@NonNull String username, int limit, int offset) throws RetrofitError {
        username = Utils.checkHostname(username);
        limit = Utils.checkLimit(limit);
        offset = Utils.checkOffset(offset);

        return mApiService.blogLikes(username,
                limit < 0 ? null : limit,
                offset < 0 ? null : offset,
                mConsumerToken.getToken());
    }


    ResponseContainer.BlogLikesContainer blogLikesBefore(@NonNull String username, int limit, long timestamp) throws RetrofitError {
        username = Utils.checkHostname(username);
        limit = Utils.checkLimit(limit);
        timestamp = Utils.checkTimestamp(timestamp);

        return mApiService.blogLikesBefore(username,
                limit < 0 ? null : limit,
                timestamp < 0 ? null : timestamp,
                mConsumerToken.getToken());
    }


    ResponseContainer.BlogLikesContainer blogLikesAfter(@NonNull String username, int limit, long timestamp) throws RetrofitError {
        username = Utils.checkHostname(username);
        limit = Utils.checkLimit(limit);
        timestamp = Utils.checkTimestamp(timestamp);

        return mApiService.blogLikesAfter(username,
                limit < 0 ? null : limit,
                timestamp < 0 ? null : timestamp,
                mConsumerToken.getToken());
    }


    ResponseContainer.BlogPostsContainer blogPosts(@NonNull String hostname,
                                                   @Nullable @TumblrExtras.PostType String type,
                                                   @Nullable String tag, int limit, int offset,
                                                   boolean reblogInfo, boolean notesInfo,
                                                   @Nullable @TumblrExtras.FilterType String filter) throws RetrofitError {

        hostname = Utils.checkHostname(hostname);
        limit = Utils.checkLimit(limit);
        offset = Utils.checkOffset(offset);

        ResponseContainer.BlogPostsContainer container;
        if (TextUtils.isEmpty(type)) {
            if (mOAuthService == null) {
                container = mApiService.blogPosts(hostname,
                        mConsumerToken.getToken(),
                        null,
                        tag,
                        limit < 0 ? null : limit,
                        offset < 0 ? null : offset,
                        reblogInfo,
                        notesInfo,
                        TextUtils.isEmpty(filter) ? null : filter);
            } else {
                container = mOAuthService.blogPosts(hostname,
                        mConsumerToken.getToken(),
                        null,
                        tag,
                        limit < 0 ? null : limit,
                        offset < 0 ? null : offset,
                        reblogInfo,
                        notesInfo,
                        TextUtils.isEmpty(filter) ? null : filter);
            }
        } else {
            if (mOAuthService == null) {
                container = mApiService.blogPosts(hostname,
                        type,
                        mConsumerToken.getToken(),
                        null,
                        tag,
                        limit < 0 ? null : limit,
                        offset < 0 ? null : offset,
                        reblogInfo,
                        notesInfo,
                        TextUtils.isEmpty(filter) ? null : filter);
            } else {
                container = mOAuthService.blogPosts(hostname,
                        type,
                        mConsumerToken.getToken(),
                        null,
                        tag,
                        limit < 0 ? null : limit,
                        offset < 0 ? null : offset,
                        reblogInfo,
                        notesInfo,
                        TextUtils.isEmpty(filter) ? null : filter);
            }
        }

        return container;
    }


    ResponseContainer.BlogPostsContainer blogPostById(String hostname, long id, boolean reblogInfo, boolean notesInfo) throws RetrofitError {

        ResponseContainer.BlogPostsContainer container;
        if (mOAuthService == null)
            container = mApiService.blogPosts(hostname, mConsumerToken.getToken(), id, reblogInfo, notesInfo);
        else
            container = mOAuthService.blogPosts(hostname, mConsumerToken.getToken(), id, reblogInfo, notesInfo);

        if (container.response != null && container.response.posts != null && container.response.blog != null)
            return container;

        return null;
    }

}
