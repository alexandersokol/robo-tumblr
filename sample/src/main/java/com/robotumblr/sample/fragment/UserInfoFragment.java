package com.robotumblr.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robotumblr.sample.R;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.UserInfoReceiver;

/**
 * Created by Alexander Sokol
 * on 29.09.15 17:58.
 */
public class UserInfoFragment extends BaseFragment implements UserInfoReceiver.UserInfoListener {

    private UserInfoReceiver mUserInfoReceiver;

    private TextView mContentText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserInfoReceiver = new UserInfoReceiver(new Handler());
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserInfoReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mUserInfoReceiver.detach();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, null, false);
        mContentText = (TextView) rootView.findViewById(R.id.content_tv);
        onRun();
        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_user_info;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_user_methods;
    }

    @Override
    protected void onRun() {
        getActivity().startService(TumblrService.userInfo(getActivity(), mUserInfoReceiver));
        showDialog();
    }

    @Override
    public void onUserInfoFail(String error) {
        onFail(error);
    }

    @Override
    public void onUserInfoSuccess(User user) {
        hideDialog();
        if (user != null) {
            mContentText.setText(user.toString());
            StorageUtils.setCurrentUser(getContext(), user);
        }
    }
}
