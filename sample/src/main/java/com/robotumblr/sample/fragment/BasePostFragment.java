package com.robotumblr.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.Blog;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogNewPostReceiver = new BlogNewPostReceiver(new Handler());
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

        return rootView;
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
                    creator.setPostState(state);
                    getActivity().startService(QueryService.newPost(getActivity(), mBlogNewPostReceiver, getHostname(), creator));
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

    protected abstract View createView(LayoutInflater inflater);

    protected abstract PostCreator onPost();


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
