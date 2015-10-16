package com.sun40.robotumblr;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.token.AccessToken;
import com.sun40.robotumblr.token.ConsumerToken;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by Alexander Sokol
 * on 16.10.15.
 */
public class TumblrRequest {

    public static final String TAG = "TumblrRequest";

    private RequestCore mRequestCore;


    public TumblrRequest(ConsumerToken consumerToken) {
        this(consumerToken, null);
    }


    public TumblrRequest(ConsumerToken consumerToken, AccessToken accessToken) {
        mRequestCore = new RequestCore(consumerToken);
        setAccessToken(accessToken);
    }


    public synchronized void setAccessToken(AccessToken accessToken) {
        mRequestCore.setAccessToken(accessToken);
    }


    /**
     * Request to get Blog info, has additional params while user is authorized see {@link com.sun40.robotumblr.model.Blog}
     *
     * @param hostname host name like temp.tumblr.com
     * @return requested blog {@link Blog} or <code>null</code> on error
     * @throws RetrofitError while error occurred
     */
    public synchronized Blog blogInfo(@NonNull String hostname) throws RetrofitError {
        ResponseContainer.BlogContainer container = mRequestCore.blogInfo(hostname);

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
    public synchronized String blogAvatar(@NonNull String hostname, @TumblrExtras.TumblrSize int size) throws RetrofitError {
        return mRequestCore.blogAvatar(hostname, size);
    }


    /**
     * Request to get Blog avatar with default size 64x64
     *
     * @param hostname host name like temp.tumblr.com
     * @return Url for blog avatar or <code>null</code>
     * @throws RetrofitError while error occurred
     */
    public synchronized String blogAvatar(@NonNull String hostname) throws RetrofitError {
        return blogAvatar(hostname, TumblrExtras.Size.SIZE_UNDEFINED);
    }


    /**
     * Request to get specified blog likes by username before timestamp, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param username user name like temp.tumblr.com, were user name equals main user blog name
     * @param limit    likes limit per page
     * @param offset   likes offset from start
     * @return List of liked posts {@link Post}
     * @throws RetrofitError while error occurred
     */
    public synchronized List<Post> blogLikes(@NonNull String username, int limit, int offset) throws RetrofitError {
        ResponseContainer.BlogLikesContainer container = mRequestCore.blogLikes(username, limit, offset);
        return Utils.extractPosts(container.response.liked_posts);
    }


    /**
     * Request to get specified blog likes by username after timestamp, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param username  host name like temp.tumblr.com, were user name equals main user blog name
     * @param limit     likes limit per page
     * @param timestamp unix epoch time stamp
     * @return List of liked posts {@link Post}
     * @throws RetrofitError while error occurred
     */
    public synchronized List<Post> blogLikesBefore(@NonNull String username, int limit, long timestamp) throws RetrofitError {
        ResponseContainer.BlogLikesContainer container = mRequestCore.blogLikesBefore(username, limit, timestamp);
        return Utils.extractPosts(container.response.liked_posts);
    }


    /**
     * Request to get specified blog likes by username after timestamp, were user name equals main user blog name
     * or return 401 Not Authorized otherwise
     *
     * @param username  host name like temp.tumblr.com, were user name equals main user blog name
     * @param limit     likes limit per page
     * @param timestamp unix epoch time stamp
     * @return List of liked posts {@link Post}
     * @throws RetrofitError while error occurred
     */
    public synchronized List<Post> blogLikesAfter(@NonNull String username, int limit, long timestamp) throws RetrofitError {
        ResponseContainer.BlogLikesContainer container = mRequestCore.blogLikesAfter(username, limit, timestamp);
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
     * @return List of blog posts {@link Post}
     * @throws RetrofitError while error occurred
     */
    public synchronized List<Post> blogPosts(@NonNull String hostname,
                                             @Nullable @TumblrExtras.PostType String type,
                                             @Nullable String tag, int limit, int offset,
                                             boolean reblogInfo, boolean notesInfo,
                                             @Nullable @TumblrExtras.FilterType String filter) throws RetrofitError {

        ResponseContainer.BlogPostsContainer container = mRequestCore.blogPosts(hostname, type, tag, limit, offset, reblogInfo, notesInfo, filter);
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
     * @return List of blog posts {@link Post}
     * @throws RetrofitError while error occurred
     */
    public synchronized List<Post> blogPosts(@NonNull String hostname, int limit, int offset) throws RetrofitError {
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
     * @return List of blog posts {@link Post}
     * @throws RetrofitError while error occurred
     */
    public synchronized List<Post> blogPosts(@NonNull String hostname, @Nullable @TumblrExtras.PostType String type, int limit, int offset) throws RetrofitError {
        return blogPosts(hostname, type, null, limit, offset, false, false, null);
    }


    /**
     * Request to get blog post by id
     *
     * @param hostname   blog hostname like temp.tumblr.com
     * @param id         post id
     * @param reblogInfo Indicates whether to return reblog information (specify true or false). Returns the various reblogged_ fields. <i>default: false</i>
     * @param notesInfo  Indicates whether to return notes information (specify true or false). Returns note count and note metadata. <i>default: false<i/>
     * @return List of blog posts {@link Post}
     * @throws RetrofitError while error occurred
     */
    public synchronized Post blogPostById(String hostname, long id, boolean reblogInfo, boolean notesInfo) throws RetrofitError {
        ResponseContainer.BlogPostsContainer container = mRequestCore.blogPostById(hostname, id, reblogInfo, notesInfo);

        if (container.response != null && container.response.posts != null && container.response.blog != null) {
            List<Post> posts = Utils.extractPosts(container.response.posts);
            if (posts.size() > 0)
                return posts.get(0);
        }

        return null;
    }


    /**
     * Request to get tagged posts
     *
     * @param tag    The tag on the posts you'd like to retrieve
     * @param before The timestamp of when you'd like to see posts before.
     *               If the Tag is a "featured" tag, use the "featured_timestamp" on the post object for pagination.
     * @param limit  The number of results to return: 1–20, inclusive
     * @param filter Specifies the post format to return, other than HTML:
     *               <li>text – Plain text, no HTML
     *               <li>raw – As entered by the user (no post-processing); if the user writes in Markdown, the Markdown will be returned rather than HTML
     * @return List of blog posts {@link Post}
     * @throws RetrofitError while error occurred
     */
    public synchronized List<Post> tagged(@NonNull String tag, long before, int limit, @Nullable @TumblrExtras.FilterType String filter) throws RetrofitError {

        ResponseContainer.TaggedContainer container = mRequestCore.tagged(tag, before, limit, filter);

        if (container != null && container.response != null)
            return Utils.extractPosts(container.response);

        return null;
    }

}
