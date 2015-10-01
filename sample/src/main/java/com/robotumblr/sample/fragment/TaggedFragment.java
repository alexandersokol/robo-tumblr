package com.robotumblr.sample.fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.receiver.TaggedReceiver;

import java.util.List;

/**
 * Created by Alexander Sokol
 * on 01.10.15 10:30.
 */
public class TaggedFragment extends BaseFragment implements TaggedReceiver.TaggedListener {

    private TaggedReceiver mTaggedReceiver;

    private EditText mTagEdit;
    private EditText mBeforeEdit;
    private EditText mLimitEdit;
    private TextView mContentText;

    @Override
    public void onResume() {
        super.onResume();
        if (mTaggedReceiver == null)
            mTaggedReceiver = new TaggedReceiver(new Handler());
        mTaggedReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTaggedReceiver.detach();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_tagged, null);

        mTagEdit = (EditText) rootView.findViewById(R.id.tag_et);
        mBeforeEdit = (EditText) rootView.findViewById(R.id.timestamp_et);
        mLimitEdit = (EditText) rootView.findViewById(R.id.limit_et);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_tagged;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_tagged_method;
    }

    @Override
    protected void onRun() {

        String tag = mTagEdit.getText().toString();
        if (TextUtils.isEmpty(tag)) {
            Toast.makeText(getContext(), "Tag is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int limit;
        long before;

        try {
            String limitString = mLimitEdit.getText().toString();
            String beforeString = mBeforeEdit.getText().toString();

            if (TextUtils.isEmpty(limitString))
                limit = -1;
            else
                limit = Integer.parseInt(limitString);

            if (TextUtils.isEmpty(beforeString))
                before = -1;
            else
                before = Long.parseLong(beforeString);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Numbers input only allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        getActivity().startService(QueryService.tagged(getActivity(), mTaggedReceiver, tag, before, limit, TumblrExtras.Filter.TEXT));
        showDialog();
    }

    @Override
    public void onTaggedFail(String error) {
        onFail(error);
    }

    @Override
    public void onTaggedSuccess(List<Post> posts, String tag, long before, int limit, String filter) {
        hideDialog();
        StringBuilder b = new StringBuilder();
        b.append("Tag: ").append(tag).append(separator);
        b.append("Before: ").append(before).append(separator);
        b.append("Limit: ").append(limit).append(separator).append(separator).append(separator);

        for (Post post : posts) {
            b.append(post.toString()).append(separator).append(separator);
        }

        mContentText.setText(b.toString());
    }
}
