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
import com.sun40.robotumblr.model.SimpleBlog;
import com.sun40.robotumblr.receiver.UserFollowingBlogsReceiver;

import java.util.List;

/**
 * Created by Alexander Sokol
 * on 30.09.15 18:26.
 */
public class UserFollowingsFragment extends BaseFragment implements UserFollowingBlogsReceiver.UserFollowingBlogsListener {

    private UserFollowingBlogsReceiver mFollowingReceiver;

    private EditText mLimitEdit;
    private EditText mOffsetEdit;
    private TextView mContentText;

    @Override
    public void onResume() {
        super.onResume();
        if (mFollowingReceiver == null)
            mFollowingReceiver = new UserFollowingBlogsReceiver(new Handler());
        mFollowingReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFollowingReceiver.detach();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_user_followings, null);

        mLimitEdit = (EditText) rootView.findViewById(R.id.limit_et);
        mOffsetEdit = (EditText) rootView.findViewById(R.id.offset_et);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_user_followings;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_user_methods;
    }

    @Override
    protected void onRun() {
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

        getActivity().startService(QueryService.userFollowingBlogs(getActivity(), mFollowingReceiver, limit, offset));
        showDialog();
    }

    @Override
    public void onUserFollowingBlogsFail(String error) {
        onFail(error);
    }

    @Override
    public void onUserFollowingBlogsSuccess(List<SimpleBlog> blogs, int total, int limit, int offset) {
        hideDialog();
        StringBuilder b = new StringBuilder();
        b.append("total: ").append(total).append(separator);
        b.append("limit: ").append(limit).append(separator);
        b.append("offset: ").append(offset).append(separator).append(separator);

        for (SimpleBlog blog : blogs) {
            b.append(blog.toString()).append(separator).append(separator);
        }

        mContentText.setText(b.toString());
    }
}
