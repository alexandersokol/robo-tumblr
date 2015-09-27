package com.sun40.robotumblr;


import com.sun40.robotumblr.token.ConsumerToken;

import retrofit.RequestInterceptor;

/**
 * Created by Alexander Sokol
 * on 25.08.15 15:34.
 */
class OAuthRequestTokenInterceptor implements RequestInterceptor {

    private ParamBuilder mBuilder;

    public OAuthRequestTokenInterceptor(ConsumerToken consumerToken) {
        mBuilder = new ParamBuilder(consumerToken);
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader(OAuthExtras.HEADER, mBuilder.getRequestTokenHeader());
    }
}
