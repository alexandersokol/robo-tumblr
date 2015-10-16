package com.robotumblr.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
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
 * on 23.09.15 13:55.
 */
public class BlogPostsFragment extends BaseFragment implements BlogPostsReceiver.BlogPostsListener {

    private BlogPostsReceiver mBlogPostsReceiver;

    private EditText mHostNameEdit;
    private EditText mLimitEdit;
    private EditText mOffsetEdit;
    private CheckBox mReblogInfoBox;
    private CheckBox mNotesInfoBox;
    private Spinner mPostTypeSpinner;
    private EditText mTagEdit;
    private Spinner mFilterTypeSpinner;
    private TextView mContentText;

    private final List<String> mPostTypes = new ArrayList<>();
    private final List<String> mFilterTypes = new ArrayList<>();

    {
        mPostTypes.add("");
        mPostTypes.add(TumblrExtras.Post.TEXT);
        mPostTypes.add(TumblrExtras.Post.QUOTE);
        mPostTypes.add(TumblrExtras.Post.LINK);
        mPostTypes.add(TumblrExtras.Post.ANSWER);
        mPostTypes.add(TumblrExtras.Post.VIDEO);
        mPostTypes.add(TumblrExtras.Post.AUDIO);
        mPostTypes.add(TumblrExtras.Post.PHOTO);
        mPostTypes.add(TumblrExtras.Post.CHAT);

        mFilterTypes.add("");
        mFilterTypes.add(TumblrExtras.Filter.RAW);
        mFilterTypes.add(TumblrExtras.Filter.TEXT);
        mFilterTypes.add(TumblrExtras.Filter.HTML);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogPostsReceiver = new BlogPostsReceiver(new Handler());
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_blog_posts, null, false);

        mHostNameEdit = (EditText) rootView.findViewById(R.id.hostname_spinner);
        mLimitEdit = (EditText) rootView.findViewById(R.id.limit_et);
        mOffsetEdit = (EditText) rootView.findViewById(R.id.offset_et);
        mReblogInfoBox = (CheckBox) rootView.findViewById(R.id.reblog_info_cb);
        mNotesInfoBox = (CheckBox) rootView.findViewById(R.id.notes_info_cb);
        mPostTypeSpinner = (Spinner) rootView.findViewById(R.id.spinner_type);
        mTagEdit = (EditText) rootView.findViewById(R.id.tag_et);
        mFilterTypeSpinner = (Spinner) rootView.findViewById(R.id.spinner_filter_type);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null && user.name != null)
            mHostNameEdit.setText(user.name);

        ArrayAdapter<String> postAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mPostTypes);
        postAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPostTypeSpinner.setAdapter(postAdapter);
        mPostTypeSpinner.setSelection(0);

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mFilterTypes);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterTypeSpinner.setAdapter(filterAdapter);
        mFilterTypeSpinner.setSelection(0);

        return rootView;
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
        return R.string.title_blog_posts;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_blog_posts;
    }

    @Override
    protected void onRun() {
        String hostname = mHostNameEdit.getText().toString();
        if (hostname.isEmpty()) {
            Toast.makeText(getContext(), "Enter hostname", Toast.LENGTH_SHORT).show();
            return;
        }

        int limit;
        int offset;

        try {
            String limitString = mLimitEdit.getText().toString();
            String offsetString = mOffsetEdit.getText().toString();

            if (TextUtils.isEmpty(limitString))
                limit = -1;
            else
                limit = Integer.parseInt(limitString);

            if (TextUtils.isEmpty(offsetString))
                offset = -1;
            else
                offset = Integer.parseInt(offsetString);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Numbers input only allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        String tag = TextUtils.isEmpty(mTagEdit.getText().toString()) ? null : mTagEdit.getText().toString();
        String type = mPostTypeSpinner.getSelectedItemPosition() == 0 ? null : mPostTypes.get(mPostTypeSpinner.getSelectedItemPosition());
        String filter = mFilterTypeSpinner.getSelectedItemPosition() == 0 ? null : mFilterTypes.get(mFilterTypeSpinner.getSelectedItemPosition());
        getActivity().startService(TumblrService.blogPosts(getActivity(), mBlogPostsReceiver, hostname, type, tag, limit, offset, mReblogInfoBox.isChecked(), mNotesInfoBox.isChecked(), filter));
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
        hideDialog();
        String separator = System.getProperty("line.separator");
        StringBuilder b = new StringBuilder();
        b.append(blog.toString()).append(separator).append(separator);
        b.append(separator).append("total posts: ").append(total).append(separator);
        b.append("limit: ").append(limit).append(separator);
        b.append("offset: ").append(offset).append(separator);
        if (type != null)
            b.append("type: ").append(type).append(separator);
        if (tag != null)
            b.append("tag: ").append(tag).append(separator);
        if (filter != null)
            b.append("filter: ").append(filter).append(separator);
        b.append("reblog info: ").append(reblogInfo).append(separator);
        b.append("notes info: ").append(notesInfo).append(separator).append(separator);

        for(Post post : posts){
            b.append(post.toString()).append(separator).append(separator);
        }

        mContentText.setText(b.toString());
    }
}
