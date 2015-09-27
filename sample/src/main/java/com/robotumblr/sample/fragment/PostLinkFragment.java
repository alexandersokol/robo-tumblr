package com.robotumblr.sample.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.model.LinkPost;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.posting.LinkPostCreator;
import com.sun40.robotumblr.model.posting.PostCreator;

/**
 * Created by Alexander Sokol
 * on 24.09.15 17:24.
 */
public class PostLinkFragment extends BasePostFragment {

    private EditText mTitleEdit;
    private EditText mUrlEdit;
    private EditText mDescriptionEdit;


    @Override
    protected View createView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_link, null, false);

        mTitleEdit = (EditText) rootView.findViewById(R.id.title_et);
        mUrlEdit = (EditText) rootView.findViewById(R.id.url_et);
        mDescriptionEdit = (EditText) rootView.findViewById(R.id.description_et);

        rootView.findViewById(R.id.default_link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrlEdit.setText(getContext().getString(R.string.default_link));
            }
        });

        return rootView;
    }


    @Override
    protected PostCreator onPost() {
        LinkPostCreator creator = new LinkPostCreator();
        creator.setTitle(mTitleEdit.getText().toString());
        creator.setUrl(mUrlEdit.getText().toString());
        creator.setDescription(mDescriptionEdit.getText().toString());
        return creator;
    }

    @Override
    protected void onPostInit(Post p) {
        LinkPost post = (LinkPost) p;
        if (post.getTitle() != null)
            mTitleEdit.setText(post.getTitle());

        if (post.getUrl() != null)
            mUrlEdit.setText(post.getUrl());

        if (post.getDescription() != null)
            mDescriptionEdit.setText(post.getDescription());
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_post_link;
    }
}
