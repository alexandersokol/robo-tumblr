package com.robotumblr.sample.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.QuotePost;
import com.sun40.robotumblr.model.posting.PostCreator;
import com.sun40.robotumblr.model.posting.QuotePostCreator;

/**
 * Created by Alexander Sokol
 * on 24.09.15 18:08.
 */
public class PostQuoteFragment extends BasePostFragment {

    private EditText mSourceEdit;
    private EditText mQuoteEdit;

    @Override
    protected View createView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_quote, null, false);

        mSourceEdit = (EditText) rootView.findViewById(R.id.source_et);
        mQuoteEdit = (EditText) rootView.findViewById(R.id.quote_et);

        rootView.findViewById(R.id.default_text_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuoteEdit.setText(getContext().getString(R.string.default_text));
            }
        });

        return rootView;
    }

    @Override
    protected PostCreator onPost() {
        QuotePostCreator creator = new QuotePostCreator();
        creator.setSource(mSourceEdit.getText().toString());
        creator.setQuote(mQuoteEdit.getText().toString());
        return creator;
    }

    @Override
    protected void onPostInit(Post p) {
        QuotePost post = ((QuotePost) p);
        if (post.getSource() != null)
            mSourceEdit.setText(post.getSource());

        if (post.getText() != null)
            mQuoteEdit.setText(post.getText());
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_post_quote;
    }
}
