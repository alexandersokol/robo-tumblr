package com.robotumblr.sample.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.robotumblr.sample.util.StorageUtils;
import com.sun40.robotumblr.model.Post;
import com.sun40.robotumblr.model.VideoPost;
import com.sun40.robotumblr.model.posting.PostCreator;
import com.sun40.robotumblr.model.posting.VideoPostCreator;

/**
 * Created by Alexander Sokol
 * on 25.09.15 16:40.
 */
public class PostVideoFragment extends BasePostFragment {

    private static final int VIDEO_PICK = 3;

    private RadioButton mLinkRadio;
    private EditText mCaptionEdit;
    private EditText mSourceEdit;

    @Override
    protected View createView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_video, null, false);

        mLinkRadio = (RadioButton) rootView.findViewById(R.id.link_cb);
        mCaptionEdit = (EditText) rootView.findViewById(R.id.caption_et);
        mSourceEdit = (EditText) rootView.findViewById(R.id.source_et);

        rootView.findViewById(R.id.default_link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinkRadio.isChecked()) {
                    mSourceEdit.setText(getContext().getString(R.string.default_video_link));
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, VIDEO_PICK);
                }
            }
        });

        ((RadioGroup) rootView.findViewById(R.id.source_radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSourceEdit.setText("");
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Uri videoUri = data.getData();

            if (videoUri == null) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                return;
            }

            String videoPath = StorageUtils.getRealPathFromURI(getContext(), videoUri);
            if (videoPath == null)
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            else
                mSourceEdit.setText(videoPath);
        }
    }


    @Override
    protected PostCreator onPost() {
        VideoPostCreator creator = new VideoPostCreator();

        creator.setCaption(mCaptionEdit.getText().toString());
        if (mLinkRadio.isChecked())
            creator.setEmbed(mSourceEdit.getText().toString());
        else
            creator.setData(mSourceEdit.getText().toString());

        return creator;
    }

    @Override
    protected void onPostInit(Post p) {
        VideoPost post = (VideoPost) p;
        if(post.getCaption() != null)
            mCaptionEdit.setText(post.getCaption());
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_post_video;
    }
}
