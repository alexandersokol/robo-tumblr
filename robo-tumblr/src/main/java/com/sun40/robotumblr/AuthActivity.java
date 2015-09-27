package com.sun40.robotumblr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sun40.robotumblr.token.RequestToken;


/**
 * Created by Alexander Sokol
 * on 17.08.15 19:21.
 */
public class AuthActivity extends Activity {

    private static final String TAG = "AuthActivity";

    public static final String KEY_REQUEST_TOKEN = "key_request_token";
    public static final String KEY_AUTH_VERIFIER = "key_auth_verifier";

    private RequestToken mRequestToken;
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mProgressBar = findViewById(R.id.progressBar);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new LoginWebViewClient());

        if (getIntent() != null) {
            RequestToken requestToken = getIntent().getParcelableExtra(KEY_REQUEST_TOKEN);
            if (requestToken != null) {
                mRequestToken = requestToken;
                String url = OAuthExtras.Extractor.getAuthorizeUrl(requestToken);
                webView.loadUrl(url);
            }
        }

    }

    private class LoginWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "redirect url: " + url);
            view.getSettings().setAppCacheEnabled(false);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e(TAG, "OnReceivedError: code: " + errorCode + " description: " + description + " url: " + failingUrl);
            Toast.makeText(AuthActivity.this, "Failed to load page", Toast.LENGTH_SHORT).show();
            AuthActivity.this.setResult(Activity.RESULT_CANCELED);
            AuthActivity.this.finish();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG, "onPageStarted " + url);
            if (url.contains(TumblrExtras.CALLBACK_URL)) {
                Intent intent = new Intent();
                String oauth_verifier = OAuthExtras.Extractor.extractVerifier(url);
                intent.putExtra(KEY_AUTH_VERIFIER, oauth_verifier);
                intent.putExtra(KEY_REQUEST_TOKEN, mRequestToken);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }
    }


    public static Intent startAuthorization(Context context, RequestToken requestToken) {
        Intent intent = new Intent(context, AuthActivity.class);
        intent.putExtra(KEY_REQUEST_TOKEN, requestToken);
        return intent;
    }

}
