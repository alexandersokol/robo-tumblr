package com.robotumblr.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Blog;
import com.sun40.robotumblr.model.SimpleUser;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.BlogFollowersReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 23.09.15 13:01.
 */
public class BlogFollowersFragment extends BaseFragment implements BlogFollowersReceiver.BlogFollowersListener {

    private BlogFollowersReceiver mBlogFollowersReceiver;

    private Spinner mHostNameSpinner;
    private TextView mLimitEdit;
    private TextView mOffsetEdit;
    private TextView mContentView;

    private List<String> mBlogNames = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogFollowersReceiver = new BlogFollowersReceiver(new Handler());
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_blog_followers, null, false);

        mHostNameSpinner = (Spinner) rootView.findViewById(R.id.hostname_spinner);
        mLimitEdit = (TextView) rootView.findViewById(R.id.limit_et);
        mOffsetEdit = (TextView) rootView.findViewById(R.id.offset_et);
        mContentView = (TextView) rootView.findViewById(R.id.content_tv);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null && user.blogs != null) {
            for (Blog blog : user.blogs) {
                mBlogNames.add(blog.name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mBlogNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHostNameSpinner.setAdapter(adapter);

        if (mBlogNames.isEmpty())
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
        else
            mHostNameSpinner.setSelection(0);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogFollowersReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBlogFollowersReceiver.detach();
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_blog_followers;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_blog_followers;
    }

    @Override
    protected void onRun() {
        if (mBlogNames.isEmpty()) {
            SimpleDialog.show(true, false, false, R.string.user_has_no_blogs, getFragmentManager());
        } else {
            String hostname = mBlogNames.get(mHostNameSpinner.getSelectedItemPosition());
            String offsetStr = mOffsetEdit.getText().toString();
            String limitStr = mLimitEdit.getText().toString();

            int limit;
            int offset;
            try {
                if (TextUtils.isEmpty(limitStr))
                    limit = -1;
                else
                    limit = Integer.parseInt(limitStr);

                if (TextUtils.isEmpty(offsetStr))
                    offset = -1;
                else
                    offset = Integer.parseInt(offsetStr);
            } catch (Exception ex) {
                Toast.makeText(getContext(), "Numbers input only allowed", Toast.LENGTH_SHORT).show();
                return;
            }

            getActivity().startService(TumblrService.getBlogFollowers(getActivity(), mBlogFollowersReceiver, hostname, limit, offset));
            SimpleDialog.show(false, false, false, getFragmentManager());
        }
    }

    @Override
    public void onBlogFollowersFail(String error) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogFollowersSuccess(List<SimpleUser> users, int totalFollowers, int limit, int offset) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        String separator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        builder.append("total followers: ").append(totalFollowers).append(separator);
        builder.append("limit: ").append(limit).append(separator);
        builder.append("offset: ").append(offset).append(separator);
        for (SimpleUser user : users) {
            builder.append(user.toString()).append(separator).append(separator);
        }
        mContentView.setText(builder.toString());
    }
}
