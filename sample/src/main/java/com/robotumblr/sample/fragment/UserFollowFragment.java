package com.robotumblr.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.receiver.BlogInfoReceiver;
import com.sun40.robotumblr.receiver.UserBlogFollowReceiver;

/**
 * Created by Alexander Sokol
 * on 30.09.15 18:39.
 */
public class UserFollowFragment extends BaseFragment implements UserBlogFollowReceiver.UserBlogFollowListener, BlogInfoReceiver.OnBlogInfoListener {

    private static final String KEY_FOLLOW = "FOLLOW";

    public static UserFollowFragment getInstance(boolean follow) {
        UserFollowFragment fragment = new UserFollowFragment();
        Bundle data = new Bundle();
        data.putBoolean(KEY_FOLLOW, follow);
        fragment.setArguments(data);
        return fragment;
    }


    private UserBlogFollowReceiver mUserFollowReceiver;
    private BlogInfoReceiver mBlogInfoReceiver;
    private Blog mBlog;
    private boolean mFollow;

    private TextView mBlogUrlText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mFollow = getArguments().getBoolean(KEY_FOLLOW);
        mUserFollowReceiver = new UserBlogFollowReceiver(new Handler());
        mBlogInfoReceiver = new BlogInfoReceiver(new Handler());
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserFollowReceiver.attach(this);
        mBlogInfoReceiver.attach(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mUserFollowReceiver.detach();
        mBlogInfoReceiver.detach();
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_user_follow, null);

        TextView blogFollowText = (TextView) rootView.findViewById(R.id.blog_tv);
        blogFollowText.setText(mFollow ? R.string.blog_to_follow : R.string.blog_to_unfollow);
        mBlogUrlText = (TextView) rootView.findViewById(R.id.blog_url_tv);

        getActivity().startService(TumblrService.blogInfo(getActivity(), mBlogInfoReceiver, getString(R.string.developers_hostname)));
        showDialog();
        return rootView;
    }


    @Override
    protected int getToolbarText() {
        if (mFollow)
            return R.string.title_user_follow;
        else
            return R.string.title_user_unfollow;
    }

    @Override
    protected int getApiReference() {
        return R.string.user_methods;
    }

    @Override
    protected void onRun() {
        if (mBlog == null || mBlog.url == null) {
            Toast.makeText(getContext(), "Blog is null", Toast.LENGTH_SHORT).show();
        } else {
            if (mFollow)
                getActivity().startService(TumblrService.userFollowBlog(getActivity(), mUserFollowReceiver, mBlog.url));
            else
                getActivity().startService(TumblrService.userUnfollowBlog(getActivity(), mUserFollowReceiver, mBlog.url));
            showDialog();
        }
    }

    @Override
    public void onUserBlogFollowFail(String error) {
        onFail(error);
    }

    @Override
    public void onUserBlogFollowSuccess(boolean follow, String url) {
        hideDialog();
        String followStr = follow ? "Follow " : "Unfollow ";
        Toast.makeText(getContext(), followStr + getString(R.string.developers_hostname), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogInfoFail(String error) {
        onFail(error);
    }

    @Override
    public void onBlogInfoSuccess(Blog blog) {
        hideDialog();
        if (blog != null && blog.url != null) {
            mBlog = blog;
            mBlogUrlText.setText(mBlog.url);
        }
    }
}
