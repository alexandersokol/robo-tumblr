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
import com.sun40.robotumblr.receiver.BlogDraftReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 24.09.15 15:11.
 */
public class BlogDraftFragment extends BaseFragment implements BlogDraftReceiver.BlogDraftListener {

    private BlogDraftReceiver mBlogDraftReceiver;

    private Spinner mBeforeSpinner;
    private Spinner mHostnameSpinner;
    private Spinner mFilterTypeSpinner;
    private EditText mLimitEdit;
    private TextView mContentText;

    private List<Long> mIdList = new ArrayList<>();
    private List<String> mHostnames = new ArrayList<>();
    private List<String> mFilterTypes = new ArrayList<>();

    {
        mIdList.add(0L);
        mFilterTypes.add("");
        mFilterTypes.add(TumblrExtras.Filter.RAW);
        mFilterTypes.add(TumblrExtras.Filter.TEXT);
        mFilterTypes.add(TumblrExtras.Filter.HTML);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogDraftReceiver = new BlogDraftReceiver(new Handler());

    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_blog_drafts, null, false);

        mHostnameSpinner = (Spinner) rootView.findViewById(R.id.hostname_spinner);
        mLimitEdit = (EditText) rootView.findViewById(R.id.limit_et);
        mBeforeSpinner = (Spinner) rootView.findViewById(R.id.before_spinner);
        mFilterTypeSpinner = (Spinner) rootView.findViewById(R.id.spinner_filter_type);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null && user.blogs != null) {
            for (Blog blog : user.blogs)
                mHostnames.add(blog.name);
        } else {
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
        }

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mHostnames);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHostnameSpinner.setAdapter(nameAdapter);
        if (!mHostnames.isEmpty())
            mHostnameSpinner.setSelection(0);

        ArrayAdapter<Long> idAdapter = new ArrayAdapter<Long>(getContext(), android.R.layout.simple_spinner_item, mIdList);
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBeforeSpinner.setAdapter(idAdapter);
        mBeforeSpinner.setSelection(0);

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mFilterTypes);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterTypeSpinner.setAdapter(filterAdapter);
        mFilterTypeSpinner.setSelection(0);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogDraftReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBlogDraftReceiver.detach();
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_blog_drafts;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_blog_drafts;
    }

    @Override
    protected void onRun() {
        if (mHostnames.isEmpty()) {
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
            return;
        }

        int limit;

        try {
            String limitString = mLimitEdit.getText().toString();

            if (TextUtils.isEmpty(limitString))
                limit = -1;
            else
                limit = Integer.parseInt(limitString);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Numbers input only allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        String hostname = mHostnames.get(mHostnameSpinner.getSelectedItemPosition());
        long id = mIdList.get(mBeforeSpinner.getSelectedItemPosition());
        String filter = mFilterTypeSpinner.getSelectedItemPosition() == 0 ? null : mFilterTypes.get(mFilterTypeSpinner.getSelectedItemPosition());
        getActivity().startService(QueryService.blogDrafts(getActivity(), mBlogDraftReceiver, hostname, limit, id, filter));
        showDialog();
    }

    @Override
    public void onBlogDraftFail(String error) {
        hideDialog();
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogDraftSuccess(List<Post> posts, long beforeId, String filter) {
        hideDialog();
        boolean addIds = false;
        if (mIdList.size() == 1) {
            mIdList.clear();
            mIdList.add(0L);
            addIds = true;
        }
        String separator = System.getProperty("line.separator");
        StringBuilder b = new StringBuilder();
        b.append("Before Id: ").append(beforeId).append(separator);
        b.append("Filter: ").append(filter).append(separator).append(separator);
        for (Post post : posts) {
            b.append(post.toString()).append(separator).append(separator);
            if (addIds)
                mIdList.add(post.getId());
        }
        mContentText.setText(b.toString());
        if (addIds) {
            ArrayAdapter<Long> idAdapter = new ArrayAdapter<Long>(getContext(), android.R.layout.simple_spinner_item, mIdList);
            idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mBeforeSpinner.setAdapter(idAdapter);
            mBeforeSpinner.setSelection(0);
        }
    }
}
