package com.robotumblr.sample.dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robotumblr.sample.R;

/**
 * Created by Alexander Sokol
 * on 17.09.15 11:20.
 */
public class SimpleDialog extends DialogFragment {

    private static final String KEY_CANCELABLE = "CANCELABLE";
    private static final String KEY_PROGRESS_VISIBLE = "PROGRESS_VISIBLE";
    private static final String KEY_CAN_CLOSE = "CAN_CLOSE";
    private static final String KEY_TEXT = "TEXT";

    public static final String DIALOG_INTENT_ACTION_CANCEL = "simpleDialogActionCancel";
    public static final String DIALOG_INTENT_ACTION_CLOSE = "simpleDialogActionClose";
    public static final String DIALOG_INTENT_ACTION_PROGRESS = "simpleDialogActionProgress";
    public static final String DIALOG_PROGRESS_KEY = "simpleDialogProgressKey";

    private ProgressBar mProgressBar;
    private DialogActionReceiver mReceiver;

    public static void show(boolean cancelable, boolean progressVisible, boolean canClose, FragmentManager fragmentManager) {
        SimpleDialog dialog = new SimpleDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CANCELABLE, cancelable);
        bundle.putBoolean(KEY_PROGRESS_VISIBLE, progressVisible);
        bundle.putBoolean(KEY_CAN_CLOSE, canClose);
        dialog.setArguments(bundle);
        dialog.show(fragmentManager, "SimpleFragment");
    }


    public static void show(boolean cancelable, boolean progressVisible, boolean canClose, @StringRes int textRes, FragmentManager fragmentManager) {
        SimpleDialog dialog = new SimpleDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CANCELABLE, cancelable);
        bundle.putBoolean(KEY_PROGRESS_VISIBLE, progressVisible);
        bundle.putBoolean(KEY_CAN_CLOSE, canClose);
        bundle.putInt(KEY_TEXT, textRes);
        dialog.setArguments(bundle);
        dialog.show(fragmentManager, "SimpleFragment");
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mReceiver == null)
            mReceiver = new DialogActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DIALOG_INTENT_ACTION_CANCEL);
        filter.addAction(DIALOG_INTENT_ACTION_CLOSE);
        filter.addAction(DIALOG_INTENT_ACTION_PROGRESS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mReceiver != null)
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(!getArguments().getBoolean(KEY_CAN_CLOSE));
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean cancelable = getArguments().getBoolean(KEY_CANCELABLE);
        boolean progressVisible = getArguments().getBoolean(KEY_PROGRESS_VISIBLE);
        boolean canClose = getArguments().getBoolean(KEY_CAN_CLOSE);
        int textRes = getArguments().getInt(KEY_TEXT, 0);

        View rootView = inflater.inflate(R.layout.dialog_simple, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.wait_tv);
        View cancelButton = rootView.findViewById(R.id.cancel_btn);
        View closeButton = rootView.findViewById(R.id.close_btn);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        if(textRes != 0)
            textView.setText(textRes);

        cancelButton.setVisibility(cancelable ? View.VISIBLE : View.GONE);
        closeButton.setVisibility(canClose ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(DIALOG_INTENT_ACTION_CANCEL));
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(DIALOG_INTENT_ACTION_CLOSE));
            }
        });

        return rootView;
    }


    private class DialogActionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DIALOG_INTENT_ACTION_CANCEL:
                case DIALOG_INTENT_ACTION_CLOSE:
                    dismiss();
                    break;
                case DIALOG_INTENT_ACTION_PROGRESS: {
                    int progress = intent.getIntExtra(DIALOG_PROGRESS_KEY, 0);
                    mProgressBar.setProgress(progress);
                    break;
                }
            }
        }
    }

}
