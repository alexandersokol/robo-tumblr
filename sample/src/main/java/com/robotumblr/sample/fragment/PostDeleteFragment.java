package com.robotumblr.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.BlogPostDeleteReceiver;
import com.sun40.robotumblr.receiver.BlogPostsReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 29.09.15 9:43.
 */
public class PostDeleteFragment extends BaseFragment implements BlogPostsReceiver.BlogPostsListener, BlogPostDeleteReceiver.BlogPostDeleteListener {

    private BlogPostsReceiver mBlogPostsReceiver;
    private BlogPostDeleteReceiver mBlogPostDeleteReceiver;

    private Spinner mHosnamesSpinner;
    private Spinner mPostSpinner;

    private List<String> mHostnames = new ArrayList<>();
    private List<Post> mPosts = new ArrayList<>();
    private List<String> mPostNames = new ArrayList<>();
    private ArrayAdapter<String> postAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogPostsReceiver = new BlogPostsReceiver(new Handler());
        mBlogPostDeleteReceiver = new BlogPostDeleteReceiver(new Handler());
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogPostsReceiver.attach(this);
        mBlogPostDeleteReceiver.attach(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mBlogPostDeleteReceiver.detach();
        mBlogPostsReceiver.detach();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_delete, null, false);

        mHosnamesSpinner = (Spinner) rootView.findViewById(R.id.hostname_spinner);
        mPostSpinner = (Spinner) rootView.findViewById(R.id.post_spinner);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null && user.blogs != null) {
            for (Blog blog : user.blogs)
                mHostnames.add(blog.name);
        }

        ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mHostnames);
        namesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHosnamesSpinner.setAdapter(namesAdapter);

        postAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mPostNames);
        postAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPostSpinner.setAdapter(postAdapter);

        if (mHostnames.isEmpty())
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());

        mHosnamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getActivity().startService(QueryService.blogPosts(getActivity(), mBlogPostsReceiver, mHostnames.get(position), 20, 0));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
            }
        });

        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_post_delete;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_post_delete;
    }

    @Override
    protected void onRun() {
        if (mPosts.isEmpty()) {
            Toast.makeText(getContext(), "No posts to remove", Toast.LENGTH_SHORT).show();
            return;
        }

        String hostname = mHostnames.get(mHosnamesSpinner.getSelectedItemPosition());
        Post post = mPosts.get(mPostSpinner.getSelectedItemPosition());
        getActivity().startService(QueryService.deletePost(getActivity(), mBlogPostDeleteReceiver, hostname, post.getId()));
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
        mPostNames.clear();

        for (Post post : mPosts) {
            mPostNames.add(post.getId() + " " + post.getType());
        }
        postAdapter.notifyDataSetChanged();
        if(!mPostNames.isEmpty())
            mPostSpinner.setSelection(0);
    }

    @Override
    public void onBlogPostDeleteFail(String error) {
        hideDialog();
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogPostDeleteSuccess(long id) {
        Toast.makeText(getContext(), "OK " + id, Toast.LENGTH_SHORT).show();
        int removedPosition = -1;
        for (int i = 0; i < mPosts.size(); i++) {
            if (mPosts.get(i).getId() == id) {
                removedPosition = i;
                break;
            }
        }

        if(removedPosition != -1) {
            mPosts.remove(removedPosition);
            mPostNames.remove(removedPosition);
        }
        postAdapter.notifyDataSetChanged();
        if(!mPostNames.isEmpty())
            mPostSpinner.setSelection(0);
    }
}
