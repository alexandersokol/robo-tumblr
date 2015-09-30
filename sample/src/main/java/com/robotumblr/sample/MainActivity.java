package com.robotumblr.sample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.fragment.BlogAvatarFragment;
import com.robotumblr.sample.fragment.BlogDraftFragment;
import com.robotumblr.sample.fragment.BlogFollowersFragment;
import com.robotumblr.sample.fragment.BlogInfoFragment;
import com.robotumblr.sample.fragment.BlogLikesFragment;
import com.robotumblr.sample.fragment.BlogPostByIdFragment;
import com.robotumblr.sample.fragment.BlogPostsFragment;
import com.robotumblr.sample.fragment.BlogQueueFragment;
import com.robotumblr.sample.fragment.PostDeleteFragment;
import com.robotumblr.sample.fragment.PostReblogFragment;
import com.robotumblr.sample.fragment.PostAudioFragment;
import com.robotumblr.sample.fragment.PostChatFragment;
import com.robotumblr.sample.fragment.PostEditFragment;
import com.robotumblr.sample.fragment.PostLinkFragment;
import com.robotumblr.sample.fragment.PostPhotoFragment;
import com.robotumblr.sample.fragment.PostQuoteFragment;
import com.robotumblr.sample.fragment.PostTextFragment;
import com.robotumblr.sample.fragment.PostVideoFragment;
import com.robotumblr.sample.fragment.UserDashboardFragment;
import com.robotumblr.sample.fragment.UserFollowingsFragment;
import com.robotumblr.sample.fragment.UserInfoFragment;
import com.robotumblr.sample.fragment.UserLikesFragment;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.receiver.AccessTokenReceiver;
import com.sun40.robotumblr.AuthActivity;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.receiver.RequestTokenReceiver;
import com.sun40.robotumblr.receiver.UserInfoReceiver;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.token.RequestToken;
import com.sun40.robotumblr.RoboTumblr;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, RequestTokenReceiver.OnRequestTokenListener, AccessTokenReceiver.OnAccessTokenListener, UserInfoReceiver.UserInfoListener {

    private static final String TAG = "MainActivitySample";
    private static final int AUTH_CODE = 1;

    private RequestTokenReceiver mRequestTokenReceiver;

    private TextView mUserLoginTv;
    private TextView mSingUpTv;

    private DialogActionReceiver mDialogActionReceiver;
    private AccessTokenReceiver mAccessTokenReceiver;
    private UserInfoReceiver mUserInfoReceiver;

    private View mBlogFollowersBtn;
    private View mBlogQueuedBtn;
    private View mBlogDraftsBtn;
    private View mPostTextBtn;
    private View mPostPhotoBtn;
    private View mPostQuoteBtn;
    private View mPostChatBtn;
    private View mPostAudioBtn;
    private View mPostVideoBtn;
    private View mPostLinkBtn;
    private View mPostEditBtn;
    private View mPostReblogBtn;
    private View mPostDeleteBtn;

    private View mUserInfoBtn;
    private View mUserDashboardBtn;
    private View mUserLikesBtn;
    private View mUserFollowingsBtn;
    private View mUserFollowBtn;
    private View mUserUnfollowBtn;
    private View mUserLikeBtn;
    private View mUserUnlikeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestTokenReceiver = new RequestTokenReceiver(new Handler());
        mAccessTokenReceiver = new AccessTokenReceiver(new Handler());
        mUserInfoReceiver = new UserInfoReceiver(new Handler());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSingUpTv = (TextView) findViewById(R.id.singup_tv);
        mUserLoginTv = (TextView) findViewById(R.id.user_login_tv);

        mSingUpTv.setOnClickListener(this);

        mBlogFollowersBtn = findViewById(R.id.btn_blog_followers);
        mBlogQueuedBtn = findViewById(R.id.btn_blog_queued);
        mBlogDraftsBtn = findViewById(R.id.btn_blog_drafts);

        mPostTextBtn = findViewById(R.id.btn_post_text);
        mPostPhotoBtn = findViewById(R.id.btn_post_photo);
        mPostQuoteBtn = findViewById(R.id.btn_post_quote);
        mPostChatBtn = findViewById(R.id.btn_post_chat);
        mPostAudioBtn = findViewById(R.id.btn_post_audio);
        mPostVideoBtn = findViewById(R.id.btn_post_video);
        mPostLinkBtn = findViewById(R.id.btn_post_link);
        mPostEditBtn = findViewById(R.id.btn_post_edit);
        mPostReblogBtn = findViewById(R.id.btn_post_reblog);
        mPostDeleteBtn = findViewById(R.id.btn_post_delete);
//
        mUserInfoBtn = findViewById(R.id.btn_user_info);
        mUserDashboardBtn = findViewById(R.id.btn_user_dashboard);
        mUserLikesBtn = findViewById(R.id.btn_user_likes);
        mUserFollowingsBtn = findViewById(R.id.btn_user_followings);
        mUserFollowBtn = findViewById(R.id.btn_user_follow);
        mUserUnfollowBtn = findViewById(R.id.btn_user_unfollow);
        mUserLikeBtn = findViewById(R.id.btn_user_like);
        mUserUnlikeBtn = findViewById(R.id.btn_user_unlike);

        mUserLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialog.show(true, false, false, getSupportFragmentManager());
            }
        });

        try {
            RoboTumblr.getConsumerToken(this);
        } catch (IllegalArgumentException e) {
            SimpleDialog.show(false, false, true, R.string.consumer_not_defined, getSupportFragmentManager());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AUTH_CODE) {
            String verifier = data.getStringExtra(AuthActivity.KEY_AUTH_VERIFIER);
            RequestToken requestToken = data.getParcelableExtra(AuthActivity.KEY_REQUEST_TOKEN);
            SimpleDialog.show(false, false, false, getSupportFragmentManager());
            startService(QueryService.getOauthAccessToken(this, mAccessTokenReceiver, requestToken, verifier));
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mDialogActionReceiver == null)
            mDialogActionReceiver = new DialogActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL);
        filter.addAction(SimpleDialog.DIALOG_INTENT_ACTION_CLOSE);
        filter.addAction(SimpleDialog.DIALOG_INTENT_ACTION_PROGRESS);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDialogActionReceiver, filter);
        setUpCurrentUser();

        mRequestTokenReceiver.attach(this);
        mAccessTokenReceiver.attach(this);
        mUserInfoReceiver.attach(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDialogActionReceiver);

        mRequestTokenReceiver.detach();
        mAccessTokenReceiver.detach();
        mUserInfoReceiver.detach();
    }


    private void setUpCurrentUser() {
        User user = StorageUtils.getCurrentUser(this);
        boolean enabled = false;
        if (RoboTumblr.isUserLoggedIn(this) && user != null) {
            mSingUpTv.setText(R.string.logout);
            mUserLoginTv.setText(user.name);
            mUserLoginTv.setTextColor(Color.GREEN);
            enabled = true;
        } else {
            mSingUpTv.setText(R.string.login);
            mUserLoginTv.setText(R.string.not_logged_in);
            mUserLoginTv.setTextColor(Color.BLACK);
        }

        mBlogFollowersBtn.setEnabled(enabled);
        mBlogQueuedBtn.setEnabled(enabled);
        mBlogDraftsBtn.setEnabled(enabled);

        mPostTextBtn.setEnabled(enabled);
        mPostPhotoBtn.setEnabled(enabled);
        mPostQuoteBtn.setEnabled(enabled);
        mPostChatBtn.setEnabled(enabled);
        mPostAudioBtn.setEnabled(enabled);
        mPostVideoBtn.setEnabled(enabled);
        mPostLinkBtn.setEnabled(enabled);
        mPostEditBtn.setEnabled(enabled);
        mPostReblogBtn.setEnabled(enabled);
        mPostDeleteBtn.setEnabled(enabled);

        mUserInfoBtn.setEnabled(enabled);
        mUserDashboardBtn.setEnabled(enabled);
        mUserLikesBtn.setEnabled(enabled);
        mUserFollowingsBtn.setEnabled(enabled);
        mUserFollowBtn.setEnabled(enabled);
        mUserUnfollowBtn.setEnabled(enabled);
        mUserLikeBtn.setEnabled(enabled);
        mUserUnlikeBtn.setEnabled(enabled);
    }


    public void pushFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    @Override
    public void onClick(View v) {

        Fragment fragment = null;

        switch (v.getId()) {
            case R.id.singup_tv: {
                if (RoboTumblr.isUserLoggedIn(this)) {
                    RoboTumblr.clearTokenData(this);
                    StorageUtils.clearCurrentUser(this);
                    setUpCurrentUser();
                } else {
                    SimpleDialog.show(false, false, false, getSupportFragmentManager());
                    startService(QueryService.getOAuthRequestToken(this, mRequestTokenReceiver));
                }
                return;
            }

            case R.id.btn_blog_info:
                fragment = new BlogInfoFragment();
                break;

            case R.id.btn_blog_avatar:
                fragment = new BlogAvatarFragment();
                break;

            case R.id.btn_blog_likes:
                fragment = new BlogLikesFragment();
                break;

            case R.id.btn_blog_followers:
                fragment = new BlogFollowersFragment();
                break;

            case R.id.btn_blog_posts:
                fragment = new BlogPostsFragment();
                break;

            case R.id.btn_blog_post_by_id:
                fragment = new BlogPostByIdFragment();
                break;

            case R.id.btn_blog_queued:
                fragment = new BlogQueueFragment();
                break;

            case R.id.btn_blog_drafts:
                fragment = new BlogDraftFragment();
                break;

            case R.id.btn_post_text:
                fragment = new PostTextFragment();
                break;

            case R.id.btn_post_photo:
                fragment = new PostPhotoFragment();
                break;

            case R.id.btn_post_quote:
                fragment = new PostQuoteFragment();
                break;

            case R.id.btn_post_chat:
                fragment = new PostChatFragment();
                break;

            case R.id.btn_post_audio:
                fragment = new PostAudioFragment();
                break;

            case R.id.btn_post_video:
                fragment = new PostVideoFragment();
                break;

            case R.id.btn_post_link:
                fragment = new PostLinkFragment();
                break;

            case R.id.btn_post_edit:
                fragment = new PostEditFragment();
                break;

            case R.id.btn_post_reblog:
                fragment = new PostReblogFragment();
                break;

            case R.id.btn_post_delete:
                fragment = new PostDeleteFragment();
                break;

            case R.id.btn_user_info:
                fragment = new UserInfoFragment();
                break;

            case R.id.btn_user_dashboard:
                fragment = new UserDashboardFragment();
                break;

            case R.id.btn_user_likes:
                fragment = new UserLikesFragment();
                break;

            case R.id.btn_user_followings:
                fragment = new UserFollowingsFragment();
                break;

            case R.id.btn_user_follow:

                break;

            case R.id.btn_user_unfollow:

                break;

            case R.id.btn_user_like:

                break;

            case R.id.btn_user_unlike:

                break;

            case R.id.btn_tagged:

                break;

        }

        if (fragment != null) {
            pushFragment(fragment);
        } else {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestTokenSuccess(RequestToken requestToken) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        startActivityForResult(AuthActivity.startAuthorization(this, requestToken), AUTH_CODE);
    }


    @Override
    public void onRequestTokenFail(String error) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Log.e(TAG, "onRequestTokenFail " + error);
        if (!TextUtils.isEmpty(error))
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onUserNotAuthorized() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Toast.makeText(this, "User not Authorized!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onTokenInvalidated() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Toast.makeText(this, "Access Token Invalidated!", Toast.LENGTH_SHORT).show();
        RoboTumblr.clearTokenData(this);
        StorageUtils.clearCurrentUser(this);
        setUpCurrentUser();
    }

    @Override
    public void onAccessTokenFail(String error) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Log.e(TAG, "onRequestTokenFail " + error);
        if (!TextUtils.isEmpty(error))
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessTokenSuccess() {
        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
        startService(QueryService.userInfo(this, mUserInfoReceiver));
    }

    @Override
    public void onUserInfoFail(String error) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Log.e(TAG, "onRequestTokenFail " + error);
        if (!TextUtils.isEmpty(error))
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserInfoSuccess(User user) {
        StorageUtils.setCurrentUser(this, user);
        setUpCurrentUser();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
    }


    private class DialogActionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SimpleDialog.DIALOG_INTENT_ACTION_CLOSE)) {
                finish();
            }
        }
    }

}
