package com.robotumblr.sample;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander Sokol
 * on 25.09.15 12:55.
 */
public class ItemAdapter extends BaseAdapter implements View.OnClickListener, /*TextView.OnEditorActionListener,*/ TextWatcher {

    private boolean mIsFileMode;
    private LinkedList<String> mItems = new LinkedList<>();
    private OnItemClick mOnItemClickListener;

    {
        mItems.add("");
    }

    public ItemAdapter(OnItemClick onItemClick) {
        mOnItemClickListener = onItemClick;
    }


    public void setIsFileMode(boolean isFileMode) {
        mIsFileMode = isFileMode;
        mItems.clear();
        mItems.add("");
        notifyDataSetChanged();
    }

    public boolean isFileMode() {
        return mIsFileMode;
    }


    public void add() {
        if (mIsFileMode && mItems.size() < 10) {
            mItems.addLast("");
            notifyDataSetChanged();
        }
    }


    public void remove() {
        if (mItems.size() > 1) {
            mItems.removeLast();
            notifyDataSetChanged();
        }
    }

    public void setData(int pos, String data) {
        if (data != null) {
            mItems.set(pos, data);
            notifyDataSetChanged();
        }
    }

    public List<String> getData() {
        return mItems;
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public String getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.actionView.setTag(R.id.action_view, position);
        holder.actionView.setOnClickListener(this);
        holder.itemEdit.setText(mItems.get(position));
        holder.itemEdit.setTag(R.id.action_view, position);

        TextChangeListener textChangeListener = (TextChangeListener) holder.itemEdit.getTag(R.id.back_btn);
        if (textChangeListener != null)
            holder.itemEdit.removeTextChangedListener(textChangeListener);

        textChangeListener = new TextChangeListener(position) {
            @Override
            public void onTextChanged(String text, int position) {
                mItems.set(position, text);
            }
        };

        holder.itemEdit.addTextChangedListener(textChangeListener);
        holder.itemEdit.setTag(R.id.back_btn, textChangeListener);

        holder.itemEdit.removeTextChangedListener(this);
        holder.itemEdit.addTextChangedListener(this);


        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag(R.id.action_view);
        if (mIsFileMode) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(pos);
        } else {
            mItems.set(pos, v.getContext().getResources().getString(R.string.default_image_link));
            notifyDataSetChanged();
        }
    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        Log.d("Editor", "Press");
//        if(actionId == KeyEvent.ACTION_UP) {
//            int pos = (int) v.getTag(R.id.action_view);
//            mItems.set(pos, v.getText().toString());
//            return true;
//        }
//        return false;
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
//        Log.d("Editor", "After  " + s.toString());
    }


    private static class Holder {
        View actionView;
        EditText itemEdit;

        public Holder(View rootView) {
            actionView = rootView.findViewById(R.id.action_view);
            itemEdit = (EditText) rootView.findViewById(R.id.item_et);
        }
    }

    private abstract class TextChangeListener implements TextWatcher {

        private int position;

        public TextChangeListener(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            onTextChanged(s.toString(), position);
        }

        public abstract void onTextChanged(String text, int position);
    }


    public interface OnItemClick {
        void onItemClick(int position);
    }

}
