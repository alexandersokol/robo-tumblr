package com.robotumblr.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.BlogInfoReceiver;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.Blog;


/**
 * Created by Alexander Sokol
 * on 18.09.15 15:33.
 */
public class BlogInfoFragment extends BaseFragment implements BlogInfoReceiver.OnBlogInfoListener {

    private EditText mHostNameEdit;
    private BlogInfoReceiver mBlogInfoReceiver;
    private TextView mContentText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogInfoReceiver = new BlogInfoReceiver(new Handler());
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_blog_info, null, false);
        mHostNameEdit = (EditText) rootView.findViewById(R.id.hostname_spinner);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null) {
            mHostNameEdit.setText(user.name);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogInfoReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBlogInfoReceiver.detach();
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_blog_info;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_blog_info;
    }

    @Override
    protected void onRun() {
        String hostname = mHostNameEdit.getText().toString();
        getActivity().startService(QueryService.blogInfo(getActivity(), mBlogInfoReceiver, hostname));
        SimpleDialog.show(false, false, false, getFragmentManager());
    }

    @Override
    public void onBlogInfoFail(String error) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogInfoSuccess(Blog blog) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        mContentText.setText(blog.toString());
    }
}
