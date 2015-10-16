package com.robotumblr.sample.fragment;

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
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.receiver.UserLikesReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 30.09.15 17:22.
 */
public class UserLikesFragment extends BaseFragment implements UserLikesReceiver.UserLikesListener {

    private UserLikesReceiver mUserLikesReceiver;

    private Spinner mOffsetSpinner;
    private EditText mLimitEdit;
    private EditText mOffsetEdit;
    private TextView mContentText;

    @Override
    public void onResume() {
        super.onResume();
        if (mUserLikesReceiver == null)
            mUserLikesReceiver = new UserLikesReceiver(new Handler());
        mUserLikesReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mUserLikesReceiver.detach();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_user_likes, null);

        mOffsetSpinner = (Spinner) rootView.findViewById(R.id.spinner_type);
        mLimitEdit = (EditText) rootView.findViewById(R.id.limit_et);
        mOffsetEdit = (EditText) rootView.findViewById(R.id.offset_et);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        final List<String> offsetList = new ArrayList<>();
        offsetList.add("Offset");
        offsetList.add("Before");
        offsetList.add("After");

        ArrayAdapter<String> offsetAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, offsetList);
        offsetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOffsetSpinner.setAdapter(offsetAdapter);
        mOffsetSpinner.setSelection(0);

        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_user_likes;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_user_methods;
    }

    @Override
    protected void onRun() {

        String offsetStr = mOffsetEdit.getText().toString();
        if (mOffsetSpinner.getSelectedItemPosition() != 0) {
            try {
                long tmp = Long.parseLong(offsetStr);
            } catch (Exception ex) {
                Toast.makeText(getContext(), "Numbers input only allowed", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String limitStr = mLimitEdit.getText().toString();
        if (TextUtils.isEmpty(limitStr))
            limitStr = "20";

        int limit = Integer.parseInt(limitStr);

        if (mOffsetSpinner.getSelectedItemPosition() == 0) {
            int offset = TextUtils.isEmpty(offsetStr) ? 0 : Integer.parseInt(offsetStr);
            getActivity().startService(TumblrService.userLikes(getContext(), mUserLikesReceiver, limit, offset));
        } else if (mOffsetSpinner.getSelectedItemPosition() == 1) {
            long timestamp = Long.parseLong(offsetStr);
            getActivity().startService(TumblrService.userLikes(getContext(), mUserLikesReceiver, limit, -1, timestamp, -1));
        } else {
            long timestamp = Long.parseLong(offsetStr);
            getActivity().startService(TumblrService.userLikes(getContext(), mUserLikesReceiver, limit, -1, -1, timestamp));
        }
        SimpleDialog.show(false, false, false, getFragmentManager());
    }

    @Override
    public void onUserLikesFail(String error) {
        onFail(error);
    }

    @Override
    public void onUserLikesSuccess(List<Post> posts, int total, int limit, int offset, long before, long after) {
        hideDialog();
        StringBuilder b = new StringBuilder();
        b.append("total: ").append(total).append(separator);
        b.append("limit: ").append(limit).append(separator);
        if (offset >= 0)
            b.append("offset: ").append(offset).append(separator);
        if (before >= 0)
            b.append("before: ").append(before).append(separator);
        if (after >= 0)
            b.append("after: ").append(after).append(separator);

        b.append(separator).append(separator);

        for (Post post : posts) {
            b.append(post.toString()).append(separator);
        }

        mContentText.setText(b.toString());
    }
}
