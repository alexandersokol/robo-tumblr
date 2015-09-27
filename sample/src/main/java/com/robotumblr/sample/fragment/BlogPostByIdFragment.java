package com.robotumblr.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.receiver.BlogPostByIdReceiver;
import com.sun40.robotumblr.receiver.BlogPostsReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 23.09.15 18:12.
 */
public class BlogPostByIdFragment extends BaseFragment implements BlogPostsReceiver.BlogPostsListener, BlogPostByIdReceiver.BlogPostByIdListener {


    private BlogPostsReceiver mBlogPostsReceiver;
    private BlogPostByIdReceiver mBlogPostByIdReceiver;

    private List<Long> mIdList = new ArrayList<>();
    private Spinner mIdSpinner;
    private TextView mContentText;
    private CheckBox mReblogInfoBox;
    private CheckBox mNotesInfoBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogPostsReceiver = new BlogPostsReceiver(new Handler());
        mBlogPostByIdReceiver = new BlogPostByIdReceiver(new Handler());
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_by_id, null, false);
        mIdSpinner = (Spinner) rootView.findViewById(R.id.spinner);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);
        mReblogInfoBox = (CheckBox) rootView.findViewById(R.id.reblog_info_cb);
        mNotesInfoBox = (CheckBox) rootView.findViewById(R.id.notes_info_cb);

        SimpleDialog.show(false, false, false, getFragmentManager());
        getActivity().startService(QueryService.blogPosts(getActivity(), mBlogPostsReceiver, getString(R.string.developers_hostname), 20, 0));
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mBlogPostsReceiver.attach(this);
        mBlogPostByIdReceiver.attach(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mBlogPostsReceiver.detach();
        mBlogPostByIdReceiver.detach();
    }

    @Override
    protected int getToolbarText() {
        return R.string.blog_post_by_id;
    }


    @Override
    protected int getApiReference() {
        return R.string.link_blog_posts;
    }


    @Override
    protected void onRun() {
        if (mIdList.isEmpty()) {
            Toast.makeText(getContext(), "Id list is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = mIdList.get(mIdSpinner.getSelectedItemPosition());
        getActivity().startService(QueryService.blogPostById(getActivity(), mBlogPostByIdReceiver, getString(R.string.developers_hostname), id, mReblogInfoBox.isChecked(), mNotesInfoBox.isChecked()));
        SimpleDialog.show(false, false, false, getFragmentManager());
    }

    @Override
    public void onBlogPostsFail(String error) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Toast.makeText(getContext(), "Failed to get id list", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogPostsSuccess(Blog blog, List<Post> posts, int limit, int offset, String tag, String type, boolean reblogInfo, boolean notesInfo, String filter, int total) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        mIdList.clear();
        for (Post post : posts) {
            mIdList.add(post.getId());
        }

        ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(getContext(), android.R.layout.simple_spinner_item, mIdList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIdSpinner.setAdapter(adapter);
        if (!mIdList.isEmpty())
            mIdSpinner.setSelection(0);
    }

    @Override
    public void onBlogPostByIdFail(String error) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogPostByIdSuccess(Blog blog, Post post) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        StringBuilder builder = new StringBuilder();
        builder.append(blog.toString());
        builder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        builder.append(post.toString());
        mContentText.setText(builder.toString());
    }
}
