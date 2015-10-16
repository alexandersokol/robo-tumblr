package com.robotumblr.sample.fragment;

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
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.receiver.UserDashboardReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 30.09.15 12:03.
 */
public class UserDashboardFragment extends BaseFragment implements UserDashboardReceiver.UserDashboardListener {

    private UserDashboardReceiver mUserDashboardReceiver;

    private Spinner mTypeSpinner;
    private CheckBox mNotesInfoBox;
    private CheckBox mReblogInfoBox;
    private EditText mLimitEdit;
    private EditText mOffsetEdit;
    private TextView mContentText;

    private final List<String> mPostTypes = new ArrayList<>();

    {
        mPostTypes.add("Any");
        mPostTypes.add(TumblrExtras.Post.TEXT);
        mPostTypes.add(TumblrExtras.Post.QUOTE);
        mPostTypes.add(TumblrExtras.Post.LINK);
        mPostTypes.add(TumblrExtras.Post.ANSWER);
        mPostTypes.add(TumblrExtras.Post.VIDEO);
        mPostTypes.add(TumblrExtras.Post.AUDIO);
        mPostTypes.add(TumblrExtras.Post.PHOTO);
        mPostTypes.add(TumblrExtras.Post.CHAT);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserDashboardReceiver == null)
            mUserDashboardReceiver = new UserDashboardReceiver(new Handler());
        mUserDashboardReceiver.attach(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        mUserDashboardReceiver.detach();
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_user_dashboard, null, false);

        mTypeSpinner = (Spinner) rootView.findViewById(R.id.spinner_type);
        mNotesInfoBox = (CheckBox) rootView.findViewById(R.id.notes_info_cb);
        mReblogInfoBox = (CheckBox) rootView.findViewById(R.id.reblog_info_cb);
        mLimitEdit = (EditText) rootView.findViewById(R.id.limit_et);
        mOffsetEdit = (EditText) rootView.findViewById(R.id.offset_et);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mPostTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);
        mTypeSpinner.setSelection(0);

        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_user_dashboard;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_blog_info;
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
        String type = mTypeSpinner.getSelectedItemPosition() == 0 ? null : mPostTypes.get(mTypeSpinner.getSelectedItemPosition());
        //noinspection ResourceType
        getActivity().startService(TumblrService.userDashbloard(getActivity(), mUserDashboardReceiver, limit, offset, type, -1, mReblogInfoBox.isChecked(), mNotesInfoBox.isChecked()));
        showDialog();
    }

    @Override
    public void onUserDashboardFail(String error) {
        onFail(error);
    }

    @Override
    public void onUserDashboardSuccess(List<Post> posts, int limit, int offset, String type, long sinceId, boolean reblogInfo, boolean notesInfo) {
        hideDialog();
        String separator = System.getProperty("line.separator");
        StringBuilder b = new StringBuilder();
        b.append("post count: ").append(posts.size()).append(separator);
        b.append("limit: ").append(limit).append(separator);
        b.append("offset: ").append(offset).append(separator);
        b.append("type: ").append(type).append(separator);
        b.append("reblog info: ").append(reblogInfo).append(separator);
        b.append("notes info: ").append(notesInfo).append(separator).append(separator);

        for (Post post : posts) {
            b.append(post.toString()).append(separator);
        }

        mContentText.setText(b.toString());
    }
}
