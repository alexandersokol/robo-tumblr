package com.robotumblr.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
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
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.BlogLikesReceiver;
import com.sun40.robotumblr.QueryService;
import com.sun40.robotumblr.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 21.09.15 17:57.
 */
public class BlogLikesFragment extends BaseFragment implements BlogLikesReceiver.BlogLikesListener {

    private BlogLikesReceiver mBlogLikesReceiver;

    private Spinner mOffsetSpinner;
    private EditText mLimitEdit;
    private EditText mOffsetEdit;
    private EditText mUserNameEditText;
    private TextView mContentText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogLikesReceiver = new BlogLikesReceiver(new Handler());
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_blog_likes, null, false);

        mOffsetSpinner = (Spinner) rootView.findViewById(R.id.spinner_type);
        mUserNameEditText = (EditText) rootView.findViewById(R.id.hostname_spinner);
        mLimitEdit = (EditText) rootView.findViewById(R.id.limit_et);
        mOffsetEdit = (EditText) rootView.findViewById(R.id.offset_et);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        final List<String> offsetList = new ArrayList<>();
        offsetList.add("Offset");
        offsetList.add("Before");
        offsetList.add("After");

        User user = StorageUtils.getCurrentUser(getContext());
        if(user != null && user.name != null){
            mUserNameEditText.setText(user.name);
        }



        ArrayAdapter<String> offsetAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, offsetList);
        offsetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOffsetSpinner.setAdapter(offsetAdapter);
        mOffsetSpinner.setSelection(0);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mBlogLikesReceiver.attach(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mBlogLikesReceiver.detach();
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_blog_likes;
    }

    @Override
    protected int getApiReference() {
        return  R.string.link_blog_likes;
    }

    @Override
    protected void onRun() {
        String hostname = mUserNameEditText.getText().toString();
        if (TextUtils.isEmpty(hostname)) {
            Toast.makeText(getContext(), "Enter Hostname", Toast.LENGTH_SHORT).show();
            return;
        }

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
            getActivity().startService(QueryService.blogLikes(getContext(), mBlogLikesReceiver, hostname, limit, offset));
        } else if (mOffsetSpinner.getSelectedItemPosition() == 1) {
            long timestamp = Long.parseLong(offsetStr);
            getActivity().startService(QueryService.blogLikesBefore(getContext(), mBlogLikesReceiver, hostname, limit, timestamp));
        } else {
            long timestamp = Long.parseLong(offsetStr);
            getActivity().startService(QueryService.blogLikesAfter(getContext(), mBlogLikesReceiver, hostname, limit, timestamp));
        }
        SimpleDialog.show(false, false, false, getFragmentManager());
    }

    @Override
    public void onBlogLikesFail(String error) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogLikesSuccess(List<Post> posts, int likedCount, int limit, int offset, long timestamp) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("likedCount=").append(likedCount).append(System.getProperty("line.separator"));
        stringBuilder.append("limit=").append(limit).append(System.getProperty("line.separator"));
        if (offset > 0)
            stringBuilder.append("offset=").append(offset).append(System.getProperty("line.separator"));
        if (timestamp > 0)
            stringBuilder.append("timestamp=").append(timestamp).append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));
        for (Post post : posts) {
            stringBuilder.append(post.toString());
            stringBuilder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        mContentText.setText(stringBuilder.toString());
    }
}
