package com.robotumblr.sample.fragment;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.RoboTumblr;
import com.sun40.robotumblr.model.Blog;

import java.lang.ref.WeakReference;

import retrofit.RetrofitError;

/**
 * Created by Alexander Sokol
 * on 08.10.15.
 */
public class BlogInfoSyncFragment extends BaseFragment {

    private TextView mContentText;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_sync_test, null);

        mContentText = (TextView) rootView.findViewById(R.id.content_tv);

        return rootView;
    }

    @Override
    protected int getToolbarText() {
        return R.string.title;
    }

    @Override
    protected int getApiReference() {
        return R.string.link_blog_info;
    }

    @Override
    protected void onRun() {
        new BlogInfoAsync(this).execute("sun40");
    }


    private static class BlogInfoAsync extends AsyncTask<String, Void, Blog> {

        private WeakReference<BlogInfoSyncFragment> reference;

        BlogInfoAsync(BlogInfoSyncFragment fragment) {
            reference = new WeakReference<BlogInfoSyncFragment>(fragment);
        }

        @Override
        protected Blog doInBackground(String... params) {
            if (params == null)
                return null;

            try {
                String hostname = params[0];
                return RoboTumblr.getInstanse(reference.get().getContext()).blogInfo(hostname);
            } catch (RetrofitError error) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Blog blog) {
            if (blog != null && reference.get() != null) {
                reference.get().mContentText.setText(blog.toString());
            }
            else{
                Toast.makeText(reference.get().getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
