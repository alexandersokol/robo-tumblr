package com.sun40.robotumblr;


import com.sun40.robotumblr.token.ConsumerToken;
import com.sun40.robotumblr.token.RequestToken;

import retrofit.RequestInterceptor;

/**
 * Created by Alexander Sokol
 * on 25.08.15 18:32.
 */
class OAuthAccessTokenInterceptor implements RequestInterceptor {


    private String mVerifier;
    private RequestToken mRequestToken;
    private ParamBuilder mBuilder;

    public OAuthAccessTokenInterceptor(ConsumerToken consumerToken, RequestToken requestToken, String verifier) {
        mVerifier = verifier;
        mRequestToken = requestToken;
        mBuilder = new ParamBuilder(consumerToken);
    }

    @Override
    public void intercept(RequestFacade request) {
        String header = mBuilder.getAccessTokenHeader(mRequestToken, mVerifier);
        request.addHeader(OAuthExtras.HEADER, header);

    }
}
