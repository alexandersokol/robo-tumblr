package com.robotumblr.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.TumblrService;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.model.User;
import com.sun40.robotumblr.receiver.BlogAvatarReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Sokol
 * on 18.09.15 15:34.
 */
public class BlogAvatarFragment extends BaseFragment implements BlogAvatarReceiver.BlogAvatarListener {

    private BlogAvatarReceiver mBlogAvatarReceiver;

    private final String[] mSizeArray = new String[]{
            "Undefined",
            "16",
            "24",
            "30",
            "40",
            "48",
            "64",
            "96",
            "128",
            "512"
    };

    private final Map<String, Integer> mSizeMap = new HashMap<>();

    {
        mSizeMap.put(mSizeArray[0], TumblrExtras.Size.SIZE_UNDEFINED);
        mSizeMap.put(mSizeArray[1], TumblrExtras.Size.SIZE_16);
        mSizeMap.put(mSizeArray[2], TumblrExtras.Size.SIZE_24);
        mSizeMap.put(mSizeArray[3], TumblrExtras.Size.SIZE_30);
        mSizeMap.put(mSizeArray[4], TumblrExtras.Size.SIZE_40);
        mSizeMap.put(mSizeArray[5], TumblrExtras.Size.SIZE_48);
        mSizeMap.put(mSizeArray[6], TumblrExtras.Size.SIZE_64);
        mSizeMap.put(mSizeArray[7], TumblrExtras.Size.SIZE_96);
        mSizeMap.put(mSizeArray[8], TumblrExtras.Size.SIZE_128);
        mSizeMap.put(mSizeArray[9], TumblrExtras.Size.SIZE_512);
    }

    private Spinner mSpinner;
    private EditText mHostNameEdit;
    private TextView mSizeView;
    private TextView mLinkView;
    private ImageView mAvatarView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBlogAvatarReceiver = new BlogAvatarReceiver(new Handler());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_blog_avatar, null, false);
        mSpinner = (Spinner) rootView.findViewById(R.id.spinner_type);
        mHostNameEdit = (EditText) rootView.findViewById(R.id.hostname_spinner);
        mSizeView = (TextView) rootView.findViewById(R.id.size_tv);
        mLinkView = (TextView) rootView.findViewById(R.id.link_tv);
        mAvatarView = (ImageView) rootView.findViewById(R.id.avatar_iv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mSizeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(0);

        User user = StorageUtils.getCurrentUser(getContext());
        if (user != null) {
            mHostNameEdit.setText(user.name);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogAvatarReceiver.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBlogAvatarReceiver.detach();
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_blog_avatar;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_blog_avatar;
    }

    @Override
    protected void onRun() {
        int size = mSizeMap.get(mSizeArray[mSpinner.getSelectedItemPosition()]);
        String hostname = mHostNameEdit.getText().toString();
        if (TextUtils.isEmpty(hostname)) {
            Toast.makeText(getContext(), "Enter Hostname", Toast.LENGTH_SHORT).show();
            return;
        }
        getActivity().startService(TumblrService.blogAvatar(getContext(), mBlogAvatarReceiver, hostname, size));
        mSizeView.setText("");
        mLinkView.setText("");
        SimpleDialog.show(false, false, false, getFragmentManager());
    }

    @Override
    public void onBlogAvatarFail(String error) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        if (TextUtils.isEmpty(error))
            error = "Failed";
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlogAvatarSuccess(String avatarUrl, int size) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        mSizeView.setText(size + "x" + size);
        mLinkView.setText(avatarUrl);
    }
}
