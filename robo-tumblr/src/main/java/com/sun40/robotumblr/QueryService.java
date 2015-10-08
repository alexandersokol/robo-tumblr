package com.sun40.robotumblr;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.RawPost;
import com.sun40.robotumblr.model.posting.AudioPostCreator;
import com.sun40.robotumblr.model.posting.ChatPostCreator;
import com.sun40.robotumblr.model.posting.LinkPostCreator;
import com.sun40.robotumblr.model.posting.PhotoPostCreator;
import com.sun40.robotumblr.model.posting.PostCreator;
import com.sun40.robotumblr.model.posting.QuotePostCreator;
import com.sun40.robotumblr.model.posting.ReblogPostCreator;
import com.sun40.robotumblr.model.posting.TextPostCreator;
import com.sun40.robotumblr.model.posting.VideoPostCreator;
import com.sun40.robotumblr.receiver.AccessTokenReceiver;
import com.sun40.robotumblr.receiver.BlogAvatarReceiver;
import com.sun40.robotumblr.receiver.BlogDraftReceiver;
import com.sun40.robotumblr.receiver.BlogFollowersReceiver;
import com.sun40.robotumblr.receiver.BlogInfoReceiver;
import com.sun40.robotumblr.receiver.BlogLikesReceiver;
import com.sun40.robotumblr.receiver.BlogNewPostReceiver;
import com.sun40.robotumblr.receiver.BlogPostByIdReceiver;
import com.sun40.robotumblr.receiver.BlogPostDeleteReceiver;
import com.sun40.robotumblr.receiver.BlogPostsReceiver;
import com.sun40.robotumblr.receiver.BlogQueueReceiver;
import com.sun40.robotumblr.receiver.BlogReblogReceiver;
import com.sun40.robotumblr.receiver.RequestTokenReceiver;
import com.sun40.robotumblr.receiver.TaggedReceiver;
import com.sun40.robotumblr.receiver.UserBlogFollowReceiver;
import com.sun40.robotumblr.receiver.UserDashboardReceiver;
import com.sun40.robotumblr.receiver.UserFollowingBlogsReceiver;
import com.sun40.robotumblr.receiver.UserInfoReceiver;
import com.sun40.robotumblr.receiver.UserLikePostReceiver;
import com.sun40.robotumblr.receiver.UserLikesReceiver;
import com.sun40.robotumblr.token.AccessToken;
import com.sun40.robotumblr.token.ConsumerToken;
import com.sun40.robotumblr.token.RequestToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;

/**
 * Created by Alexander Sokol
 * on 25.08.15 15:38.
 */
public class QueryService extends IntentService implements CountingTypedFile.FileProgressListener {

    private static final String TAG = "QueryService";

    private static final String NOT_AUTHORIZED_CODE = "401";

    private static final String ACTION_OAUTH_REQUEST_TOKEN = "action_oauth_request_token";
    private static final String ACTION_OAUTH_ACCESS_TOKEN = "action_oauth_access_token";
    private static final String ACTION_BLOG_INFO = "action_blog_info";
    private static final String ACTION_BLOG_AVATAR = "action_blog_avatar";
    private static final String ACTION_BLOG_LIKES = "action_blog_likes";
    private static final String ACTION_BLOG_LIKES_BEFORE = "action_blog_likes_before";
    private static final String ACTION_BLOG_LIKES_AFTER = "action_blog_likes_after";
    private static final String ACTION_BLOG_FOLLOWERS = "action_blog_followers";
    private static final String ACTION_BLOG_POSTS = "action_posts";
    private static final String ACTION_BLOG_POST_BY_ID = "action_blog_post_by_id";
    private static final String ACTION_BLOG_QUEUED = "action_blog_queued";
    private static final String ACTION_BLOG_DRAFT = "action_blog_draft";
    private static final String ACTION_BLOG_NEW_POST = "action_blog_new_post";
    private static final String ACTION_BLOG_EDIT_POST = "action_blog_edit_post";
    private static final String ACTION_BLOG_REBLOG_POST = "action_blog_reblog_post";
    private static final String ACTION_BLOG_DELETE_POST = "action_blog_delete_post";
    private static final String ACTION_USER_INFO = "action_user_info";
    private static final String ACTION_USER_DASHBOARD = "action_user_dashboard";
    private static final String ACTION_USER_LIKES = "action_user_likes";
    private static final String ACTION_USER_FOLLOWING_BLOGS = "action_user_following_blogs";
    private static final String ACTION_USER_BLOG_FOLLOW = "action_user_follow_blog";
    private static final String ACTION_USER_POST_LIKE = "action_user_post_like";
    private static final String ACTION_TAGGED = "action_tagged";
    private static final String ACTION_SEARCH = "action_search";

    private static final String KEY_RECEIVER = "key_receiver";

    public static final String KEY_TOKEN = "key_token";
    public static final String KEY_VERIFIER = "key_verifier";
    public static final String KEY_REQUEST_TOKEN = "key_request_token";
    public static final String KEY_HOSTNAME = "key_hostname";
    public static final String KEY_BLOG = "key_blog";
    public static final String KEY_SIZE = "key_size";
    public static final String KEY_BLOG_AVATAR = "key_blog_avatar";
    public static final String KEY_LIMIT = "key_limit";
    public static final String KEY_OFFSET = "key_offset";
    public static final String KEY_TIMESTAMP = "key_timestamp";
    public static final String KEY_LIKED_COUNT = "key_liked_count";
    public static final String KEY_POSTS = "key_posts";
    public static final String KEY_TOTAL = "key_total";
    public static final String KEY_USERS = "key_users";
    public static final String KEY_TYPE = "key_type";
    public static final String KEY_TAG = "key_tag";
    public static final String KEY_REBLOG_INFO = "key_reblog_info";
    public static final String KEY_NOTES_INFO = "key_notes_info";
    public static final String KEY_FILTER = "key_filter";
    public static final String KEY_ID = "key_id";
    public static final String KEY_PROGRESS = "key_progress";
    public static final String KEY_CREATOR = "key_creator";
    public static final String KEY_ERROR = "key_error";
    public static final String KEY_USER = "key_user";
    public static final String KEY_BEFORE = "key_before";
    public static final String KEY_AFTER = "key_after";
    public static final String KEY_FOLLOW = "key_follow";
    public static final String KEY_URL = "key_url";
    public static final String KEY_LIKE = "key_like";
    public static final String KEY_REBLOG = "key_reblog_key";
    public static final String KEY_SEARCH_TYPE = "key_search_type";

    public static final int CODE_START = 0;
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_PROGRESS = 2;
    public static final int CODE_ERROR = 3;
    public static final int CODE_TOKEN_EXPIRED = 4;
    public static final int CODE_NOT_AUTHORIZED = 5;

    private ResultReceiver mResultReceiver;
    private ConsumerToken mConsumerToken;
    private ApiService mApiService;
    private OAuthClient mOAuthClient;
    private OAuthService mOAuthService;

    private long mSummaryUploadSize;
    private long mSummaryTransferredSize;

    public QueryService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (mConsumerToken == null) {
            mConsumerToken = RoboTumblr.getConsumerToken(this);
        }

        if (mApiService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ApiService.API_ENDPOINT)
                    .setLogLevel(RoboTumblr.LOG_LEVEL)
                    .build();

