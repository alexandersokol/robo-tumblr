package com.robotumblr.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.model.posting.PostCreator;
import com.sun40.robotumblr.receiver.BlogNewPostReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 24.09.15 16:12.
 */
abstract class BasePostFragment extends BaseFragment implements BlogNewPostReceiver.BlogPostListener {

    private BlogNewPostReceiver mBlogNewPostReceiver;

    static final String KEY_POST = "key_post";
    static final String KEY_HOSTNAME = "key_hostname";

    private final List<String> mHostnames = new ArrayList<>();
    private final List<String> mStates = new ArrayList<>();
    private EditText mTagsEdit;

    {
        mStates.add("");
        mStates.add(TumblrExtras.State.PUBLISHED);
        mStates.add(TumblrExtras.State.QUEUED);
        mStates.add(TumblrExtras.State.DRAFT);
        mStates.add(TumblrExtras.State.PRIVATE);
    }

    private Spinner mHostnameSpinner;
    private Spinner mStateSpinner;

    private Post mEditPost;
    private String mHostname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogNewPostReceiver = new BlogNewPostReceiver(new Handler());
        if (getArguments() != null) {
            mEditPost = getArguments().getParcelable(KEY_POST);
            mHostname = getArguments().getString(KEY_HOSTNAME);
        }
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.fragment_post_base, null, false);

        mHostnameSpinner = (Spinner) rootView.findViewById(R.id.hostname_spinner);
        mStateSpinner = (Spinner) rootView.findViewById(R.id.state_spinner);
        mTagsEdit = (EditText) rootView.findViewById(R.id.tags_et);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null && user.blogs != null) {
            for (Blog blog : user.blogs) {
                mHostnames.add(blog.name);
            }
        }

        if (mHostnames.isEmpty()) {
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
        }

        ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mHostnames);
        namesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHostnameSpinner.setAdapter(namesAdapter);
        if (!mHostnames.isEmpty())
            mStateSpinner.setSelection(0);

        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mStates);
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStateSpinner.setAdapter(statesAdapter);
        mStateSpinner.setSelection(0);

        rootView.addView(createView(inflater));

        if (isInEditMode()) {
            mHostnameSpinner.setVisibility(View.GONE);
            rootView.findViewById(R.id.tumblr_hostname).setVisibility(View.GONE);
            switch (mEditPost.getState()) {
                case TumblrExtras.State.PUBLISHED:
                    mStateSpinner.setSelection(1);
                    break;

                case TumblrExtras.State.QUEUED:
                    mStateSpinner.setSelection(2);
                    break;

                case TumblrExtras.State.DRAFT:
                    mStateSpinner.setSelection(3);
                    break;

                case TumblrExtras.State.PRIVATE:
                    mStateSpinner.setSelection(4);
                    break;
            }

            if (mEditPost.getTags() != null && mEditPost.getTags().length > 0) {
                StringBuilder b = new StringBuilder();
                for (String tag : mEditPost.getTags()) {
                    b.append(tag).append(", ");
                }
                mTagsEdit.setText(b.toString());
            }

            onPostInit(mEditPost);
        }

        return rootView;
    }

    protected Post getEditPost() {
        return mEditPost;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogNewPostReceiver.attach(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mBlogNewPostReceiver.detach();
    }

    @Override
    protected int getApiReference() {
        return R.string.link_posting;
    }


    protected String getHostname() {
        if(!TextUtils.isEmpty(mHostname))
            return mHostname;

        if (mHostnames.isEmpty())
            return null;
        else
            return mHostnames.get(mHostnameSpinner.getSelectedItemPosition());
    }


    @Override
    protected void onRun() {
        if (mHostnames.isEmpty()) {
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
        } else {
            String state = mStateSpinner.getSelectedItemPosition() == 0 ? null : mStates.get(mStateSpinner.getSelectedItemPosition());
            PostCreator creator = onPost();
            if (creator != null) {
                if (creator.valid()) {
                    if (!TextUtils.isEmpty(mTagsEdit.getText().toString()))
                        creator.setTags(mTagsEdit.getText().toString());
                    //noinspection ResourceType
                    creator.setPostState(state);
                    if(isInEditMode())
                        getActivity().startService(TumblrService.editPost(getActivity(), mBlogNewPostReceiver, getHostname(), creator));
                    else
                        getActivity().startService(TumblrService.newPost(getActivity(), mBlogNewPostReceiver, getHostname(), creator));
                    switch (creator.getPostType()) {
                        case TumblrExtras.Post.VIDEO:
                        case TumblrExtras.Post.PHOTO:
                        case TumblrExtras.Post.AUDIO:
                            SimpleDialog.show(false, true, false, getFragmentManager());
                            break;
                        default:
                            showDialog();
                    }
                } else {
                    Toast.makeText(getContext(), creator.getInvalidationString(getContext()), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    protected boolean isInEditMode() {
        return mEditPost != null;
    }

    protected abstract View createView(LayoutInflater inflater);

    protected abstract PostCreator onPost();

    protected abstract void onPostInit(Post p);


    @Override
    public void onBlogPostStart() {

    }

    @Override
    public void onBlogPostProgress(int value) {
        Intent intent = new Intent(SimpleDialog.DIALOG_INTENT_ACTION_PROGRESS);
        intent.putExtra(SimpleDialog.DIALOG_PROGRESS_KEY, value);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onBlogPostFail(String error) {
        hideDialog();
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogPostSuccess(long id) {
        hideDialog();
        Toast.makeText(getContext(), "OK " + id, Toast.LENGTH_SHORT).show();
    }
}
