package com.robotumblr.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.BlogQueueReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 24.09.15 12:48.
 */
public class BlogQueueFragment extends BaseFragment implements BlogQueueReceiver.BlogQueueListener {

    private BlogQueueReceiver mBlogQueueReceiver;

    private Spinner mHostnameSpinner;
    private EditText mLimitEdit;
    private EditText mOffsetEdit;
    private Spinner mFilterTypeSpinner;
    private TextView mContentText;

    private List<String> mHostnames = new ArrayList<>();
    private List<String> mFilterTypes = new ArrayList<>();

    {
        mFilterTypes.add("");
        mFilterTypes.add(TumblrExtras.Filter.RAW);
        mFilterTypes.add(TumblrExtras.Filter.TEXT);
        mFilterTypes.add(TumblrExtras.Filter.HTML);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogQueueReceiver = new BlogQueueReceiver(new Handler());
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_blog_queue, null, false);

        mHostnameSpinner = (Spinner) rootView.findViewById(R.id.hostname_spinner);
        mLimitEdit = (EditText) rootView.findViewById(R.id.limit_et);
        mOffsetEdit = (EditText) rootView.findViewById(R.id.offset_et);
        mFilterTypeSpinner = (Spinner) rootView.findViewById(R.id.spinner_filter_type);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null && user.blogs != null) {
            for (Blog blog : user.blogs)
                mHostnames.add(blog.name);
        }

        if(mHostnames.isEmpty()){
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
        }

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mHostnames);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHostnameSpinner.setAdapter(nameAdapter);
        if (!mHostnames.isEmpty())
            mHostnameSpinner.setSelection(0);

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mFilterTypes);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterTypeSpinner.setAdapter(filterAdapter);
        mFilterTypeSpinner.setSelection(0);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mBlogQueueReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBlogQueueReceiver.detach();
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_blog_queue;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_blog_queue;
    }

    @Override
    protected void onRun() {
        if (mHostnames.isEmpty()) {
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
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

        String hostname = mHostnames.get(mHostnameSpinner.getSelectedItemPosition());
        String filter = mFilterTypeSpinner.getSelectedItemPosition() == 0 ? null : mFilterTypes.get(mFilterTypeSpinner.getSelectedItemPosition());
        getActivity().startService(QueryService.blogPostQueue(getActivity(), mBlogQueueReceiver, hostname, limit, offset, filter));
        showDialog();
    }

    @Override
    public void onBlogQueuedFail(String error) {
        hideDialog();
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogQueuedSuccess(List<Post> posts, int offset, int limit, String filter) {
        hideDialog();
        String separator = System.getProperty("line.separator");
        StringBuilder b = new StringBuilder();
        b.append("offset: ").append(offset).append(separator);
        b.append("limit: ").append(limit).append(separator);
        b.append("filter: ").append(filter).append(separator).append(separator);

        for(Post post : posts){
            b.append(post.toString()).append(separator);
        }
        mContentText.setText(b.toString());
    }
}
