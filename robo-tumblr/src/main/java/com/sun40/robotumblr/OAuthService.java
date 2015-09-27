package com.sun40.robotumblr;

import java.util.Map;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by Alexander Sokol
 * on 18.08.15 17:50.
 */
interface OAuthService {

    @POST("/request_token")
    Response requestToken();

    @POST("/access_token")
    Response accessToken();

    @GET("/blog/{base-hostname}/info")
    ResponseContainer.BlogContainer blogInfo(@Path("base-hostname") String hostname,
                                             @Query("api_key") String api_key);

    @GET("/blog/{base-hostname}/followers")
    ResponseContainer.BlogFollowersContainer getBlogFollowers(@Path("base-hostname") String hostname,
                                                              @Query("limit") Integer limit,
                                                              @Query("offset") Integer offset);

    @GET("/blog/{base-hostname}/followers")
    ResponseContainer.BlogFollowersContainer getBlogFollowers(@Path("base-hostname") String hostname);


    @GET("/blog/{base-hostname}/posts")
    ResponseContainer.BlogPostsContainer blogPosts(@Path("base-hostname") String hostname,
                                                   @Query("api_key") String api_key,
                                                   @Query("id") Long id,
                                                   @Query("tag") String tag,
                                                   @Query("limit") Integer limit,
                                                   @Query("offset") Integer offset,
                                                   @Query("reblog_info") Boolean reblogInfo,
                                                   @Query("notes_info") Boolean notesInfo,
                                                   @Query("filter") String filter);


    @GET("/blog/{base-hostname}/posts/{type}")
    ResponseContainer.BlogPostsContainer blogPosts(@Path("base-hostname") String hostname,
                                                   @Path("type") String type,
                                                   @Query("api_key") String api_key,
                                                   @Query("id") Long id,
                                                   @Query("tag") String tag,
                                                   @Query("limit") Integer limit,
                                                   @Query("offset") Integer offset,
                                                   @Query("reblog_info") Boolean reblogInfo,
                                                   @Query("notes_info") Boolean notesInfo,
                                                   @Query("filter") String filter);

    @GET("/blog/{base-hostname}/posts")
    ResponseContainer.BlogPostsContainer blogPosts(@Path("base-hostname") String hostname,
                                                   @Query("api_key") String api_key,
                                                   @Query("id") Long id,
                                                   @Query("reblog_info") Boolean reblogInfo,
                                                   @Query("notes_info") Boolean notesInfo);


    @GET("/blog/{base-hostname}/posts/queue")
    ResponseContainer.BlogQueueContainer getBlogQueuedPosts(@Path("base-hostname") String hostname,
                                                            @Query("offset") Integer offset,
                                                            @Query("limit") Integer limit,
                                                            @Query("filter") String filter);

    @GET("/blog/{base-hostname}/posts/draft")
    ResponseContainer.BlogDraftContainer getBlogDraftPosts(@Path("base-hostname") String hostname,
                                                           @Query("limit") Integer limit,
                                                           @Query("before_id") Long beforeId,
                                                           @Query("filter") String filter);


    @Multipart
    @POST("/blog/{base-hostname}/post")
    ResponseContainer.BlogNewPostContainer textPost(@Path("base-hostname") String hostname,
                                                    @Part(TumblrExtras.Params.TYPE) String type,
                                                    @Part(TumblrExtras.Params.STATE) String state,
                                                    @Part(TumblrExtras.Params.TAGS) String tags,
                                                    @Part(TumblrExtras.Params.TWEET) String tweet,
                                                    @Part(TumblrExtras.Params.DATE) String date,
                                                    @Part(TumblrExtras.Params.FORMAT) String format,
                                                    @Part(TumblrExtras.Params.SLUG) String slug,
                                                    @Part(TumblrExtras.Params.TITLE) String title,
                                                    @Part(TumblrExtras.Params.BODY) String body);

    @Multipart
    @POST("/blog/{base-hostname}/post/edit")
    ResponseContainer.BlogNewPostContainer textPostEdit(@Path("base-hostname") String hostname,
                                                        @Part(TumblrExtras.Params.ID) Long id,
                                                        @Part(TumblrExtras.Params.TYPE) String type,
                                                        @Part(TumblrExtras.Params.STATE) String state,
                                                        @Part(TumblrExtras.Params.TAGS) String tags,
                                                        @Part(TumblrExtras.Params.TWEET) String tweet,
                                                        @Part(TumblrExtras.Params.DATE) String date,
                                                        @Part(TumblrExtras.Params.FORMAT) String format,
                                                        @Part(TumblrExtras.Params.SLUG) String slug,
                                                        @Part(TumblrExtras.Params.TITLE) String title,
                                                        @Part(TumblrExtras.Params.BODY) String body);

