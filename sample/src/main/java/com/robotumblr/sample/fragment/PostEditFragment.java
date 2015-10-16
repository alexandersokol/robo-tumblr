package com.robotumblr.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.robotumblr.sample.MainActivity;
import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.BlogPostsReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 9/27/2015 12:28 PM.
 */
public class PostEditFragment extends BaseFragment implements BlogPostsReceiver.BlogPostsListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private BlogPostsReceiver mBlogPostsReceiver;

    private List<Post> mPosts = new ArrayList<>();
    private List<String> mPostNames = new ArrayList<>();
    private List<String> mHostNames = new ArrayList<>();

    private ArrayAdapter<String> mPostAdapter;
    private String mCurrentHostname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogPostsReceiver = new BlogPostsReceiver(new Handler());
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_edit, null, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        Spinner hostnameSpinner = (Spinner) rootView.findViewById(R.id.hostname_spinner);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null && user.blogs != null) {
            for (Blog blog : user.blogs)
                mHostNames.add(blog.name);
        }

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mHostNames);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPostAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mPostNames);

        listView.setAdapter(mPostAdapter);
        listView.setOnItemClickListener(this);

        hostnameSpinner.setAdapter(nameAdapter);
        hostnameSpinner.setOnItemSelectedListener(this);

        if (mHostNames.isEmpty())
            hostnameSpinner.setSelection(0);

        return rootView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post post = mPosts.get(position);
        Fragment fragment = null;
        switch (post.getType()) {
            case TumblrExtras.Post.TEXT:
                fragment = new PostTextFragment();
                break;

            case TumblrExtras.Post.QUOTE:
                fragment = new PostQuoteFragment();
                break;

            case TumblrExtras.Post.LINK:
                fragment = new PostLinkFragment();
                break;

            case TumblrExtras.Post.VIDEO:
                fragment = new PostVideoFragment();
                break;

            case TumblrExtras.Post.AUDIO:
                fragment = new PostVideoFragment();
                break;

            case TumblrExtras.Post.PHOTO:
                fragment = new PostPhotoFragment();
                break;

            case TumblrExtras.Post.CHAT:
                fragment = new PostChatFragment();
                break;
        }

        if (fragment != null) {
            Bundle arg = new Bundle();
            arg.putParcelable(BasePostFragment.KEY_POST, post);
            arg.putString(BasePostFragment.KEY_HOSTNAME, mCurrentHostname);
            fragment.setArguments(arg);
            ((MainActivity) getActivity()).pushFragment(fragment);
        } else
            Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCurrentHostname = mHostNames.get(position);
        getActivity().startService(TumblrService.blogPosts(getContext(), mBlogPostsReceiver, mCurrentHostname, 0, 20));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogPostsReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBlogPostsReceiver.detach();
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_post_edit;
    }

    @Override
    protected boolean isRunBtnVisible() {
        return false;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_post_edit;
    }

    @Override
    protected void onRun() {

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
        hideDialog();
        mPosts.clear();
        mPosts.addAll(posts);

        mPostNames.clear();

        for (Post post : mPosts) {
            mPostNames.add(post.getId() + "  " + post.getType());
        }
        mPostAdapter.notifyDataSetChanged();
    }
}
