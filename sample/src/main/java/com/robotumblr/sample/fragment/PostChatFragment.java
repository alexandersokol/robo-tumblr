package com.robotumblr.sample.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.model.ChatPost;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.posting.ChatPostCreator;
import com.sun40.robotumblr.model.posting.PostCreator;

/**
 * Created by Alexander Sokol
 * on 24.09.15 18:23.
 */
public class PostChatFragment extends BasePostFragment {

    private EditText mTitleEdit;
    private EditText mConversationEdit;

    @Override
    protected View createView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_chat, null, false);

        mTitleEdit = (EditText) rootView.findViewById(R.id.title_et);
        mConversationEdit = (EditText) rootView.findViewById(R.id.conversation_et);

        rootView.findViewById(R.id.default_text_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConversationEdit.setText(getContext().getString(R.string.default_text));
            }
        });

        return rootView;
    }

    @Override
    protected PostCreator onPost() {
        ChatPostCreator creator = new ChatPostCreator();
        creator.setTitle(mTitleEdit.getText().toString());
        creator.setConversation(mConversationEdit.getText().toString());
        return creator;
    }

    @Override
    protected void onPostInit(Post p) {
        ChatPost chat = (ChatPost) p;
        if (chat.getTitle() != null)
            mTitleEdit.setText(chat.getTitle());
        if (chat.getBody() != null)
            mConversationEdit.setText(chat.getBody());
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_post_chat;
    }
}