    @Multipart
    @POST("/blog/{base-hostname}/post")
    ResponseContainer.BlogNewPostContainer photoPost(@Path("base-hostname") String hostname,
                                                     @Part(TumblrExtras.Params.TYPE) String type,
                                                     @Part(TumblrExtras.Params.STATE) String state,
                                                     @Part(TumblrExtras.Params.TAGS) String tags,
                                                     @Part(TumblrExtras.Params.TWEET) String tweet,
                                                     @Part(TumblrExtras.Params.DATE) String date,
                                                     @Part(TumblrExtras.Params.FORMAT) String format,
                                                     @Part(TumblrExtras.Params.SLUG) String slug,
                                                     @Part(TumblrExtras.Params.CAPTION) String caption,
                                                     @Part(TumblrExtras.Params.LINK) String link,
                                                     @Part(TumblrExtras.Params.SOURCE) String source,
                                                     @PartMap Map<String, TypedFile> data);

    @Multipart
    @POST("/blog/{base-hostname}/post/edit")
    ResponseContainer.BlogNewPostContainer photoPostEdit(@Path("base-hostname") String hostname,
                                                         @Part(TumblrExtras.Params.ID) Long id,
                                                         @Part(TumblrExtras.Params.TYPE) String type,
                                                         @Part(TumblrExtras.Params.STATE) String state,
                                                         @Part(TumblrExtras.Params.TAGS) String tags,
                                                         @Part(TumblrExtras.Params.TWEET) String tweet,
                                                         @Part(TumblrExtras.Params.DATE) String date,
                                                         @Part(TumblrExtras.Params.FORMAT) String format,
                                                         @Part(TumblrExtras.Params.SLUG) String slug,
                                                         @Part(TumblrExtras.Params.CAPTION) String caption,
                                                         @Part(TumblrExtras.Params.LINK) String link,
                                                         @Part(TumblrExtras.Params.SOURCE) String source,
                                                         @PartMap Map<String, TypedFile> data);

    @Multipart
    @POST("/blog/{base-hostname}/post")
    ResponseContainer.BlogNewPostContainer quotePost(@Path("base-hostname") String hostname,
                                                     @Part(TumblrExtras.Params.TYPE) String type,
                                                     @Part(TumblrExtras.Params.STATE) String state,
                                                     @Part(TumblrExtras.Params.TAGS) String tags,
                                                     @Part(TumblrExtras.Params.TWEET) String tweet,
                                                     @Part(TumblrExtras.Params.DATE) String date,
                                                     @Part(TumblrExtras.Params.FORMAT) String format,
                                                     @Part(TumblrExtras.Params.SLUG) String slug,
                                                     @Part(TumblrExtras.Params.QUOTE) String quote,
                                                     @Part(TumblrExtras.Params.SOURCE) String source);


    @Multipart
    @POST("/blog/{base-hostname}/post/edit")
    ResponseContainer.BlogNewPostContainer quotePostEdit(@Path("base-hostname") String hostname,
                                                         @Part(TumblrExtras.Params.ID) Long id,
                                                         @Part(TumblrExtras.Params.TYPE) String type,
                                                         @Part(TumblrExtras.Params.STATE) String state,
                                                         @Part(TumblrExtras.Params.TAGS) String tags,
                                                         @Part(TumblrExtras.Params.TWEET) String tweet,
                                                         @Part(TumblrExtras.Params.DATE) String date,
                                                         @Part(TumblrExtras.Params.FORMAT) String format,
                                                         @Part(TumblrExtras.Params.SLUG) String slug,
                                                         @Part(TumblrExtras.Params.QUOTE) String quote,
                                                         @Part(TumblrExtras.Params.SOURCE) String source);


    @Multipart
    @POST("/blog/{base-hostname}/post")
    ResponseContainer.BlogNewPostContainer linkPost(@Path("base-hostname") String hostname,
                                                    @Part(TumblrExtras.Params.TYPE) String type,
                                                    @Part(TumblrExtras.Params.STATE) String state,
                                                    @Part(TumblrExtras.Params.TAGS) String tags,
                                                    @Part(TumblrExtras.Params.TWEET) String tweet,
                                                    @Part(TumblrExtras.Params.DATE) String date,
                                                    @Part(TumblrExtras.Params.FORMAT) String format,
                                                    @Part(TumblrExtras.Params.SLUG) String slug,
                                                    @Part(TumblrExtras.Params.TITLE) String title,
                                                    @Part(TumblrExtras.Params.URL) String url,
                                                    @Part(TumblrExtras.Params.DESCRIPTION) String description);

