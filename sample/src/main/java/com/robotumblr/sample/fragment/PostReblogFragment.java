package com.robotumblr.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.BlogPostsReceiver;
import com.sun40.robotumblr.receiver.BlogReblogReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 9/27/2015 4:25 PM.
 */
public class PostReblogFragment extends BaseFragment implements BlogPostsReceiver.BlogPostsListener, BlogReblogReceiver.BlogReblogListener {

    private BlogPostsReceiver mBlogPostsReceiver;
    private BlogReblogReceiver mBlogReblogReceiver;

    private Spinner mPostSpinner;
    private Spinner mHostnameSpinner;
    private EditText mCommentEdit;

    private List<String> mHostnames = new ArrayList<>();
    private List<Long> mIds = new ArrayList<>();
    private List<Post> mPosts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogPostsReceiver = new BlogPostsReceiver(new Handler());
        mBlogReblogReceiver = new BlogReblogReceiver(new Handler());
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogPostsReceiver.attach(this);
        mBlogReblogReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBlogPostsReceiver.detach();
        mBlogReblogReceiver.detach();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_audio, null, false);

        mPostSpinner = (Spinner) rootView.findViewById(R.id.post_spinner);
        mHostnameSpinner = (Spinner) rootView.findViewById(R.id.hostname_spinner);
        mCommentEdit = (EditText) rootView.findViewById(R.id.comment_et);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null && user.blogs != null) {
            for (Blog blog : user.blogs)
                mHostnames.add(blog.name);
        }

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mHostnames);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHostnameSpinner.setAdapter(nameAdapter);


        if (!mHostnames.isEmpty()) {
            mHostnameSpinner.setSelection(0);
            getActivity().startService(TumblrService.blogPosts(getActivity(), mBlogPostsReceiver, getString(R.string.developers_hostname), 20, 0));
        } else {
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
        }

        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_post_reblog;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_post_reblog;
    }

    @Override
    protected void onRun() {
        if (mHostnames.isEmpty()) {
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
            return;
        }

        if (mPosts.isEmpty()) {
            Toast.makeText(getContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Post post = mPosts.get(mPostSpinner.getSelectedItemPosition());
        String hostname = mHostnames.get(mHostnameSpinner.getSelectedItemPosition());
        getActivity().startService(TumblrService.reblogPost(getActivity(), mBlogReblogReceiver, hostname, post, mCommentEdit.getText().toString()));
        showDialog();
    }

    @Override
    public void onBlogPostsFail(String error) {
        hideDialog();
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogPostsSuccess(Blog blog, List<Post> posts, int limit, int offset, String tag, String type, boolean reblogInfo, boolean notesInfo, String filter, int total) {
        mPosts.clear();
        mPosts.addAll(posts);
        mIds.clear();

        for (Post post : mPosts) {
            mIds.add(post.getId());
        }

        ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(getContext(), android.R.layout.simple_spinner_item, mIds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPostSpinner.setAdapter(adapter);
    }

    @Override
    public void onReblogFail(String error) {
        hideDialog();
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReblogSuccess(long id) {
        hideDialog();
        Toast.makeText(getContext(), "OK " + id, Toast.LENGTH_SHORT).show();
    }
}
