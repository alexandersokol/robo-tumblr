package com.sun40.robotumblr;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Alexander Sokol
 * on 01.09.15 17:18.
 */
interface ApiService {

    String API_ENDPOINT = "http://api.tumblr.com/v2";

    @GET("/blog/{base-hostname}/info")
    ResponseContainer.BlogContainer blogInfo(@Path("base-hostname") String hostname,
                                             @Query("api_key") String api_key);


    @GET("/blog/{base-hostname}/avatar/{size}")
    Response blogAvatar(@Path("base-hostname") String hostname,
                        @Path("size") int size);

    @GET("/blog/{base-hostname}/avatar")
    Response blogAvatar(@Path("base-hostname") String hostname);


    @GET("/blog/{base-hostname}/likes")
    ResponseContainer.BlogLikesContainer blogLikes(@Path("base-hostname") String hostname,
                                                   @Query("limit") Integer limit,
                                                   @Query("offset") Integer offset,
                                                   @Query("api_key") String api_key);


    @GET("/blog/{base-hostname}/likes")
    ResponseContainer.BlogLikesContainer blogLikesBefore(@Path("base-hostname") String hostname,
                                                         @Query("limit") Integer limit,
                                                         @Query("before") Long before,
                                                         @Query("api_key") String api_key);


    @GET("/blog/{base-hostname}/likes")
    ResponseContainer.BlogLikesContainer blogLikesAfter(@Path("base-hostname") String hostname,
                                                        @Query("limit") Integer limit,
                                                        @Query("after") Long after,
                                                        @Query("api_key") String api_key);


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

    @GET("/tagged")
    ResponseContainer.TaggedContainer tagged(@Query("tag") String tag,
                                             @Query("before") Long before,
                                             @Query("limit") Integer limit,
                                             @Query("filter") String filter,
                                             @Query("api_key") String apiKey);


    @GET("/search/{param}")
    ResponseContainer.SearchContainer search(@Path("param") String param,
                                             @Query("api_key") String apiKey);


    @GET("/search/{param}")
        // TODO: 16.09.15 does not work well yet
    ResponseContainer.SearchContainer search(@Path("param") String param,
                                             @Query("search_type") String searchType,
                                             @Query("api_key") String apiKey);
}
