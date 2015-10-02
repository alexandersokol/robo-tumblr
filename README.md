# robo-tumblr

Simple Android library to fetch and publish data via [Tumblr API](https://www.tumblr.com/docs/en/api/v2). Provides simple arhitecture to manipulate data fetched from server and create/edit posts.



Methods which requires Tumblr API:

- Blog Info
- Blog Avatar
- Blog Likes
- Blog Posts
- Search tagged posts

Methods which requires both Tumblr API and user Authorization:

- Blog Followers
- Blog Queued Posts
- Blog Drafts
- Text post
- Photo post
- Quote post
- Chat post
- Audio post
- Video post
- Link post
- Reblog post
- Edit post
- Delete post
- User info
- User dashboard
- User likes
- User followings
- Follow/Unfollow blog
- Like/Unlike post

#Download
repositories {
        jcenter()
}

compile 'com.github.sun40:robo-tumblr:1.0.0'