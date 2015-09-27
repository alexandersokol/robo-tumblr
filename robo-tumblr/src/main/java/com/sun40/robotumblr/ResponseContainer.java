package com.sun40.robotumblr;

import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.RawPost;
import com.sun40.robotumblr.model.SimpleBlog;
import com.sun40.robotumblr.model.SimpleUser;
import com.sun40.robotumblr.model.Tag;
import com.sun40.robotumblr.model.User;

/**
 * Created by Alexander Sokol
 * on 01.09.15 17:35.
 */
class ResponseContainer {

    public static class BlogContainer {
        Meta meta;
        BlogResponse response;
    }

    public static class Meta {
        int status;
        String msg;
    }

    public static class BlogResponse {
        Blog blog;
    }


    public static class BlogLikesContainer {
        Meta meta;
        BlogLikesResponse response;
    }


    public static class BlogLikesResponse {
        RawPost[] liked_posts;
        int liked_count;
    }


    public static class BlogFollowersContainer {
        Meta meta;
        BlogFollowersResponse response;
    }


    public static class BlogFollowersResponse {
        int total_users;
        SimpleUser[] users;
    }


    public static class BlogPostsContainer {
        Meta meta;
        BlogPostsResponse response;
    }

    public static class BlogPostsResponse {
        Blog blog;
        RawPost[] posts;
        int total_posts;
    }


    public static class BlogQueueContainer {
        Meta meta;
        BlogQueueResponse response;
    }

    public static class BlogQueueResponse {
        RawPost[] posts;
    }


    public static class BlogDraftContainer {
        Meta meta;
        BlogDraftResponse response;
    }

    public static class BlogDraftResponse {
        RawPost[] posts;
    }

    public static class BlogNewPostContainer {
        Meta meta;
        BlogNewPostResponse response;
    }

    public static class BlogNewPostResponse {
        long id;
    }


    public static class UserInfoContainer {
        Meta meta;
        UserInfoResponse response;
    }

    public static class UserInfoResponse {
        User user;
    }


    public static class UserLikesContainer {
        Meta meta;
        UserLikesResponse response;
    }

    public static class UserLikesResponse {
        RawPost[] liked_posts;
        int liked_count;
    }


    public static class UserFollowingBlogsContainer {
        Meta meta;
        UserFollowingBlogsResponse response;
    }


    public static class UserFollowingBlogsResponse {
        int total_blogs;
        SimpleBlog[] blogs;
    }


    public static class TaggedContainer {
        Meta meta;
        RawPost[] response;
    }


    public static class SearchContainer {
        Meta meta;
        SearchResponse response;
    }

    public static class SearchResponse {
        Blog[] blogs;
        Tag[] tags;
    }
}