    @Multipart
    @POST("/blog/{base-hostname}/post/edit")
    ResponseContainer.BlogNewPostContainer linkPostEdit(@Path("base-hostname") String hostname,
                                                        @Part(TumblrExtras.Params.ID) Long id,
                                                        @Part(TumblrExtras.Params.TYPE) String type,
                                                        @Part(TumblrExtras.Params.STATE) String state,
                                                        @Part(TumblrExtras.Params.TAGS) String tags,
                                                        @Part(TumblrExtras.Params.TWEET) String tweet,
                                                        @Part(TumblrExtras.Params.DATE) String date,
                                                        @Part(TumblrExtras.Params.FORMAT) String format,
                                                        @Part(TumblrExtras.Params.SLUG) String slug,
                                                        @Part(TumblrExtras.Params.TITLE) String title,
                                                        @Part(TumblrExtras.Params.URL) String url,
                                                        @Part(TumblrExtras.Params.DESCRIPTION) String description);

    @Multipart
    @POST("/blog/{base-hostname}/post")
    ResponseContainer.BlogNewPostContainer chatPost(@Path("base-hostname") String hostname,
                                                    @Part(TumblrExtras.Params.TYPE) String type,
                                                    @Part(TumblrExtras.Params.STATE) String state,
                                                    @Part(TumblrExtras.Params.TAGS) String tags,
                                                    @Part(TumblrExtras.Params.TWEET) String tweet,
                                                    @Part(TumblrExtras.Params.DATE) String date,
                                                    @Part(TumblrExtras.Params.FORMAT) String format,
                                                    @Part(TumblrExtras.Params.SLUG) String slug,
                                                    @Part(TumblrExtras.Params.TITLE) String title,
                                                    @Part(TumblrExtras.Params.CONVERSATION) String conversation);

    @Multipart
    @POST("/blog/{base-hostname}/post/edit")
    ResponseContainer.BlogNewPostContainer chatPostEdit(@Path("base-hostname") String hostname,
                                                        @Part(TumblrExtras.Params.ID) Long id,
                                                        @Part(TumblrExtras.Params.TYPE) String type,
                                                        @Part(TumblrExtras.Params.STATE) String state,
                                                        @Part(TumblrExtras.Params.TAGS) String tags,
                                                        @Part(TumblrExtras.Params.TWEET) String tweet,
                                                        @Part(TumblrExtras.Params.DATE) String date,
                                                        @Part(TumblrExtras.Params.FORMAT) String format,
                                                        @Part(TumblrExtras.Params.SLUG) String slug,
                                                        @Part(TumblrExtras.Params.TITLE) String title,
                                                        @Part(TumblrExtras.Params.CONVERSATION) String conversation);

    @Multipart
    @POST("/blog/{base-hostname}/post")
    ResponseContainer.BlogNewPostContainer audioPost(@Path("base-hostname") String hostname,
                                                     @Part(TumblrExtras.Params.TYPE) String type,
                                                     @Part(TumblrExtras.Params.STATE) String state,
                                                     @Part(TumblrExtras.Params.TAGS) String tags,
                                                     @Part(TumblrExtras.Params.TWEET) String tweet,
                                                     @Part(TumblrExtras.Params.DATE) String date,
                                                     @Part(TumblrExtras.Params.FORMAT) String format,
                                                     @Part(TumblrExtras.Params.SLUG) String slug,
                                                     @Part(TumblrExtras.Params.CAPTION) String caption,
                                                     @Part(TumblrExtras.Params.EXTERNAL_URL) String externalUrl,
                                                     @Part(TumblrExtras.Params.DATA) TypedFile data);

    @Multipart
    @POST("/blog/{base-hostname}/post/edit")
    ResponseContainer.BlogNewPostContainer audioPostEdit(@Path("base-hostname") String hostname,
                                                         @Part(TumblrExtras.Params.ID) Long id,
                                                         @Part(TumblrExtras.Params.TYPE) String type,
                                                         @Part(TumblrExtras.Params.STATE) String state,
                                                         @Part(TumblrExtras.Params.TAGS) String tags,
                                                         @Part(TumblrExtras.Params.TWEET) String tweet,
                                                         @Part(TumblrExtras.Params.DATE) String date,
                                                         @Part(TumblrExtras.Params.FORMAT) String format,
                                                         @Part(TumblrExtras.Params.SLUG) String slug,
                                                         @Part(TumblrExtras.Params.CAPTION) String caption,
                                                         @Part(TumblrExtras.Params.EXTERNAL_URL) String externalUrl,
                                                         @Part(TumblrExtras.Params.DATA) TypedFile data);

