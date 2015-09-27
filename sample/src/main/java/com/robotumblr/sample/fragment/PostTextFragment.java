package com.robotumblr.sample.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.model.posting.PostCreator;
import com.sun40.robotumblr.model.posting.TextPostCreator;

/**
 * Created by Alexander Sokol
 * on 24.09.15 16:29.
 */
public class PostTextFragment extends BasePostFragment {

    private EditText mTitleEdit;
    private EditText mBodyEdit;

    @Override
    protected View createView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_text, null, false);

        mTitleEdit = (EditText) rootView.findViewById(R.id.title_et);
        mBodyEdit = (EditText) rootView.findViewById(R.id.body_et);
        rootView.findViewById(R.id.default_text_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBodyEdit.setText(getContext().getString(R.string.default_text));
            }
        });

        return rootView;
    }

    @Override
    protected PostCreator onPost() {
        TextPostCreator creator = new TextPostCreator();
        creator.setTitle(mTitleEdit.getText().toString());
        creator.setBody(mBodyEdit.getText().toString());
        return creator;
    }


    @Override
    protected int getToolbarText() {
        return R.string.title_post_text;
    }
}
