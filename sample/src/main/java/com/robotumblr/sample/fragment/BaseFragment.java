package com.robotumblr.sample.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.dialog.SimpleDialog;
import com.sun40.robotumblr.OnTokenInvalidatedListener;

/**
 * Created by Alexander Sokol
 * on 18.09.15 15:33.
 */
abstract class BaseFragment extends Fragment implements OnTokenInvalidatedListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        FrameLayout contentLayout = (FrameLayout) rootView.findViewById(R.id.content_layout);
        contentLayout.addView(onCreateView(inflater));
        rootView.findViewById(R.id.btn_run).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                onRun();
            }
        });
        rootView.findViewById(R.id.btn_run).setVisibility(isRunBtnVisible() ? View.VISIBLE : View.GONE);
        TextView titleView = (TextView) rootView.findViewById(R.id.title_tv);
        titleView.setText(getToolbarText());
        rootView.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment.this.getActivity().onBackPressed();
            }
        });
        rootView.findViewById(R.id.link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = BaseFragment.this.getString(getApiReference());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                BaseFragment.this.startActivity(intent);
            }
        });
        return rootView;
    }


    protected boolean isRunBtnVisible() {
        return true;
    }

    protected void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onUserNotAuthorized() {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Toast.makeText(getContext(), "Not Authorized", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenInvalidated() {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
        Toast.makeText(getContext(), "Token invalidated", Toast.LENGTH_SHORT).show();
    }

    protected void showDialog() {
        SimpleDialog.show(false, false, false, getFragmentManager());
    }

    protected void hideDialog() {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SimpleDialog.DIALOG_INTENT_ACTION_CANCEL));
    }

    protected abstract View onCreateView(LayoutInflater inflater);

    @StringRes
    protected abstract int getToolbarText();

    @StringRes
    protected abstract int getApiReference();

    protected abstract void onRun();
}
