package com.robotumblr.sample.fragment;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robotumblr.sample.R;
import com.sun40.robotumblr.RoboTumblr;
import com.sun40.robotumblr.TumblrExtras;
import com.sun40.robotumblr.TumblrRequest;
import com.sun40.robotumblr.model.Post;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executor;

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
        Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;
        for (int i = 0; i < 5; i++) {
            TumblrRequest tumblr = new TumblrRequest(RoboTumblr.getConsumerToken(getContext()), RoboTumblr.getAccessToken(getContext()));
            new BlogInfoAsync(i, tumblr, this).executeOnExecutor(executor);
        }
    }


    private static class BlogInfoAsync extends AsyncTask<Void, Void, String> {

        private static final String TAG = "AsyncExecution";

        private WeakReference<BlogInfoSyncFragment> reference;
        private final int id;
        private final TumblrRequest request;

        BlogInfoAsync(int id, TumblrRequest request, BlogInfoSyncFragment fragment) {
            reference = new WeakReference<BlogInfoSyncFragment>(fragment);
            this.id = id;
            this.request = request;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute " + id);
        }

        @Override
        protected String doInBackground(Void... params) {
            if (params == null)
                return null;

            try {
//                String hostname = params[0];

                List<Post> list = request.tagged("lol", 0, 10, TumblrExtras.Filter.RAW);

                StringBuffer b = new StringBuffer();
                for (Post post : list) {
                    b.append(post.toString());
                }

                return b.toString();

            } catch (RetrofitError error) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String str) {
            Log.d(TAG, "onPostExecute " + id);
        }
    }
}
