# Robo-tumblr

Simple Android library to fetch and publish data via [Tumblr API](https://www.tumblr.com/docs/en/api/v2). Provides simple arhitecture to manipulate data fetched from server and create/edit posts in Android lifecycle. 

*Robo-tumblr requires at least sdk version 10 (2.3.3) and Java 7*

##Download
	repositories {
        jcenter()
	}

	compile 'com.github.sun40:robo-tumblr:1.0.0'

##How to

Before use you should register your aplication [here](https://www.tumblr.com/oauth/apps)  and redefine current variables in your android resource xml file


    <string name="consumer_key">YOUR_CONSUMER_KEY</string>
    <string name="consumer_secret_key">YOUR_SECRET_CONSUMER_KEY</string>


##Dependencies
Robo-tumblr uses:

- appcompat-v7 23.0.0
- retrofit 1.9.0


##Features

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


