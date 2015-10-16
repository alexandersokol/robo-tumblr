package com.robotumblr.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.receiver.BlogPostsReceiver;
import com.sun40.robotumblr.receiver.UserLikePostReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 30.09.15 19:05.
 */
public class UserLikeFragment extends BaseFragment implements BlogPostsReceiver.BlogPostsListener, UserLikePostReceiver.UserLikePostListener {

    private static final String KEY_LIKE = "LIKE";

    private BlogPostsReceiver mBlogPostsReceiver;
    private UserLikePostReceiver mLikesReceiver;

    public static UserLikeFragment getInstance(boolean like) {
        UserLikeFragment fragment = new UserLikeFragment();
        Bundle data = new Bundle();
        data.putBoolean(KEY_LIKE, like);
        fragment.setArguments(data);
        return fragment;
    }

    private boolean mLike;

    private List<Post> mPostList = new ArrayList<>();
    private List<String> mIds = new ArrayList<>();

    private Spinner mIdSpinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogPostsReceiver = new BlogPostsReceiver(new Handler());
        mLikesReceiver = new UserLikePostReceiver(new Handler());
        if (getArguments() != null)
            mLike = getArguments().getBoolean(KEY_LIKE);
    }


    @Override
    public void onResume() {
        super.onResume();
        mBlogPostsReceiver.attach(this);
        mLikesReceiver.attach(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mBlogPostsReceiver.detach();
        mLikesReceiver.detach();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_user_like, null);

        mIdSpinner = (Spinner) rootView.findViewById(R.id.post_spinner);

        getActivity().startService(TumblrService.blogPosts(getActivity(), mBlogPostsReceiver, getString(R.string.developers_hostname), 20, 0));
        showDialog();
        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return mLike ? R.string.title_user_like : R.string.title_user_unlike;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_user_methods;
    }

    @Override
    protected void onRun() {
        if (mIds.isEmpty()) {
            Toast.makeText(getContext(), "Id list is empty", Toast.LENGTH_SHORT).show();
        } else {
            Post post = mPostList.get(mIdSpinner.getSelectedItemPosition());
            if (mLike)
                getActivity().startService(TumblrService.userLikePost(getActivity(), mLikesReceiver, post.getId(), post.getReblogKey()));
            else
                getActivity().startService(TumblrService.userUnlikePost(getActivity(), mLikesReceiver, post.getId(), post.getReblogKey()));
            showDialog();
        }
    }


    @Override
    public void onBlogPostsFail(String error) {
        onFail(error);
    }

    @Override
    public void onBlogPostsSuccess(Blog blog, List<Post> posts, int limit, int offset, String tag, String type, boolean reblogInfo, boolean notesInfo, String filter, int total) {
        hideDialog();
        if (posts != null) {
            mPostList.clear();
            mPostList.addAll(posts);
        }

        mIds.clear();
        for (Post post : mPostList) {
            mIds.add(post.getId() + " " + post.getReblogKey());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mIds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIdSpinner.setAdapter(adapter);
        if (!mIds.isEmpty())
            mIdSpinner.setSelection(0);
    }

    @Override
    public void onUserLikePostFail(String error) {
        onFail(error);
    }

    @Override
    public void onUserLikePostSuccess(boolean like, long postId, String reblogKey) {
        hideDialog();
        String likeStr = like ? "Liked " : "Unliked ";
        Toast.makeText(getContext(), likeStr + reblogKey, Toast.LENGTH_SHORT).show();
    }
}