    @Multipart
    @POST("/blog/{base-hostname}/post")
    ResponseContainer.BlogNewPostContainer videoPost(@Path("base-hostname") String hostname,
                                                     @Part(TumblrExtras.Params.TYPE) String type,
                                                     @Part(TumblrExtras.Params.STATE) String state,
                                                     @Part(TumblrExtras.Params.TAGS) String tags,
                                                     @Part(TumblrExtras.Params.TWEET) String tweet,
                                                     @Part(TumblrExtras.Params.DATE) String date,
                                                     @Part(TumblrExtras.Params.FORMAT) String format,
                                                     @Part(TumblrExtras.Params.SLUG) String slug,
                                                     @Part(TumblrExtras.Params.CAPTION) String caption,
                                                     @Part(TumblrExtras.Params.EMBED) String embed,
                                                     @Part(TumblrExtras.Params.DATA) TypedFile data);


    @Multipart
    @POST("/blog/{base-hostname}/post/edit")
    ResponseContainer.BlogNewPostContainer videoPostEdit(@Path("base-hostname") String hostname,
                                                         @Part(TumblrExtras.Params.ID) Long id,
                                                         @Part(TumblrExtras.Params.TYPE) String type,
                                                         @Part(TumblrExtras.Params.STATE) String state,
                                                         @Part(TumblrExtras.Params.TAGS) String tags,
                                                         @Part(TumblrExtras.Params.TWEET) String tweet,
                                                         @Part(TumblrExtras.Params.DATE) String date,
                                                         @Part(TumblrExtras.Params.FORMAT) String format,
                                                         @Part(TumblrExtras.Params.SLUG) String slug,
                                                         @Part(TumblrExtras.Params.CAPTION) String caption,
                                                         @Part(TumblrExtras.Params.EMBED) String embed,
                                                         @Part(TumblrExtras.Params.DATA) TypedFile data);


    @Multipart
    @POST("/blog/{base-hostname}/post/reblog")
    ResponseContainer.BlogNewPostContainer reblogPost(@Path("base-hostname") String hostname,
                                                      @Part(TumblrExtras.Params.ID) Long id,
                                                      @Part(TumblrExtras.Params.TYPE) String type,
                                                      @Part(TumblrExtras.Params.STATE) String state,
                                                      @Part(TumblrExtras.Params.TAGS) String tags,
                                                      @Part(TumblrExtras.Params.TWEET) String tweet,
                                                      @Part(TumblrExtras.Params.DATE) String date,
                                                      @Part(TumblrExtras.Params.FORMAT) String format,
                                                      @Part(TumblrExtras.Params.SLUG) String slug,
                                                      @Part(TumblrExtras.Params.REBLOG_KEY) String reblog_key,
                                                      @Part(TumblrExtras.Params.COMMENT) String comment);

    @Multipart
    @POST("/blog/{base-hostname}/post/delete")
    ResponseContainer.BlogNewPostContainer deletePost(@Path("base-hostname") String hostname,
                                                      @Part(TumblrExtras.Params.ID) Long id);

    @GET("/user/info")
    ResponseContainer.UserInfoContainer userInfo();

    @GET("/user/dashboard")
    ResponseContainer.BlogPostsContainer userDashboard(@Query("limit") Integer limit,
                                                       @Query("offset") Integer offset,
                                                       @Query("type") String type,
                                                       @Query("since_id") Long sinceId,
                                                       @Query("reblog_info") Boolean reblogInfo,
                                                       @Query("notes_info") Boolean notesInfo);

    @GET("/user/likes")
    ResponseContainer.UserLikesContainer userLikes(@Query("limit") Integer limit,
                                                   @Query("offset") Integer offset,
                                                   @Query("before") Long before,
                                                   @Query("after") Long after);

    @GET("/user/following")
    ResponseContainer.UserFollowingBlogsContainer userFollowingBlogs(@Query("limit") Integer limit,
                                                                     @Query("offset") Integer offset);


    @Multipart
    @POST("/user/follow")
    Response userFollowBlog(@Part(TumblrExtras.Params.URL) String url);


    @Multipart
    @POST("/user/unfollow")
    Response userUnfollowBlog(@Part(TumblrExtras.Params.URL) String url);


    @Multipart
    @POST("/user/like")
    Response userLikePost(@Part(TumblrExtras.Params.ID) long id,
                          @Part(TumblrExtras.Params.REBLOG_KEY) String reblogKey);


    @Multipart
    @POST("/user/unlike")
    Response userUnlikePost(@Part(TumblrExtras.Params.ID) long id,
                            @Part(TumblrExtras.Params.REBLOG_KEY) String reblogKey);

    @GET("/tagged")
    ResponseContainer.TaggedContainer tagged(@Query("tag") String tag,
                                             @Query("before") Long before,
                                             @Query("limit") Integer limit,
                                             @Query("filter") String filter,
                                             @Query("api_key") String apiKey);
}