            mApiService = restAdapter.create(ApiService.class);
        }

        AccessToken accessToken = RoboTumblr.getAccessToken(this);
        if (mOAuthService == null) {
            if (accessToken != null) {
                mOAuthClient = new OAuthClient(mConsumerToken, accessToken);
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ApiService.API_ENDPOINT)
                        .setClient(mOAuthClient)
                        .setLogLevel(RoboTumblr.LOG_LEVEL)
                        .build();

                mOAuthService = restAdapter.create(OAuthService.class);
            }
        }

        if (accessToken == null) {
            mOAuthService = null;
        }

        mSummaryUploadSize = -1;
        mSummaryTransferredSize = -1;

        mResultReceiver = intent.getParcelableExtra(KEY_RECEIVER);
        if (mResultReceiver == null)
            throw new IllegalArgumentException("Result receiver is null");

        switch (action) {
            case ACTION_OAUTH_REQUEST_TOKEN:
                oAuthRequestToken();
                break;

            case ACTION_OAUTH_ACCESS_TOKEN:
                oAuthAccessToken(intent);
                break;

            case ACTION_BLOG_INFO: {
                blogInfo(intent);
                break;
            }

            case ACTION_BLOG_AVATAR: {
                blogAvatar(intent);
                break;
            }

            case ACTION_BLOG_LIKES:
            case ACTION_BLOG_LIKES_BEFORE:
            case ACTION_BLOG_LIKES_AFTER: {
                blogLikes(intent);
                break;
            }

            case ACTION_BLOG_FOLLOWERS: {
                blogFollowers(intent);
                break;
            }

            case ACTION_BLOG_POSTS: {
                blogPosts(intent);
                break;
            }

            case ACTION_BLOG_POST_BY_ID: {
                blogPostById(intent);
                break;
            }

            case ACTION_BLOG_QUEUED: {
                blogQueue(intent);
                break;
            }

            case ACTION_BLOG_DRAFT: {
                blogDrafts(intent);
                break;
            }

            case ACTION_BLOG_EDIT_POST:
            case ACTION_BLOG_NEW_POST: {
                newPost(intent);
                break;
            }

            case ACTION_BLOG_REBLOG_POST: {
                reblogPost(intent);
                break;
            }

            case ACTION_BLOG_DELETE_POST: {
                deletePost(intent);
                break;
            }

            case ACTION_USER_INFO: {
                userInfo();
                break;
            }

            case ACTION_USER_DASHBOARD: {
                userDashboard(intent);
                break;
            }

            case ACTION_USER_LIKES: {
                userLikes(intent);
                break;
            }

            case ACTION_USER_FOLLOWING_BLOGS: {
                userFollowingBlogs(intent);
                break;
            }

            case ACTION_USER_BLOG_FOLLOW: {
                userFollowBlog(intent);
                break;
            }

            case ACTION_USER_POST_LIKE: {
                userLikePost(intent);
                break;
            }

            case ACTION_TAGGED: {
                tagged(intent);
                break;
            }

            case ACTION_SEARCH: {
                search(intent);
                break;
            }
        }

    }


    private void search(Intent intent) {
        String param = intent.getStringExtra(KEY_TAG);
        String searchType = intent.getStringExtra(KEY_SEARCH_TYPE);

        if (TextUtils.isEmpty(param)) {
            L.e(TAG, "param is empty");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        try {
            ResponseContainer.SearchContainer container;
            if (TextUtils.isEmpty(searchType))
                container = mApiService.search(param, mConsumerToken.getToken());
            else
                container = mApiService.search(param, searchType, mConsumerToken.getToken());

            if (container != null && container.response != null) {
                Bundle data = new Bundle();
                data.putParcelableArray(KEY_BLOG, container.response.blogs);
                data.putParcelableArray(KEY_TAG, container.response.tags);
                data.putString(KEY_SEARCH_TYPE, searchType);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }
        } catch (RetrofitError error) {
            handleError("search", error);
        }
    }


    private void tagged(Intent intent) {
        String tag = intent.getStringExtra(KEY_TAG);
        long before = intent.getLongExtra(KEY_BEFORE, -1);
        int limit = intent.getIntExtra(KEY_LIMIT, -1);
        String filter = intent.getStringExtra(KEY_FILTER);

        if (TextUtils.isEmpty(tag)) {
            L.e(TAG, "tag is empty");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        ResponseContainer.TaggedContainer container = null;

        try {
            if (mOAuthService != null) {
                container = mOAuthService.tagged(tag,
                        before < 0 ? null : before,
                        limit < 0 ? null : limit,
                        filter,
                        mConsumerToken.getToken());
            } else {
                container = mApiService.tagged(tag,
                        before < 0 ? null : before,
                        limit < 0 ? null : limit,
                        filter,
                        mConsumerToken.getToken());
            }

            if (container != null && container.response != null) {
                Bundle data = new Bundle();
                data.putParcelableArrayList(KEY_POSTS, extractPosts(container.response));
                data.putString(KEY_TAG, tag);
                data.putLong(KEY_BEFORE, before);
                data.putInt(KEY_LIMIT, limit);
                data.putString(KEY_FILTER, filter);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                L.e(TAG, "response is null");
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }

        } catch (RetrofitError error) {
            handleError("tagged", error);
        }
    }


    private void userLikePost(Intent intent) {
        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        long id = intent.getLongExtra(KEY_ID, -1);
        String reblogKey = intent.getStringExtra(KEY_REBLOG);
        boolean like = intent.getBooleanExtra(KEY_LIKE, false);

        if (TextUtils.isEmpty(reblogKey)) {
            L.e(TAG, "reblog key is empty");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (id < 0) {
            L.e(TAG, "post id is empty");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(TumblrExtras.Params.ID, String.valueOf(id));
        map.put(TumblrExtras.Params.REBLOG_KEY, reblogKey);

        mOAuthClient.addPostParameters(map);

        try {

            if (like)
                mOAuthService.userLikePost(id, reblogKey);
            else
                mOAuthService.userUnlikePost(id, reblogKey);

            Bundle data = new Bundle();
            data.putBoolean(KEY_LIKE, like);
            data.putLong(KEY_ID, id);
            data.putString(KEY_REBLOG, reblogKey);
            mResultReceiver.send(CODE_SUCCESS, data);

        } catch (RetrofitError error) {
            handleError("user like post ", error);
        }
    }


    private void userFollowBlog(Intent intent) {
        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        boolean follow = intent.getBooleanExtra(KEY_FOLLOW, false);
        String url = intent.getStringExtra(KEY_URL);

        if (TextUtils.isEmpty(url)) {
            L.e(TAG, "userFollowBlog url is empty");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(TumblrExtras.Params.URL, url);
        mOAuthClient.addPostParameters(map);

        try {

            if (follow)
                mOAuthService.userFollowBlog(url);
            else
                mOAuthService.userUnfollowBlog(url);

            Bundle data = new Bundle();
            data.putBoolean(KEY_FOLLOW, follow);
            data.putString(KEY_URL, url);
            mResultReceiver.send(CODE_SUCCESS, data);

        } catch (RetrofitError error) {
            handleError("user follow blog", error);
        }

    }


    private void userFollowingBlogs(Intent intent) {
        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        int limit = intent.getIntExtra(KEY_LIMIT, -1);
        int offset = intent.getIntExtra(KEY_OFFSET, -1);

        try {

            ResponseContainer.UserFollowingBlogsContainer container = mOAuthService.userFollowingBlogs(limit < 0 ? null : limit,
                    offset < 0 ? null : offset);

            if (container.response != null && container.response.blogs != null) {
                Bundle data = new Bundle();
                data.putParcelableArray(KEY_BLOG, container.response.blogs);
                data.putInt(KEY_TOTAL, container.response.total_blogs);
                data.putInt(KEY_LIMIT, limit);
                data.putInt(KEY_OFFSET, offset);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                L.e(TAG, "userFollowingBlogs response is null");
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }

        } catch (RetrofitError error) {
            handleError("user following blogs", error);
        }
    }


    private void userLikes(Intent intent) {
        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        int limit = intent.getIntExtra(KEY_LIMIT, -1);
        int offset = intent.getIntExtra(KEY_OFFSET, -1);
        long before = intent.getLongExtra(KEY_BEFORE, -1);
        long after = intent.getLongExtra(KEY_AFTER, -1);

        try {

            ResponseContainer.UserLikesContainer container = mOAuthService.userLikes((limit < 0) ? null : limit,
                    (offset < 0) ? null : offset,
                    (before < 0) ? null : before,
                    (after < 0) ? null : after);

            if (container.response != null && container.response.liked_posts != null) {
                Bundle data = new Bundle();
                data.putParcelableArrayList(KEY_POSTS, extractPosts(container.response.liked_posts));
                data.putInt(KEY_TOTAL, container.response.liked_count);
                data.putInt(KEY_LIMIT, limit);
                data.putInt(KEY_OFFSET, offset);
                data.putLong(KEY_BEFORE, before);
                data.putLong(KEY_AFTER, after);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                L.e(TAG, "userLikes response is null");
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }

        } catch (RetrofitError error) {
            handleError("user likes", error);
        }
    }


    private void userDashboard(Intent intent) {
        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        int limit = intent.getIntExtra(KEY_LIMIT, -1);
        int offset = intent.getIntExtra(KEY_OFFSET, -1);
        String type = intent.getStringExtra(KEY_TYPE);
        long sinceId = intent.getLongExtra(KEY_ID, -1);
        boolean reblogInfo = intent.getBooleanExtra(KEY_REBLOG_INFO, false);
        boolean notesInfo = intent.getBooleanExtra(KEY_NOTES_INFO, false);

        try {

            ResponseContainer.BlogPostsContainer container = mOAuthService.userDashboard((limit > 0) ? limit : null,
                    (offset > 0) ? offset : null,
                    type,
                    (sinceId > 0) ? sinceId : null,
                    reblogInfo,
                    notesInfo);

            if (container.response != null && container.response.posts != null) {
                Bundle data = new Bundle();
                data.putParcelableArrayList(KEY_POSTS, extractPosts(container.response.posts));
                data.putInt(KEY_LIMIT, limit);
                data.putInt(KEY_OFFSET, offset);
                data.putString(KEY_TYPE, type);
                data.putLong(KEY_ID, sinceId);
                data.putBoolean(KEY_REBLOG_INFO, reblogInfo);
                data.putBoolean(KEY_NOTES_INFO, notesInfo);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                L.e(TAG, "user dashboard response is null");
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }

        } catch (RetrofitError error) {
            handleError("user dashboard", error);
        }

    }


    private void userInfo() {
        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        try {
            ResponseContainer.UserInfoContainer container = mOAuthService.userInfo();
            if (container.response != null && container.response.user != null) {
                Bundle data = new Bundle();
                data.putParcelable(KEY_USER, container.response.user);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                L.e(TAG, "user response is null");
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }
        } catch (RetrofitError error) {
            handleError("user info", error);
        }
    }


    private void deletePost(Intent intent) {
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        long id = intent.getLongExtra(KEY_ID, 0);

        if (hostname.isEmpty()) {
            L.e(TAG, "newPost hostname is empty");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (id == 0) {
            L.e(TAG, "newPost wrong id");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(TumblrExtras.Params.ID, String.valueOf(id));
        mOAuthClient.addPostParameters(params);

        try {
            mOAuthService.deletePost(hostname, id);
            L.i(TAG, "deletePost success");
            Bundle data = new Bundle();
            data.putLong(KEY_ID, id);
            mResultReceiver.send(CODE_SUCCESS, data);
        } catch (RetrofitError error) {
            handleError("delete post", error);
        }
    }


    private void reblogPost(Intent intent) {
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        ReblogPostCreator creator = intent.getParcelableExtra(KEY_CREATOR);

        if (hostname.isEmpty()) {
            L.e(TAG, "newPost hostname is empty");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (creator == null || !creator.valid()) {
            L.e(TAG, "newPost invalid creator");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        mResultReceiver.send(CODE_START, Bundle.EMPTY);
        mOAuthClient.addPostParameters(creator.params());
        try {
            ResponseContainer.BlogNewPostContainer container = mOAuthService.reblogPost(hostname,
                    creator.getId(),
                    creator.getPostType(),
                    creator.getPostState(),
                    creator.getTags(),
                    creator.getTweet(),
                    creator.getDate(),
                    creator.getFormat(),
                    creator.getSlug(),
                    creator.getReblogKey(),
                    creator.getComment());

            if (container != null && container.response != null) {
                Bundle data = new Bundle();
                data.putLong(KEY_ID, container.response.id);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }

            L.i(TAG, "reblogPost success");

        } catch (RetrofitError error) {
            handleError("post reblog", error);
        }
    }


    private void newPost(Intent intent) {
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        PostCreator creator = intent.getParcelableExtra(KEY_CREATOR);

        if (hostname.isEmpty()) {
            L.e(TAG, "newPost hostname is empty");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (creator == null || !creator.valid()) {
            L.e(TAG, "newPost invalid creator");
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        mResultReceiver.send(CODE_START, Bundle.EMPTY);
        mOAuthClient.addPostParameters(creator.params());
        @TumblrExtras.PostType String postType = creator.getPostType();
        try {
            ResponseContainer.BlogNewPostContainer container = null;
            switch (postType) {
                case TumblrExtras.Post.TEXT: {
                    TextPostCreator textCreator = (TextPostCreator) creator;
                    if (creator.getId() == null) {
                        container = mOAuthService.textPost(hostname,
                                textCreator.getPostType(),
                                textCreator.getPostState(),
                                textCreator.getTags(),
                                textCreator.getTweet(),
                                textCreator.getDate(),
                                textCreator.getFormat(),
                                textCreator.getSlug(),
                                textCreator.getTitle() == null ? null : Html.escapeHtml(textCreator.getTitle()),
                                textCreator.getBody());
                    } else {
                        container = mOAuthService.textPostEdit(hostname,
                                textCreator.getId(),
                                textCreator.getPostType(),
                                textCreator.getPostState(),
                                textCreator.getTags(),
                                textCreator.getTweet(),
                                textCreator.getDate(),
                                textCreator.getFormat(),
                                textCreator.getSlug(),
                                textCreator.getTitle() == null ? null : Html.escapeHtml(textCreator.getTitle()),
                                textCreator.getBody());
                    }
                    break;
                }

                case TumblrExtras.Post.PHOTO: {
                    PhotoPostCreator photoCreator = (PhotoPostCreator) creator;
                    Map<String, TypedFile> partMap = null;
                    if (photoCreator.getDataSize() > 0) {
                        partMap = new HashMap<>();
                        for (int i = 0; i < photoCreator.getDataSize(); i++) {
                            String mime = photoCreator.getMimeTypeAt(i);
                            if (mime != null) {
                                String key = "data[" + i + "]";
                                TypedFile file = new CountingTypedFile(mime, photoCreator.getDataFileAt(i), this);
                                partMap.put(key, file);
                                mSummaryUploadSize += file.length();
                            }
                        }
                    }

                    if (creator.getId() == null) {
                        container = mOAuthService.photoPost(hostname,
                                photoCreator.getPostType(),
                                photoCreator.getPostState(),
                                photoCreator.getTags(),
                                photoCreator.getTweet(),
                                photoCreator.getDate(),
                                photoCreator.getFormat(),
                                photoCreator.getSlug(),
                                photoCreator.getCaption(),
                                photoCreator.getLink(),
                                photoCreator.getSource(),
                                partMap);
                    } else {
                        container = mOAuthService.photoPostEdit(hostname,
                                photoCreator.getId(),
                                photoCreator.getPostType(),
                                photoCreator.getPostState(),
                                photoCreator.getTags(),
                                photoCreator.getTweet(),
                                photoCreator.getDate(),
                                photoCreator.getFormat(),
                                photoCreator.getSlug(),
                                photoCreator.getCaption(),
                                photoCreator.getLink(),
                                photoCreator.getSource(),
                                partMap);
                    }

                    break;
                }


                case TumblrExtras.Post.QUOTE: {
                    QuotePostCreator quoteCreator = (QuotePostCreator) creator;
                    if (creator.getId() == null) {
                        container = mOAuthService.quotePost(hostname,
                                quoteCreator.getPostType(),
                                quoteCreator.getPostState(),
                                quoteCreator.getTags(),
                                quoteCreator.getTweet(),
                                quoteCreator.getDate(),
                                quoteCreator.getFormat(),
                                quoteCreator.getSlug(),
                                quoteCreator.getQuote() == null ? null : Html.escapeHtml(quoteCreator.getQuote()),
                                quoteCreator.getSource());
                    } else {
                        container = mOAuthService.quotePostEdit(hostname,
                                quoteCreator.getId(),
                                quoteCreator.getPostType(),
                                quoteCreator.getPostState(),
                                quoteCreator.getTags(),
                                quoteCreator.getTweet(),
                                quoteCreator.getDate(),
                                quoteCreator.getFormat(),
                                quoteCreator.getSlug(),
                                quoteCreator.getQuote() == null ? null : Html.escapeHtml(quoteCreator.getQuote()),
                                quoteCreator.getSource());
                    }

                    break;
                }

                case TumblrExtras.Post.LINK: {
                    LinkPostCreator linkCreator = (LinkPostCreator) creator;
                    if (creator.getId() == null) {
                        container = mOAuthService.linkPost(hostname,
                                linkCreator.getPostType(),
                                linkCreator.getPostState(),
                                linkCreator.getTags(),
                                linkCreator.getTweet(),
                                linkCreator.getDate(),
                                linkCreator.getFormat(),
                                linkCreator.getSlug(),
                                linkCreator.getTitle() == null ? null : Html.escapeHtml(linkCreator.getTitle()),
                                linkCreator.getUrl(),
                                linkCreator.getDescription());
                    } else {
                        container = mOAuthService.linkPostEdit(hostname,
                                linkCreator.getId(),
                                linkCreator.getPostType(),
                                linkCreator.getPostState(),
                                linkCreator.getTags(),
                                linkCreator.getTweet(),
                                linkCreator.getDate(),
                                linkCreator.getFormat(),
                                linkCreator.getSlug(),
                                linkCreator.getTitle() == null ? null : Html.escapeHtml(linkCreator.getTitle()),
                                linkCreator.getUrl(),
                                linkCreator.getDescription());
                    }
                    break;
                }

                case TumblrExtras.Post.CHAT: {
                    ChatPostCreator chatCreator = (ChatPostCreator) creator;
                    if (creator.getId() == null) {
                        container = mOAuthService.chatPost(hostname,
                                chatCreator.getPostType(),
                                chatCreator.getPostState(),
                                chatCreator.getTags(),
                                chatCreator.getTweet(),
                                chatCreator.getDate(),
                                chatCreator.getFormat(),
                                chatCreator.getSlug(),
                                chatCreator.getTitle(),
                                chatCreator.getConversation());
                    } else {
                        container = mOAuthService.chatPostEdit(hostname,
                                chatCreator.getId(),
                                chatCreator.getPostType(),
                                chatCreator.getPostState(),
                                chatCreator.getTags(),
                                chatCreator.getTweet(),
                                chatCreator.getDate(),
                                chatCreator.getFormat(),
                                chatCreator.getSlug(),
                                chatCreator.getTitle(),
                                chatCreator.getConversation());
                    }
                    break;
                }

                case TumblrExtras.Post.AUDIO: {
                    AudioPostCreator audioCreator = (AudioPostCreator) creator;

                    TypedFile data = null;
                    if (audioCreator.getDataFile() != null && audioCreator.getDataMimeType() != null) {
                        data = new CountingTypedFile(audioCreator.getDataMimeType(), audioCreator.getDataFile(), this);
                        mSummaryUploadSize = data.length();
                    }

                    if (creator.getId() == null) {
                        container = mOAuthService.audioPost(hostname,
                                audioCreator.getPostType(),
                                audioCreator.getPostState(),
                                audioCreator.getTags(),
                                audioCreator.getTweet(),
                                audioCreator.getDate(),
                                audioCreator.getFormat(),
                                audioCreator.getSlug(),
                                audioCreator.getCaption(),
                                audioCreator.getExternalUrl(),
                                data);
                    } else {
                        container = mOAuthService.audioPostEdit(hostname,
                                audioCreator.getId(),
                                audioCreator.getPostType(),
                                audioCreator.getPostState(),
                                audioCreator.getTags(),
                                audioCreator.getTweet(),
                                audioCreator.getDate(),
                                audioCreator.getFormat(),
                                audioCreator.getSlug(),
                                audioCreator.getCaption(),
                                audioCreator.getExternalUrl(),
                                data);
                    }

                    break;
                }

                case TumblrExtras.Post.VIDEO: {
                    VideoPostCreator videoCreator = (VideoPostCreator) creator;

                    TypedFile data = null;
                    if (videoCreator.getData() != null && videoCreator.getDataMimeType() != null) {
                        data = new CountingTypedFile(videoCreator.getDataMimeType(), videoCreator.getData(), this);
                        mSummaryUploadSize = data.length();
                    }

                    if (creator.getId() == null) {
                        container = mOAuthService.videoPost(hostname,
                                videoCreator.getPostType(),
                                videoCreator.getPostState(),
                                videoCreator.getTags(),
                                videoCreator.getTweet(),
                                videoCreator.getDate(),
                                videoCreator.getFormat(),
                                videoCreator.getSlug(),
                                videoCreator.getCaption(),
                                videoCreator.getEmbed(),
                                data);
                    } else {
                        container = mOAuthService.videoPostEdit(hostname,
                                videoCreator.getId(),
                                videoCreator.getPostType(),
                                videoCreator.getPostState(),
                                videoCreator.getTags(),
                                videoCreator.getTweet(),
                                videoCreator.getDate(),
                                videoCreator.getFormat(),
                                videoCreator.getSlug(),
                                videoCreator.getCaption(),
                                videoCreator.getEmbed(),
                                data);
                    }

                    break;
                }
            }

            if (container != null && container.response != null) {
                Bundle data = new Bundle();
                data.putLong(KEY_ID, container.response.id);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }

            L.i(TAG, "newPost success");


        } catch (RetrofitError error) {
            handleError("new post", error);
        }
    }


    private void blogDrafts(Intent intent) {
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        long beforeId = intent.getLongExtra(KEY_ID, -1);
        int limit = intent.getIntExtra(KEY_LIMIT, -1);
        String filter = intent.getStringExtra(KEY_FILTER);

        if (TextUtils.isEmpty(hostname)) {
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        try {
            mResultReceiver.send(CODE_START, Bundle.EMPTY);
            ResponseContainer.BlogDraftContainer container = mOAuthService.getBlogDraftPosts(hostname,
                    limit < 0 ? null : limit,
                    beforeId < 0 ? null : beforeId,
                    TextUtils.isEmpty(filter) ? null : filter);
            if (container.response != null && container.response.posts != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(KEY_POSTS, extractPosts(container.response.posts));
                bundle.putLong(KEY_ID, beforeId);
                bundle.putString(KEY_FILTER, filter);
                mResultReceiver.send(CODE_SUCCESS, bundle);
            } else {
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }
        } catch (RetrofitError error) {
            handleError("blog drafts", error);
        }
    }


    private void blogQueue(Intent intent) {
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        int offset = intent.getIntExtra(KEY_OFFSET, -1);
        int limit = intent.getIntExtra(KEY_LIMIT, -1);
        String filter = intent.getStringExtra(KEY_FILTER);

        if (TextUtils.isEmpty(hostname)) {
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        if (mOAuthService == null) {
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
            return;
        }

        try {
            mResultReceiver.send(CODE_START, Bundle.EMPTY);
            ResponseContainer.BlogQueueContainer container = mOAuthService.getBlogQueuedPosts(hostname,
                    offset < 0 ? null : offset,
                    limit < 0 ? null : limit,
                    TextUtils.isEmpty(filter) ? null : filter);

            if (container.response != null && container.response.posts != null) {
                Bundle data = new Bundle();
                data.putParcelableArrayList(KEY_POSTS, extractPosts(container.response.posts));
                data.putInt(KEY_OFFSET, offset);
                data.putInt(KEY_LIMIT, limit);
                data.putString(KEY_FILTER, filter);
                mResultReceiver.send(CODE_SUCCESS, data);
            }

        } catch (RetrofitError error) {
            handleError("blog queued", error);
        }

    }


    private void blogPostById(Intent intent) {
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        long postId = intent.getLongExtra(KEY_ID, 0);
        boolean reblogInfo = intent.getBooleanExtra(KEY_REBLOG_INFO, false);
        boolean notesInfo = intent.getBooleanExtra(KEY_NOTES_INFO, false);

        if (TextUtils.isEmpty(hostname) || postId <= 0) {
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        try {
            ResponseContainer.BlogPostsContainer container;
            if (mOAuthService == null)
                container = mApiService.blogPosts(hostname, mConsumerToken.getToken(), postId, reblogInfo, notesInfo);
            else
                container = mOAuthService.blogPosts(hostname, mConsumerToken.getToken(), postId, reblogInfo, notesInfo);

            if (container.response != null && container.response.posts != null && container.response.blog != null) {
                Bundle data = new Bundle();
                data.putParcelable(KEY_BLOG, container.response.blog);
                data.putParcelableArrayList(KEY_POSTS, extractPosts(container.response.posts));
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }
        } catch (RetrofitError error) {
            handleError("blog post by id", error);
        }
    }


    private void blogPosts(Intent intent) {
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        String type = intent.getStringExtra(KEY_TYPE);
        String tag = intent.getStringExtra(KEY_TAG);
        int limit = intent.getIntExtra(KEY_LIMIT, 20);
        int offset = intent.getIntExtra(KEY_OFFSET, 0);
        boolean reblogInfo = intent.getBooleanExtra(KEY_REBLOG_INFO, false);
        boolean notesInfo = intent.getBooleanExtra(KEY_NOTES_INFO, false);
        String filter = intent.getStringExtra(KEY_FILTER);

        if (TextUtils.isEmpty(filter))
            filter = TumblrExtras.Filter.TEXT;

        if (TextUtils.isEmpty(hostname)) {
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        try {
            mResultReceiver.send(CODE_START, Bundle.EMPTY);
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
                Bundle data = new Bundle();
                data.putParcelable(KEY_BLOG, container.response.blog);
                data.putParcelableArrayList(KEY_POSTS, extractPosts(container.response.posts));
                data.putString(KEY_TYPE, type);
                data.putString(KEY_TAG, tag);
                data.putInt(KEY_LIMIT, limit);
                data.putInt(KEY_OFFSET, offset);
                data.putBoolean(KEY_REBLOG_INFO, reblogInfo);
                data.putBoolean(KEY_NOTES_INFO, notesInfo);
                data.putString(KEY_FILTER, filter);
                data.putInt(KEY_TOTAL, container.response.total_posts);
                mResultReceiver.send(CODE_SUCCESS, data);
            } else {
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }
        } catch (RetrofitError error) {
            handleError("blog posts", error);
        }


    }


    private void blogFollowers(Intent intent) {
        if (mOAuthService == null) {
            L.e(TAG, "user not authorized");
            mResultReceiver.send(CODE_NOT_AUTHORIZED, Bundle.EMPTY);
        } else {
            mResultReceiver.send(CODE_START, Bundle.EMPTY);
            String hostname = intent.getStringExtra(KEY_HOSTNAME);
            int limit = intent.getIntExtra(KEY_LIMIT, -1);
            int offset = intent.getIntExtra(KEY_OFFSET, -1);

            if (TextUtils.isEmpty(hostname)) {
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
                return;
            }

            try {
                ResponseContainer.BlogFollowersContainer container = mOAuthService.getBlogFollowers(hostname, limit < 0 ? null : limit, offset < 0 ? null : offset);
                if (container.response != null && container.response.users != null) {
                    Bundle data = new Bundle();
                    data.putParcelableArray(KEY_USERS, container.response.users);
                    data.putInt(KEY_LIMIT, limit);
                    data.putInt(KEY_OFFSET, offset);
                    data.putInt(KEY_TOTAL, container.response.total_users);
                    mResultReceiver.send(CODE_SUCCESS, data);
                } else {
                    mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
                }
            } catch (RetrofitError error) {
                handleError("blog followers", error);
            }
        }
    }


    private void blogLikes(Intent intent) {
        mResultReceiver.send(CODE_START, Bundle.EMPTY);
        String action = intent.getAction();
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        int limit = intent.getIntExtra(KEY_LIMIT, -1);
        int offset = intent.getIntExtra(KEY_OFFSET, -1);
        long timestamp = intent.getLongExtra(KEY_TIMESTAMP, -1);

        if (TextUtils.isEmpty(hostname)) {
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        try {
            ResponseContainer.BlogLikesContainer container;
            switch (action) {
                case ACTION_BLOG_LIKES_BEFORE:
                    container = mApiService.blogLikesBefore(hostname,
                            limit < 0 ? null : limit,
                            timestamp < 0 ? null : timestamp,
                            mConsumerToken.getToken());

                    break;
                case ACTION_BLOG_LIKES_AFTER:
                    container = mApiService.blogLikesAfter(hostname,
                            limit < 0 ? null : limit,
                            timestamp < 0 ? null : timestamp,
                            mConsumerToken.getToken());

                    break;
                default:
                    container = mApiService.blogLikes(hostname,
                            limit < 0 ? null : limit,
                            offset < 0 ? null : offset,
                            mConsumerToken.getToken());
                    break;
            }

            if (container != null && container.response != null && container.response.liked_posts != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(KEY_POSTS, extractPosts(container.response.liked_posts));
                bundle.putInt(KEY_LIKED_COUNT, container.response.liked_count);
                bundle.putInt(KEY_LIMIT, limit);
                bundle.putInt(KEY_OFFSET, offset);
                bundle.putLong(KEY_TIMESTAMP, timestamp);
                mResultReceiver.send(CODE_SUCCESS, bundle);
            } else {
                mResultReceiver.send(CODE_ERROR, new Bundle());
            }
        } catch (RetrofitError error) {
            handleError("blog likes", error);
        }
    }


    private void blogAvatar(Intent intent) {
        mResultReceiver.send(CODE_START, Bundle.EMPTY);
        int size = intent.getIntExtra(KEY_SIZE, TumblrExtras.Size.SIZE_UNDEFINED);
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        if (TextUtils.isEmpty(hostname)) {
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        try {

            Response response;
            if (size == TumblrExtras.Size.SIZE_UNDEFINED)
                mApiService.blogAvatar(hostname);
            else
                mApiService.blogAvatar(hostname, size);

            L.d(TAG, "response avatar");

        } catch (RetrofitError error) {
            if (error.getResponse() != null && error.getResponse().getStatus() == Utils.STATUS_FOUND && !TextUtils.isEmpty(error.getResponse().getUrl())) {
                Bundle result = new Bundle();
                result.putInt(KEY_SIZE, size);
                result.putString(KEY_BLOG_AVATAR, error.getResponse().getUrl());
                mResultReceiver.send(CODE_SUCCESS, result);
            } else {
                handleError("blog avatar", error);
            }
        }
    }


    private void blogInfo(Intent intent) {
        mResultReceiver.send(CODE_START, Bundle.EMPTY);
        String hostname = intent.getStringExtra(KEY_HOSTNAME);
        if (TextUtils.isEmpty(hostname)) {
            mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            return;
        }

        try {
            ResponseContainer.BlogContainer container;
            if (mOAuthService != null)
                container = mOAuthService.blogInfo(hostname, mConsumerToken.getToken());
            else
                container = mApiService.blogInfo(hostname, mConsumerToken.getToken());

            if (container != null && container.response != null) {
                Bundle result = new Bundle();
                result.putParcelable(KEY_BLOG, container.response.blog);
                mResultReceiver.send(CODE_SUCCESS, result);
            } else {
                mResultReceiver.send(CODE_ERROR, Bundle.EMPTY);
            }
        } catch (RetrofitError error) {
            handleError("blog info", error);
        }
    }


    private void oAuthRequestToken() {
        mResultReceiver.send(CODE_START, Bundle.EMPTY);
        OAuthRequestTokenInterceptor interceptor = new OAuthRequestTokenInterceptor(mConsumerToken);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(OAuthExtras.OAUTH_ENDPOINT)
                .setRequestInterceptor(interceptor)
                .setLogLevel(RoboTumblr.LOG_LEVEL)
                .build();

        OAuthService oAuthService = restAdapter.create(OAuthService.class);

        try {

            Response response = oAuthService.requestToken();
            String oauth = new String(((TypedByteArray) response.getBody()).getBytes());
            RequestToken requestToken = OAuthExtras.Extractor.extractRequestToken(oauth);
            Bundle args = new Bundle();
            args.putParcelable(KEY_TOKEN, requestToken);
            mResultReceiver.send(CODE_SUCCESS, args);
        } catch (RetrofitError error) {
            handleError("oAuthRequestToken", error);
        }

    }


    private void oAuthAccessToken(Intent intent) {
        String verifier = intent.getStringExtra(KEY_VERIFIER);
        RequestToken requestToken = intent.getParcelableExtra(KEY_REQUEST_TOKEN);
        mResultReceiver.send(CODE_START, Bundle.EMPTY);

        OAuthAccessTokenInterceptor interceptor = new OAuthAccessTokenInterceptor(mConsumerToken, requestToken, verifier);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(OAuthExtras.OAUTH_ENDPOINT)
                .setRequestInterceptor(interceptor)
                .setLogLevel(RoboTumblr.LOG_LEVEL)
                .build();

        OAuthService oAuthService = restAdapter.create(OAuthService.class);

        try {
            Response response = oAuthService.accessToken();
            String oauth = new String(((TypedByteArray) response.getBody()).getBytes());
            AccessToken accessToken = OAuthExtras.Extractor.extractAccessToken(oauth);
            RoboTumblr.setAccessToken(this, accessToken);
            mResultReceiver.send(CODE_SUCCESS, Bundle.EMPTY);
        } catch (RetrofitError error) {
            handleError("oAuthAccessToken", error);
        }
    }


    private void handleError(String from, RetrofitError error) {
        if (error != null && from != null && error.getMessage() != null)
            L.e(TAG, from + ": " + error.getMessage());

        String errorText = null;
        if (error != null && error.getResponse() != null && error.getResponse().getBody() != null) {
            if (error.getMessage() != null && error.getMessage().contains(NOT_AUTHORIZED_CODE)) {
                L.e(from, "User not authorized");
                mResultReceiver.send(CODE_TOKEN_EXPIRED, Bundle.EMPTY);
                return;
            }
            String data = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
            if (!TextUtils.isEmpty(data)) {
                try {
                    String msg = null;
                    String err = null;

                    JsonParser parser = new JsonParser();
                    JsonObject root = (JsonObject) parser.parse(data);
                    if (root.has("meta")) {
                        JsonObject meta = root.getAsJsonObject("meta");
                        msg = meta.get("msg").getAsString();
                    }

                    try {
                        if (root.has("response")) {
                            JsonObject response = root.getAsJsonObject("response");
                            if (response.has("error")) {
                                err = response.get("error").getAsString();
                            }

                            if (response.has("errors")) {
                                JsonArray errors = response.getAsJsonArray("errors");
                                if (errors.size() > 0) {
                                    err = errors.get(0).getAsString();
                                }
                            }
                        }

                    } catch (Exception e) {
                        err = null;
                    }

                    if (TextUtils.isEmpty(err))
                        errorText = msg;
                    else
                        errorText = err;
                } catch (Exception e) {
                    L.e(TAG, "error parsing error response");
                }
            }
        }
        Bundle data = new Bundle();
        data.putString(KEY_ERROR, errorText);
        mResultReceiver.send(CODE_ERROR, data);
    }


    @Override
    public void onFileDataTransferred(long transferred) {
        if (mSummaryUploadSize != -1) {
            mSummaryTransferredSize += transferred;
            int progress = (int) (((double) mSummaryTransferredSize / mSummaryUploadSize) * 100d);
            Bundle data = new Bundle();
            data.putInt(KEY_PROGRESS, progress);
            mResultReceiver.send(CODE_PROGRESS, data);
        }
    }


    private ArrayList<Post> extractPosts(RawPost[] rawPosts) {
        ArrayList<Post> posts = new ArrayList<>();
        if (rawPosts != null && rawPosts.length > 0) {
            for (RawPost raw : rawPosts) {
                posts.add(raw.create());
            }
        }
        return posts;
    }

    private static String checkHostname(String hostname) {
        if (hostname != null) {
            if (!hostname.contains(TumblrExtras.HOSTNAME_SUFFIX))
                hostname += TumblrExtras.HOSTNAME_SUFFIX;
            return hostname;
        }
        return null;
    }


    private static int checkOffset(int offset) {
        if (offset < 0)
            offset = 0;
        return offset;
    }


    private static int checkLimit(int limit) {
        if (limit < 1 || limit > 20)
            limit = 20;
        return limit;
    }


    private static long checkTimestamp(long timestamp) {
        if (timestamp < 0)
            timestamp = 0;
        return timestamp;
    }


    private static Intent getBaseIntent(@NonNull Context context, ResultReceiver receiver, @NonNull String action) {
        Intent intent = new Intent(action, null, context, QueryService.class);
        intent.putExtra(KEY_RECEIVER, receiver);
        return intent;
    }

    /**
     * Request to get request token {@link RequestToken}
     *
     * @param context  package context
     * @param receiver receiver to get data
     * @return Intent to start {@link QueryService}
     */
    public static Intent getOAuthRequestToken(@NonNull Context context, RequestTokenReceiver receiver) {
        return getBaseIntent(context, receiver, ACTION_OAUTH_REQUEST_TOKEN);
    }

    /**
     * Request to get access token {@link AccessToken}
     *
     * @param context       package context
     * @param receiver      receiver to get data
     * @param requestToken  a request token got with {@link QueryService#getOAuthRequestToken(Context, RequestTokenReceiver)}
     * @param oauthVerifier string verifier parsed from authorization url response
     * @return Intent to start {@link QueryService}
     */
    public static Intent getOauthAccessToken(@NonNull Context context, AccessTokenReceiver receiver, RequestToken requestToken, String oauthVerifier) {
        Intent intent = getBaseIntent(context, receiver, ACTION_OAUTH_ACCESS_TOKEN);
        intent.putExtra(KEY_REQUEST_TOKEN, requestToken);
        intent.putExtra(KEY_VERIFIER, oauthVerifier);
        return intent;
    }


    /**
     * Request to get Blog info, has additional params while user is authorized see {@link com.sun40.robotumblr.model.Blog}
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param hostname host name like temp.tumblr.com
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogInfo(@NonNull Context context, BlogInfoReceiver receiver, String hostname) {
        Intent intent = getBaseIntent(context, receiver, ACTION_BLOG_INFO);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        return intent;
    }


    /**
     * Request to get Blog avatar, use size param to get specified avatar
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param hostname host name like temp.tumblr.com
     * @param size     avatar size of {@link com.sun40.robotumblr.TumblrExtras.TumblrSize}, by default use {@link com.sun40.robotumblr.TumblrExtras.Size#SIZE_UNDEFINED} which returns 64x64 avatar
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogAvatar(@NonNull Context context, BlogAvatarReceiver receiver, String hostname, @TumblrExtras.TumblrSize int size) {
        Intent intent = getBaseIntent(context, receiver, ACTION_BLOG_AVATAR);
        intent.putExtra(KEY_SIZE, size);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        return intent;
    }

    /**
     * Request to get Blog avatar, use size param to get avatar with default size 64x64
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param hostname host name like temp.tumblr.com
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogAvatar(@NonNull Context context, BlogAvatarReceiver receiver, String hostname) {
        return blogAvatar(context, receiver, checkHostname(hostname), TumblrExtras.Size.SIZE_UNDEFINED);
    }


    /**
     * Request to get Blog likes, with limit and offset params, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param context       package context
     * @param likesReceiver receiver to get response data
     * @param username      host name like temp.tumblr.com, were user name equals main user blog name
     * @param limit         likes limit per page
     * @param offset        likes offset from start
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogLikes(@NonNull Context context, BlogLikesReceiver likesReceiver, String username, int limit, int offset) {
        Intent intent = getBaseIntent(context, likesReceiver, ACTION_BLOG_LIKES);
        intent.putExtra(KEY_HOSTNAME, checkHostname(username));
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        return intent;
    }


    /**
     * Request to get specified blog likes by username before timestamp, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param context       package context
     * @param likesReceiver receiver to get response data
     * @param username      user name like temp.tumblr.com, were user name equals main user blog name
     * @param limit         likes limit per page
     * @param timestamp     unix epoch time stamp
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogLikesBefore(@NonNull Context context, BlogLikesReceiver likesReceiver, String username, int limit, long timestamp) {
        Intent intent = getBaseIntent(context, likesReceiver, ACTION_BLOG_LIKES_BEFORE);
        intent.putExtra(KEY_HOSTNAME, checkHostname(username));
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_TIMESTAMP, checkTimestamp(timestamp));
        return intent;
    }

    /**
     * Request to get specified blog likes by username after timestamp, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param context       package context
     * @param likesReceiver receiver to get response data
     * @param username      host name like temp.tumblr.com, were user name equals main user blog name
     * @param limit         likes limit per page
     * @param timestamp     unix epoch time stamp
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogLikesAfter(@NonNull Context context, BlogLikesReceiver likesReceiver, String username, int limit, long timestamp) {
        Intent intent = getBaseIntent(context, likesReceiver, ACTION_BLOG_LIKES_AFTER);
        intent.putExtra(KEY_HOSTNAME, checkHostname(username));
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_TIMESTAMP, checkTimestamp(timestamp));
        return intent;
    }


    /**
     * Request to get own blog followers, if current host name doesn't belong to current user you will receive Authorization Error
     *
     * @param context     package context
     * @param receiver    receiver to get response data
     * @param ownHostname user's host name like temp.tumblr.com
     * @param limit       followers limit per page
     * @param offset      offset from start
     * @return Intent to start {@link QueryService}
     */
    public static Intent getBlogFollowers(@NonNull Context context, BlogFollowersReceiver receiver, String ownHostname, int limit, int offset) {
        Intent intent = getBaseIntent(context, receiver, ACTION_BLOG_FOLLOWERS);
        intent.putExtra(KEY_HOSTNAME, checkHostname(ownHostname));
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        return intent;
    }


    /**
     * Request to get blog posts
     *
     * @param context    package context
     * @param receiver   receiver to get response data
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
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogPosts(@NonNull Context context,
                                   @NonNull BlogPostsReceiver receiver,
                                   @NonNull String hostname,
                                   @Nullable @TumblrExtras.PostType String type,
                                   @Nullable String tag,
                                   int limit,
                                   int offset,
                                   boolean reblogInfo,
                                   boolean notesInfo,
                                   @Nullable @TumblrExtras.FilterType String filter) {
        Intent intent = getBaseIntent(context, receiver, ACTION_BLOG_POSTS);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_TAG, tag);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        intent.putExtra(KEY_REBLOG_INFO, reblogInfo);
        intent.putExtra(KEY_NOTES_INFO, notesInfo);
        intent.putExtra(KEY_FILTER, filter);
        return intent;
    }


    /**
     * Request to get blog posts
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param hostname blog hostname like temp.tumblr.com
     * @param type     The type of post to return. Specify one of the following: {@link com.sun40.robotumblr.TumblrExtras.Post#TEXT}, {@link com.sun40.robotumblr.TumblrExtras.Post#QUOTE}, {@link com.sun40.robotumblr.TumblrExtras.Post#LINK},
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#ANSWER}, {@link com.sun40.robotumblr.TumblrExtras.Post#VIDEO}, {@link com.sun40.robotumblr.TumblrExtras.Post#AUDIO},
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#PHOTO}, {@link com.sun40.robotumblr.TumblrExtras.Post#CHAT} or null to get all posts
     * @param tag      Limits the response to posts with the specified tag may be null
     * @param limit    The number of posts to return: 1–20, inclusive or -1 to use default value 20
     * @param offset   RawPost number to start at or -1 to use default value 0
     * @param filter   Specifies the post format to return, other than HTML:
     *                 {@link com.sun40.robotumblr.TumblrExtras.Filter#TEXT} – Plain text, no HTML
     *                 {@link com.sun40.robotumblr.TumblrExtras.Filter#RAW} – As entered by the user (no post-processing); if the user writes in Markdown, the Markdown will be returned rather than HTML
     *                 <i>Default null - HTML</i>
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogPosts(@NonNull Context context,
                                   @NonNull BlogPostsReceiver receiver,
                                   @NonNull String hostname,
                                   @Nullable @TumblrExtras.PostType String type,
                                   @Nullable String tag,
                                   int limit,
                                   int offset,
                                   @Nullable @TumblrExtras.FilterType String filter) {
        Intent intent = getBaseIntent(context, receiver, ACTION_BLOG_POSTS);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_TAG, tag);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        intent.putExtra(KEY_REBLOG_INFO, false);
        intent.putExtra(KEY_NOTES_INFO, false);
        intent.putExtra(KEY_FILTER, filter);
        return intent;
    }


    /**
     * Request to get blog posts
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param hostname blog hostname like temp.tumblr.com
     * @param type     The type of post to return. Specify one of the following: {@link com.sun40.robotumblr.TumblrExtras.Post#TEXT}, {@link com.sun40.robotumblr.TumblrExtras.Post#QUOTE}, {@link com.sun40.robotumblr.TumblrExtras.Post#LINK},
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#ANSWER}, {@link com.sun40.robotumblr.TumblrExtras.Post#VIDEO}, {@link com.sun40.robotumblr.TumblrExtras.Post#AUDIO},
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#PHOTO}, {@link com.sun40.robotumblr.TumblrExtras.Post#CHAT} or null to get all posts
     * @param tag      Limits the response to posts with the specified tag may be null
     * @param limit    The number of posts to return: 1–20, inclusive or -1 to use default value 20
     * @param offset   RawPost number to start at or -1 to use default value 0
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogPosts(@NonNull Context context,
                                   @NonNull BlogPostsReceiver receiver,
                                   @NonNull String hostname,
                                   @Nullable @TumblrExtras.PostType String type,
                                   @Nullable String tag,
                                   int limit,
                                   int offset) {
        Intent intent = getBaseIntent(context, receiver, ACTION_BLOG_POSTS);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_TAG, tag);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        intent.putExtra(KEY_REBLOG_INFO, false);
        intent.putExtra(KEY_NOTES_INFO, false);
        return intent;
    }


    /**
     * Request to get blog posts
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param hostname blog hostname like temp.tumblr.com
     * @param type     The type of post to return. Specify one of the following: {@link com.sun40.robotumblr.TumblrExtras.Post#TEXT}, {@link com.sun40.robotumblr.TumblrExtras.Post#QUOTE}, {@link com.sun40.robotumblr.TumblrExtras.Post#LINK},
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#ANSWER}, {@link com.sun40.robotumblr.TumblrExtras.Post#VIDEO}, {@link com.sun40.robotumblr.TumblrExtras.Post#AUDIO},
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#PHOTO}, {@link com.sun40.robotumblr.TumblrExtras.Post#CHAT} or null to get all posts
     * @param limit    The number of posts to return: 1–20, inclusive or -1 to use default value 20
     * @param offset   RawPost number to start at or -1 to use default value 0
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogPosts(@NonNull Context context,
                                   @NonNull BlogPostsReceiver receiver,
                                   @NonNull String hostname,
                                   @Nullable @TumblrExtras.PostType String type,
                                   int limit,
                                   int offset) {
        Intent intent = getBaseIntent(context, receiver, ACTION_BLOG_POSTS);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        intent.putExtra(KEY_REBLOG_INFO, false);
        intent.putExtra(KEY_NOTES_INFO, false);
        return intent;
    }

    /**
     * Request to get blog posts
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param hostname blog hostname like temp.tumblr.com
     * @param limit    The number of posts to return: 1–20, inclusive or -1 to use default value 20
     * @param offset   RawPost number to start at or -1 to use default value 0
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogPosts(@NonNull Context context,
                                   @NonNull BlogPostsReceiver receiver,
                                   @NonNull String hostname,
                                   int limit,
                                   int offset) {
        Intent intent = getBaseIntent(context, receiver, ACTION_BLOG_POSTS);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        intent.putExtra(KEY_REBLOG_INFO, false);
        intent.putExtra(KEY_NOTES_INFO, false);
        return intent;
    }


    /**
     * Request to get blog post by id
     *
     * @param context        package context
     * @param resultReceiver receiver to get response data
     * @param hostname       blog hostname like temp.tumblr.com
     * @param id             post id
     * @param reblogInfo     Indicates whether to return reblog information (specify true or false). Returns the various reblogged_ fields. <i>default: false</i>
     * @param notesInfo      Indicates whether to return notes information (specify true or false). Returns note count and note metadata. <i>default: false<i/>
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogPostById(@NonNull Context context, BlogPostByIdReceiver resultReceiver, String hostname, long id, boolean reblogInfo, boolean notesInfo) {
        Intent intent = getBaseIntent(context, resultReceiver, ACTION_BLOG_POST_BY_ID);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_REBLOG_INFO, reblogInfo);
        intent.putExtra(KEY_NOTES_INFO, notesInfo);
        return intent;
    }


    /**
     * Request to get users blog queued posts
     *
     * @param context        package context
     * @param resultReceiver receiver to get response data
     * @param hostname       users own host name like temp.tumbl.com
     * @param limit          items limit per page
     * @param offset         offset from start
     * @param filter         Specifies the post format to return, other than HTML:
     *                       {@link com.sun40.robotumblr.TumblrExtras.Filter#TEXT} – Plain text, no HTML
     *                       {@link com.sun40.robotumblr.TumblrExtras.Filter#RAW} – As entered by the user (no post-processing); if the user writes in Markdown, the Markdown will be returned rather than HTML
     *                       <i>Default null - HTML</i>
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogPostQueue(@NonNull Context context, BlogQueueReceiver resultReceiver, String hostname, int limit, int offset, @Nullable @TumblrExtras.PostType String filter) {
        Intent intent = getBaseIntent(context, resultReceiver, ACTION_BLOG_QUEUED);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_FILTER, filter);
        return intent;
    }


    /**
     * Request to get users blog queued posts
     *
     * @param context        package context
     * @param resultReceiver receiver to get response data
     * @param hostname       users own host name like temp.tumblr.com
     * @param limit          items limit per page
     * @param beforeId       Return posts that have appeared after this ID
     *                       Use this parameter to page through the results: first get a set of posts,
     *                       and then get posts since the last ID of the previous set.
     * @param filter         Specifies the post format to return, other than HTML:
     *                       {@link com.sun40.robotumblr.TumblrExtras.Filter#TEXT} – Plain text, no HTML
     *                       {@link com.sun40.robotumblr.TumblrExtras.Filter#RAW} – As entered by the user (no post-processing); if the user writes in Markdown, the Markdown will be returned rather than HTML
     *                       <i>Default null - HTML</i>
     * @return Intent to start {@link QueryService}
     */
    public static Intent blogDrafts(@NonNull Context context, BlogDraftReceiver resultReceiver, String hostname, int limit, long beforeId, String filter) {
        Intent intent = getBaseIntent(context, resultReceiver, ACTION_BLOG_DRAFT);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_LIMIT, limit);
        intent.putExtra(KEY_ID, beforeId);
        intent.putExtra(KEY_FILTER, filter);
        return intent;
    }


    /**
     * Request to create new post in current blog
     *
     * @param context        package context
     * @param resultReceiver receiver to get response data
     * @param hostname       users own hostname like temp.tumblr.com
     * @param creator        creator object witch contains post params
     * @return Intent to start {@link QueryService}
     */
    public static Intent newPost(@NonNull Context context, BlogNewPostReceiver resultReceiver, @NonNull String hostname, @NonNull PostCreator creator) {
        if (!creator.valid())
            return null;
        Intent intent = getBaseIntent(context, resultReceiver, ACTION_BLOG_NEW_POST);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_CREATOR, creator);
        return intent;
    }


    /**
     * Request to create edit post in current blog
     *
     * @param context        package context
     * @param resultReceiver receiver to get response data
     * @param hostname       users own hostname like temp.tumblr.com
     * @param creator        creator object witch contains post params
     * @return Intent to start {@link QueryService}
     */
    public static Intent editPost(@NonNull Context context, BlogNewPostReceiver resultReceiver, @NonNull String hostname, @NonNull PostCreator creator) {
        if (!creator.valid())
            return null;
        Intent intent = getBaseIntent(context, resultReceiver, ACTION_BLOG_EDIT_POST);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_CREATOR, creator);
        return intent;
    }


    /**
     * Request to reblog post in current blog
     *
     * @param context        package context
     * @param resultReceiver receiver to get response data
     * @param hostname       user blog hostname like temp.tumblr.com
     * @param creator        creator object witch contains post params
     * @return Intent to start {@link QueryService}
     */
    public static Intent reblogPost(@NonNull Context context, BlogReblogReceiver resultReceiver, @NonNull String hostname, @NonNull ReblogPostCreator creator) {
        if (!creator.valid())
            return null;
        Intent intent = getBaseIntent(context, resultReceiver, ACTION_BLOG_REBLOG_POST);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_CREATOR, creator);
        return intent;
    }


    /**
     * Request to reblog post in current blog
     *
     * @param context        package context
     * @param resultReceiver receiver to get response data
     * @param hostname       user blog hostname like temp.tumblr.com
     * @param post           post for reblogging
     * @param comment        comment on reblogged post
     * @return Intent to start {@link QueryService}
     */
    public static Intent reblogPost(@NonNull Context context, BlogReblogReceiver resultReceiver, @NonNull String hostname, Post post, @Nullable String comment) {
        //noinspection ResourceType
        ReblogPostCreator creator = new ReblogPostCreator(post.getType(), post.getId(), post.getReblogKey());
        creator.setComment(comment);
        return reblogPost(context, resultReceiver, hostname, creator);
    }


    /**
     * Request to remove post in current user's blog
     *
     * @param context        package context
     * @param resultReceiver receiver to get response data
     * @param hostname       users own hostname like temp.tumblr.com
     * @param id             post id to remove
     * @return Intent to start {@link QueryService}
     */
    public static Intent deletePost(@NonNull Context context, BlogPostDeleteReceiver resultReceiver, @NonNull String hostname, long id) {
        Intent intent = getBaseIntent(context, resultReceiver, ACTION_BLOG_DELETE_POST);
        intent.putExtra(KEY_HOSTNAME, checkHostname(hostname));
        intent.putExtra(KEY_ID, id);
        return intent;
    }


    /**
     * Request to get current user info
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @return Intent to start {@link QueryService}
     */
    public static Intent userInfo(@NonNull Context context, UserInfoReceiver receiver) {
        return getBaseIntent(context, receiver, ACTION_USER_INFO);
    }


    /**
     * Request to get user dashboard posts
     *
     * @param context    package context
     * @param receiver   receiver to get response data
     * @param limit      The number of results to return: 1–20, inclusive
     * @param offset     RawPost number to start at
     * @param type       The type of post to return or null to return any types. Specify one of the following:
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#TEXT}
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#PHOTO}
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#QUOTE}
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#LINK}
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#CHAT}
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#AUDIO}
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#VIDEO}
     *                   {@link com.sun40.robotumblr.TumblrExtras.Post#ANSWER}
     * @param sinceId    Return posts that have appeared after this ID
     *                   Use this parameter to page through the results: first get a set of posts, and then get posts since the last ID of the previous set.
     * @param reblogInfo Indicates whether to return reblog information (specify true or false). Returns the various reblogged_ fields.
     * @param notesInfo  Indicates whether to return notes information (specify true or false). Returns note count and note metadata.
     * @return Intent to start {@link QueryService}
     */
    public static Intent userDashbloard(@NonNull Context context, @NonNull UserDashboardReceiver receiver, int limit, int offset, @TumblrExtras.PostType String type, long sinceId, boolean reblogInfo, boolean notesInfo) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_DASHBOARD);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_ID, sinceId);
        intent.putExtra(KEY_REBLOG_INFO, reblogInfo);
        intent.putExtra(KEY_NOTES_INFO, notesInfo);
        return intent;
    }


    /**
     * Request to get user dashboard posts
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param limit    The number of results to return: 1–20, inclusive
     * @param offset   RawPost number to start at
     * @param type     The type of post to return or null to return any types. Specify one of the following:
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#TEXT}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#PHOTO}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#QUOTE}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#LINK}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#CHAT}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#AUDIO}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#VIDEO}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#ANSWER}
     * @return Intent to start {@link QueryService}
     */
    public static Intent userDashbloard(@NonNull Context context, @NonNull UserDashboardReceiver receiver, int limit, int offset, @Nullable @TumblrExtras.PostType String type) {
        return userDashbloard(context, receiver, limit, offset, type, -1, false, false);
    }


    /**
     * Request to get user dashboard posts
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param limit    The number of results to return: 1–20, inclusive
     * @param type     The type of post to return or null to return any types. Specify one of the following:
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#TEXT}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#PHOTO}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#QUOTE}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#LINK}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#CHAT}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#AUDIO}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#VIDEO}
     *                 {@link com.sun40.robotumblr.TumblrExtras.Post#ANSWER}
     * @param sinceId  Return posts that have appeared after this ID
     *                 Use this parameter to page through the results: first get a set of posts, and then get posts since the last ID of the previous set.
     * @return Intent to start {@link QueryService}
     */
    public static Intent userDashbloard(@NonNull Context context, @NonNull UserDashboardReceiver receiver, int limit, @Nullable @TumblrExtras.PostType String type, long sinceId) {
        return userDashbloard(context, receiver, limit, -1, type, sinceId, false, false);
    }


    /**
     * Request to get user's posts likes
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param limit    The number of results to return: 1–20, inclusive
     * @param offset   Liked post number to start at
     * @param before   Retrieve posts liked before the specified timestamp
     * @param after    Retrieve posts liked after the specified timestamp
     * @return Intent to start {@link QueryService}
     * <p/>
     * <li>You can only provide either before, after, or offset. If you provide more than one of these options together you will get an error.
     * <li>You can still use limit with any of those three options to limit your result set.
     * <li>When using the offset parameter the maximum limit on the offset is 1000. If you would like to get more results than that use either before or after.
     */
    public static Intent userLikes(@NonNull Context context, @NonNull UserLikesReceiver receiver, int limit, int offset, long before, long after) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_LIKES);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        intent.putExtra(KEY_BEFORE, checkTimestamp(before));
        intent.putExtra(KEY_AFTER, checkTimestamp(after));
        return intent;
    }


    /**
     * Request to get user's posts likes
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param limit    The number of results to return: 1–20, inclusive
     * @return Intent to start {@link QueryService}
     * <p/>
     * <li>You can only provide either before, after, or offset. If you provide more than one of these options together you will get an error.
     * <li>You can still use limit with any of those three options to limit your result set.
     * <li>When using the offset parameter the maximum limit on the offset is 1000. If you would like to get more results than that use either before or after.
     */
    public static Intent userLikes(@NonNull Context context, @NonNull UserLikesReceiver receiver, int limit, int offset) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_LIKES);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        return intent;
    }


    /**
     * Request to get user's posts likes
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param limit    The number of results to return: 1–20, inclusive
     * @param before   Retrieve posts liked before the specified timestamp
     * @return Intent to start {@link QueryService}
     * <p/>
     * <li>You can only provide either before, after, or offset. If you provide more than one of these options together you will get an error.
     * <li>You can still use limit with any of those three options to limit your result set.
     * <li>When using the offset parameter the maximum limit on the offset is 1000. If you would like to get more results than that use either before or after.
     */
    public static Intent userLikesBefore(@NonNull Context context, @NonNull UserLikesReceiver receiver, int limit, long before) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_LIKES);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_BEFORE, checkTimestamp(before));
        return intent;
    }


    /**
     * Request to get user's posts likes
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param limit    The number of results to return: 1–20, inclusive
     * @param after    Retrieve posts liked after the specified timestamp
     * @return Intent to start {@link QueryService}
     * <p/>
     * <li>You can only provide either before, after, or offset. If you provide more than one of these options together you will get an error.
     * <li>You can still use limit with any of those three options to limit your result set.
     * <li>When using the offset parameter the maximum limit on the offset is 1000. If you would like to get more results than that use either before or after.
     */
    public static Intent userLikesAfter(@NonNull Context context, @NonNull UserLikesReceiver receiver, int limit, long after) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_LIKES);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_AFTER, checkTimestamp(after));
        return intent;
    }


    /**
     * Request to get user's following blogs
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param limit    The number of results to return: 1–20, inclusive
     * @param offset   Result number to start at
     * @return Intent to start {@link QueryService}
     */
    public static Intent userFollowingBlogs(@NonNull Context context, @NonNull UserFollowingBlogsReceiver receiver, int limit, int offset) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_FOLLOWING_BLOGS);
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_OFFSET, checkOffset(offset));
        return intent;
    }


    /**
     * Request to follow blog by blog url
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param url      The URL of the blog to follow
     * @return Intent to start {@link QueryService}
     */
    public static Intent userFollowBlog(@NonNull Context context, @NonNull UserBlogFollowReceiver receiver, @NonNull String url) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_BLOG_FOLLOW);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_FOLLOW, true);
        return intent;
    }


    /**
     * Request to unfollow blog by blog url
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param url      The URL of the blog to unfollow
     * @return Intent to start {@link QueryService}
     */
    public static Intent userUnfollowBlog(@NonNull Context context, @NonNull UserBlogFollowReceiver receiver, @NonNull String url) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_BLOG_FOLLOW);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_FOLLOW, false);
        return intent;
    }


    /**
     * Request to like post
     *
     * @param context   package context
     * @param receiver  receiver to get response data
     * @param id        The ID of the post to like
     * @param reblogKey The reblog key for the post id
     * @return Intent to start {@link QueryService}
     */
    public static Intent userLikePost(@NonNull Context context, @NonNull UserLikePostReceiver receiver, long id, @NonNull String reblogKey) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_POST_LIKE);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_REBLOG, reblogKey);
        intent.putExtra(KEY_LIKE, true);
        return intent;
    }


    /**
     * Request to unlike post
     *
     * @param context   package context
     * @param receiver  receiver to get response data
     * @param id        The ID of the post to unlike
     * @param reblogKey The reblog key for the post id
     * @return Intent to start {@link QueryService}
     */
    public static Intent userUnlikePost(@NonNull Context context, @NonNull UserLikePostReceiver receiver, long id, @NonNull String reblogKey) {
        Intent intent = getBaseIntent(context, receiver, ACTION_USER_POST_LIKE);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_REBLOG, reblogKey);
        intent.putExtra(KEY_LIKE, false);
        return intent;
    }


    /**
     * Request to get tagged posts
     *
     * @param context  package context
     * @param receiver receiver to get response data
     * @param tag      The tag on the posts you'd like to retrieve
     * @param before   The timestamp of when you'd like to see posts before.
     *                 If the Tag is a "featured" tag, use the "featured_timestamp" on the post object for pagination.
     * @param limit    The number of results to return: 1–20, inclusive
     * @param filter   Specifies the post format to return, other than HTML:
     *                 <li>text – Plain text, no HTML
     *                 <li>raw – As entered by the user (no post-processing); if the user writes in Markdown, the Markdown will be returned rather than HTML
     * @return Intent to start {@link QueryService}
     */
    public static Intent tagged(@NonNull Context context, @NonNull TaggedReceiver receiver, @NonNull String tag, long before, int limit, @Nullable @TumblrExtras.FilterType String filter) {
        Intent intent = getBaseIntent(context, receiver, ACTION_TAGGED);
        intent.putExtra(KEY_TAG, tag);
        intent.putExtra(KEY_BEFORE, checkTimestamp(before));
        intent.putExtra(KEY_LIMIT, checkLimit(limit));
        intent.putExtra(KEY_FILTER, filter);
        return intent;
    }
}
