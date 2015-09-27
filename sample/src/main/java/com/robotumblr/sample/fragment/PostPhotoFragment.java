package com.robotumblr.sample.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.robotumblr.sample.ItemAdapter;
import com.robotumblr.sample.R;
import com.sun40.robotumblr.model.posting.PhotoPostCreator;
import com.sun40.robotumblr.model.posting.PostCreator;

import java.io.File;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 25.09.15 11:01.
 */
public class PostPhotoFragment extends BasePostFragment implements ItemAdapter.OnItemClick, RadioGroup.OnCheckedChangeListener {

    private static final int PICK_IMAGE = 2;

    private int mLastClickPosition = -1;
    private ItemAdapter mAdapter;

    private EditText mCaptionEdit;
    private EditText mLinkEdit;


    @Override
    protected View createView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_post_photo, null, false);

        mAdapter = new ItemAdapter(this);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);

        rootView.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.add();
            }
        });

        rootView.findViewById(R.id.remove_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.remove();
            }
        });

        rootView.findViewById(R.id.default_link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinkEdit.setText(getContext().getString(R.string.default_link));
            }
        });

        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.source_radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        mCaptionEdit = (EditText) rootView.findViewById(R.id.caption_et);
        mLinkEdit = (EditText) rootView.findViewById(R.id.link_et);

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && mLastClickPosition != -1) {

            Uri imageUri = data.getData();
            if (imageUri == null) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(imageUri, filePathColumn,
                    null, null, null);

            if (cursor == null || !cursor.moveToFirst()) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                return;
            }

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();

            if (imagePath == null) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            } else
                mAdapter.setData(mLastClickPosition, imagePath);

            mLastClickPosition = -1;
        }
    }


    @Override
    protected PostCreator onPost() {
        PhotoPostCreator creator = new PhotoPostCreator();
        creator.setCaption(mCaptionEdit.getText().toString());
        creator.setLink(mLinkEdit.getText().toString());

        List<String> data = mAdapter.getData();
        for (String str : data) {
            if (!TextUtils.isEmpty(str))
                if (mAdapter.isFileMode())
                    creator.addFile(new File(str));
                else
                    creator.setSource(str);
        }

        return creator;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title_post_photo;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
        mLastClickPosition = position;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mAdapter.setIsFileMode(checkedId != R.id.link_cb);
    }
}
