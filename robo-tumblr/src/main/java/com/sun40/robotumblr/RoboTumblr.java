package com.sun40.robotumblr;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.token.AccessToken;
import com.sun40.robotumblr.token.ConsumerToken;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;


/**
 * Created by Alexander Sokol
 * on 25.08.15 17:24.
 */
public final class RoboTumblr {

    private static final String TAG = "RoboTumblr";
    private static final String TOKEN_FILE = "tokens";

    private static final String KEY_ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";
    private static final String KEY_ACCESS_TOKEN_SECRET = "ACCESS_TOKEN_SECRET";

    static RestAdapter.LogLevel LOG_LEVEL = RestAdapter.LogLevel.BASIC;

    private static volatile RoboTumblr sInstanse;

    private ApiService mApiService;
    private OAuthService mOAuthService;
    private OAuthClient mOAuthClient;
    private ConsumerToken mConsumerToken;
    private AccessToken mAccessToken;

    private RoboTumblr() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiService.API_ENDPOINT)
                .setLogLevel(RoboTumblr.LOG_LEVEL)
                .build();

        mApiService = restAdapter.create(ApiService.class);
    }


    public static RoboTumblr getInstanse(Context context) {
        synchronized (RoboTumblr.class) {
            if (sInstanse == null)
                sInstanse = new RoboTumblr();
            sInstanse.update(context);
        }
        return sInstanse;
    }


    public static void setRetrofitLogLevel(RestAdapter.LogLevel level) {
        if (level != null)
            LOG_LEVEL = level;
    }

    public static void setVerbose(boolean verbose) {
        L.VERBOSE = verbose;
    }

    public static void clearTokenData(Context context) {
        getTokenPreferences(context).edit().clear().apply();
    }


    public static ConsumerToken getConsumerToken(Context context) {
        String consumerKey = context.getString(R.string.consumer_key);
        String consumerSecret = context.getString(R.string.consumer_secret_key);
        if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret))
            throw new IllegalArgumentException("consumer or consumer secret keys are not defined");
        return new ConsumerToken(consumerKey, consumerSecret);
    }


    public static void setAccessToken(Context context, AccessToken accessToken) {
        getTokenPreferences(context).edit().putString(KEY_ACCESS_TOKEN_KEY, accessToken.getToken()).apply();
        getTokenPreferences(context).edit().putString(KEY_ACCESS_TOKEN_SECRET, accessToken.getSecret()).apply();
    }


    public static AccessToken getAccessToken(Context context) {
        String token = getTokenPreferences(context).getString(KEY_ACCESS_TOKEN_KEY, null);
        String secret = getTokenPreferences(context).getString(KEY_ACCESS_TOKEN_SECRET, null);
        if (token == null || secret == null)
            return null;
        else
            return new AccessToken(token, secret);
    }


    public static boolean isUserLoggedIn(Context context) {
        return getAccessToken(context) != null;
    }


    private static SharedPreferences getTokenPreferences(Context context) {
        return context.getSharedPreferences(TOKEN_FILE, Context.MODE_PRIVATE);
    }


    private void update(Context context) {
        if (mConsumerToken == null)
            mConsumerToken = getConsumerToken(context);

        AccessToken savedToken = getAccessToken(context);
        boolean needNewInstanse = false;

        if (mAccessToken != null) {
            if (savedToken == null)
                mAccessToken = null;
            else {
                if (!mAccessToken.equals(savedToken)) {
                    mAccessToken = savedToken;
                    needNewInstanse = true;
                }
            }
        } else if (savedToken != null) {
            mAccessToken = savedToken;
            needNewInstanse = true;
        }

        if (mAccessToken != null && needNewInstanse) {
            mOAuthClient = new OAuthClient(mConsumerToken, mAccessToken);
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ApiService.API_ENDPOINT)
                    .setClient(mOAuthClient)
                    .setLogLevel(RoboTumblr.LOG_LEVEL)
                    .build();

            mOAuthService = restAdapter.create(OAuthService.class);
        }

        if (mAccessToken == null) {
            mOAuthClient = null;
            mOAuthService = null;
        }
    }


    /**
     * Request to get Blog info, has additional params while user is authorized see {@link com.sun40.robotumblr.model.Blog}
     *
     * @param hostname host name like temp.tumblr.com
     * @return requested blog {@link Blog} or <code>null</code> on error
     * @throws RetrofitError while error occurred
     */
    public Blog blogInfo(@NonNull String hostname) throws RetrofitError {
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


    /**
     * Request to get Blog avatar, use size param to get specified avatar
     *
     * @param hostname host name like temp.tumblr.com
     * @param size     avatar size of {@link com.sun40.robotumblr.TumblrExtras.TumblrSize}, by default use {@link com.sun40.robotumblr.TumblrExtras.Size#SIZE_UNDEFINED} which returns 64x64 avatar
     * @return Url for blog avatar or <code>null</code>
     * @throws RetrofitError while error occurred
     */
    public String blogAvatar(@NonNull String hostname, @TumblrExtras.TumblrSize int size) throws RetrofitError {
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


    /**
     * Request to get Blog avatar with default size 64x64
     *
     * @param hostname host name like temp.tumblr.com
     * @return Url for blog avatar or <code>null</code>
     * @throws RetrofitError while error occurred
     */
    public String blogAvatar(@NonNull String hostname) throws RetrofitError {
        return blogAvatar(hostname, TumblrExtras.Size.SIZE_UNDEFINED);
    }


    /**
     * Request to get specified blog likes by username before timestamp, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param username user name like temp.tumblr.com, were user name equals main user blog name
     * @param limit    likes limit per page
     * @param offset   likes offset from start
     * @return List of liked posts
     * @throws RetrofitError while error occurred
     */
    public List<Post> blogLikes(@NonNull String username, int limit, int offset) throws RetrofitError {
        username = Utils.checkHostname(username);
        limit = Utils.checkLimit(limit);
        offset = Utils.checkOffset(offset);

        ResponseContainer.BlogLikesContainer container = mApiService.blogLikes(username,
                limit < 0 ? null : limit,
                offset < 0 ? null : offset,
                mConsumerToken.getToken());
        return Utils.extractPosts(container.response.liked_posts);
    }


    /**
     * Request to get specified blog likes by username after timestamp, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param username  host name like temp.tumblr.com, were user name equals main user blog name
     * @param limit     likes limit per page
     * @param timestamp unix epoch time stamp
     * @return List of liked posts
     * @throws RetrofitError while error occurred
     */
    public List<Post> blogLikesBefore(@NonNull String username, int limit, long timestamp) throws RetrofitError {
        username = Utils.checkHostname(username);
        limit = Utils.checkLimit(limit);
        timestamp = Utils.checkTimestamp(timestamp);

        ResponseContainer.BlogLikesContainer container = mApiService.blogLikesBefore(username,
                limit < 0 ? null : limit,
                timestamp < 0 ? null : timestamp,
                mConsumerToken.getToken());

        return Utils.extractPosts(container.response.liked_posts);
    }


    /**
     * Request to get specified blog likes by username after timestamp, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param username  host name like temp.tumblr.com, were user name equals main user blog name
     * @param limit     likes limit per page
     * @param timestamp unix epoch time stamp
     * @return List of liked posts
     * @throws RetrofitError while error occurred
     */
    public List<Post> blogLikesAfter(@NonNull String username, int limit, long timestamp) throws RetrofitError {
        username = Utils.checkHostname(username);
        limit = Utils.checkLimit(limit);
        timestamp = Utils.checkTimestamp(timestamp);

        ResponseContainer.BlogLikesContainer container = mApiService.blogLikesAfter(username,
                limit < 0 ? null : limit,
                timestamp < 0 ? null : timestamp,
                mConsumerToken.getToken());

        return Utils.extractPosts(container.response.liked_posts);
    }


    /**
     * Request to get blog posts
     *
     * @param hostname   blog hostname like temp.tumblr.com
     * @param type       The type of post to return. Specify one of the following: {@link com.sun40.robotumblr.TumblrExtras.Post#TEXT}, {@link com.sun40.robotumblr.TumblrExtras.Post#QUOTE}, {@link com.sun40.robotumblr.TumblrExtras.Post#LINK},
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#ANSWER}, {@link com.sun40.robotumblr.TumblrExtras.Post#VIDEO}, {@link com.sun40.robotumblr.TumblrExtras.Post#AUDIO},
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#PHOTO}, {@link com.sun40.robotumblr.TumblrExtras.Post#CHAT} or null to get all posts
     * @param tag        Limits the response to posts with the specified tag may be null
     * @param limit      The number of posts to return: 1–20, inclusive or -1 to use default value 20
     * @param offset     RawPost number to start at or -1 to use default value 0
     * @param reblogInfo Indicates whether to return reblog information (specify true or false). Returns the various reblogged_ fields. <i>default: false</i>
     * @param notesInfo  Indicates whether to return notes information (specify true or false). Returns note count and note metadata. <i>default: false<i/>
     * @param filter     Specifies the post format to return, other than HTML:
     *                   {@link com.sun40.robotumblr.TumblrExtras.Filter#TEXT} – Plain text, no HTML
     *                   {@link com.sun40.robotumblr.TumblrExtras.Filter#RAW} – As entered by the user (no post-processing); if the user writes in Markdown, the Markdown will be returned rather than HTML
     *                   <i>Default null - HTML</i>
     * @return List of blog posts
     */
    public List<Post> blogPosts(@NonNull String hostname,
                                @Nullable @TumblrExtras.PostType String type,
                                @Nullable String tag, int limit, int offset,
                                boolean reblogInfo, boolean notesInfo,
                                @Nullable @TumblrExtras.FilterType String filter) {

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

        if (container.response != null && container.response.blog != null && container.response.posts != null) {
            return Utils.extractPosts(container.response.posts);
        } else
            return null;
    }


    /**
     * Request to get blog posts
     *
     * @param hostname blog hostname like temp.tumblr.com
     * @param limit    The number of posts to return: 1–20, inclusive or -1 to use default value 20
     * @param offset   RawPost number to start at or -1 to use default value 0
     * @return List of blog posts
     */
    public List<Post> blogPosts(@NonNull String hostname, int limit, int offset) throws RetrofitError {
        return blogPosts(hostname, null, null, limit, offset, false, false, null);
    }


    /**
     * Request to get blog posts
     *
     * @param hostname blog hostname like temp.tumblr.com
     * @param type     The type of post to return. Specify one of the following: {@link com.sun40.robotumblr.TumblrExtras.Post#TEXT}, {@link com.sun40.robotumblr.TumblrExtras.Post#QUOTE}, {@link com.sun40.robotumblr.TumblrExtras.Post#LINK},
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#ANSWER}, {@link com.sun40.robotumblr.TumblrExtras.Post#VIDEO}, {@link com.sun40.robotumblr.TumblrExtras.Post#AUDIO},
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#PHOTO}, {@link com.sun40.robotumblr.TumblrExtras.Post#CHAT} or null to get all posts
     * @param limit    The number of posts to return: 1–20, inclusive or -1 to use default value 20
     * @param offset   RawPost number to start at or -1 to use default value 0
     * @return List of blog posts
     */
    public List<Post> blogPosts(@NonNull String hostname,  @Nullable @TumblrExtras.PostType String type, int limit, int offset) throws RetrofitError {
        return blogPosts(hostname, type, null, limit, offset, false, false, null);
    }

}
