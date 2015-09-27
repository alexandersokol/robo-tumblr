package com.sun40.robotumblr;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;


import com.sun40.robotumblr.token.AccessToken;
import com.sun40.robotumblr.token.ConsumerToken;
import com.sun40.robotumblr.token.RequestToken;
import com.sun40.robotumblr.token.Token;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Alexander Sokol
 * on 26.08.15 19:15.
 */
final class ParamBuilder {

    private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";

    private static final String PARAM_SEPARATOR = ", ";
    private static final String PREAMBLE = "OAuth ";
    private static final int ESTIMATED_PARAM_LENGTH = 20;

    private List<Parameter> mParameters = new ArrayList<>();
    private List<Parameter> mAdditionalParameters = new ArrayList<>();
    private String mVerb;
    private String mSanitizedUrl;
    private String mCompleteUrl;
    private AccessToken mAccessToken;
    private final ConsumerToken mConsumerToken;


    public ParamBuilder(ConsumerToken consumerToken) {
        mConsumerToken = consumerToken;

        if (mConsumerToken == null || mConsumerToken.isEmpty())
            throw new IllegalArgumentException("Consumer token is empty");
    }


    /**
     * Method which uses tumblr constants
     *
     * @return request token header
     */
    public String getRequestTokenHeader() {
        return getRequestTokenHeader(OAuthExtras.REQUEST_TOKEN_VERB, OAuthExtras.REQUEST_TOKEN_URL, TumblrExtras.CALLBACK_URL);
    }


    public String getRequestTokenHeader(String verb, String url, String callback) {
        cleanUp();

        mVerb = verb;
        mCompleteUrl = url;
        mSanitizedUrl = url;

        if (TextUtils.isEmpty(mVerb))
            throw new IllegalArgumentException("Verb is empty");

        if (TextUtils.isEmpty(mCompleteUrl))
            throw new IllegalArgumentException("Url is empty");

        mParameters.add(new Parameter(OAuthExtras.CALLBACK, callback));
        initOAuthBaseParams();
        mParameters.add(new Parameter(OAuthExtras.SIGNATURE, getSignature(new Token("", ""))));

        String header = extractHeaderParams();
        cleanUp();
        return header;
    }


    /**
     * Method to get request token for Tumblr
     *
     * @param requestToken request token
     * @param verifier     authorization verifier
     * @return header
     */
    public String getAccessTokenHeader(RequestToken requestToken, String verifier) {
        return getAccessTokenHeader(requestToken, verifier, OAuthExtras.ACCESS_TOKEN_VERB, OAuthExtras.ACCESS_TOKEN_URL);
    }


    public String getAccessTokenHeader(RequestToken requestToken, String verifier, String verb, String url) {
        cleanUp();

        mVerb = verb;
        mCompleteUrl = url;
        mSanitizedUrl = url;

        if (requestToken == null || requestToken.isEmpty())
            throw new IllegalArgumentException("Request Token is empty");
        if (TextUtils.isEmpty(verifier))
            throw new IllegalArgumentException("Verifier is empty");
        if (TextUtils.isEmpty(mVerb))
            throw new IllegalArgumentException("Verb is empty");
        if (TextUtils.isEmpty(mCompleteUrl))
            throw new IllegalArgumentException("Url is empty");

        mParameters.add(new Parameter(OAuthExtras.TOKEN, requestToken.getToken()));
        mParameters.add(new Parameter(OAuthExtras.VERIFIER, verifier));
        initOAuthBaseParams();
        mParameters.add(new Parameter(OAuthExtras.SIGNATURE, getSignature(requestToken)));

        String header = extractHeaderParams();
        cleanUp();
        return header;
    }


    public void setAccessToken(AccessToken accessToken) {
        mAccessToken = accessToken;
    }


    public void addAdditionalParameters(Map<String, String> params) {
        mAdditionalParameters.clear();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            mAdditionalParameters.add(new Parameter(key, value, false));
        }
    }


    public String getSignedHeader(String verb, String url) throws IllegalArgumentException {
        if (mAccessToken == null)
            throw new IllegalArgumentException("Access Token is null");

        return getSignedHeader(mAccessToken, verb, url);
    }


    public String getSignedHeader(AccessToken accessToken, String verb, String url) {
        cleanUp();
        mVerb = verb;
        mCompleteUrl = url;

        if (accessToken == null)
            throw new IllegalArgumentException("Access Token is empty");
        if (TextUtils.isEmpty(mVerb))
            throw new IllegalArgumentException("Verb is empty");
        if (TextUtils.isEmpty(mCompleteUrl))
            throw new IllegalArgumentException("Url is empty");

        extractQueryParameters(url);

        mParameters.addAll(mAdditionalParameters);

        if (!TextUtils.isEmpty(accessToken.getToken()))
            mParameters.add(new Parameter(OAuthExtras.TOKEN, accessToken.getToken()));

        initOAuthBaseParams();
        mParameters.add(new Parameter(OAuthExtras.SIGNATURE, getSignature(accessToken)));

        String header = extractHeaderParams();
        mAdditionalParameters.clear();
        cleanUp();
        return header;
    }


    private void cleanUp() {
        mParameters.clear();
        mVerb = null;
        mCompleteUrl = null;
        mSanitizedUrl = null;
    }

    private void initOAuthBaseParams() {
        String timestamp = getTimestamp();
        String nonce = getNonce(timestamp);

        mParameters.add(new Parameter(OAuthExtras.TIMESTAMP, timestamp));
        mParameters.add(new Parameter(OAuthExtras.NONCE, nonce));
        mParameters.add(new Parameter(OAuthExtras.CONSUMER_KEY, mConsumerToken.getToken()));
        mParameters.add(new Parameter(OAuthExtras.SIGN_METHOD, OAuthExtras.SIGNATURE_METHOD));
        mParameters.add(new Parameter(OAuthExtras.VERSION, OAuthExtras.VERSION_10));

        // TODO: 27.08.15 add Signature
    }


    private String extractHeaderParams() {
        if (mParameters == null || mParameters.isEmpty()) {
            throw new IllegalArgumentException("Parameters is empty");
        }

        List<Parameter> queryParams = new ArrayList<>();
        for (Parameter parameter : mParameters) {
            if (!parameter.oauth)
                queryParams.add(parameter);
        }

        for (Parameter parameter : queryParams) {
            mParameters.remove(parameter);
        }

        try {
            StringBuilder stringBuilder = new StringBuilder(mParameters.size() * ESTIMATED_PARAM_LENGTH);
            stringBuilder.append(PREAMBLE);
            for (Parameter parameter : mParameters) {
                if (stringBuilder.length() > PREAMBLE.length())
                    stringBuilder.append(PARAM_SEPARATOR);
                stringBuilder.append(String.format("%s=\"%s\"", parameter.key, StringEncoder.encode(parameter.value)));
            }
            return stringBuilder.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    private void extractQueryParameters(String url) {
        if (!TextUtils.isEmpty(url)) {

            int firstParamPosition = url.indexOf("?");
            if (firstParamPosition == -1) {
                mSanitizedUrl = url;
                return;
            } else
                mSanitizedUrl = url.substring(0, firstParamPosition);

            String paramString = url.substring(firstParamPosition + 1, url.length());
            while (paramString.length() > 0) {
                int paramNameEnd = paramString.indexOf("=");
                String paramName = paramString.substring(0, paramNameEnd);
                paramString = paramString.substring(paramNameEnd + 1, paramString.length());

                int paramValueEnd = paramString.indexOf("&");

                String paramValue;
                if (paramValueEnd == -1) {
                    paramValue = paramString;
                    paramString = paramString.substring(0, 0);
                } else {
                    paramValue = paramString.substring(0, paramValueEnd);
                    paramString = paramString.substring(paramValueEnd + 1, paramString.length());
                }
                mParameters.add(new Parameter(paramName, paramValue, false));
            }
        }
    }


    private String getSignature(Token token) {
        String baseString = extractParamsToString();

        if (token == null)
            throw new IllegalArgumentException("Token is null");
        if (token.getSecret() == null)
            throw new IllegalArgumentException("Token secret is null");
        if (TextUtils.isEmpty(baseString))
            throw new IllegalArgumentException("base String is empty");

        try {
            String encodedConsumerSecret = StringEncoder.encode(mConsumerToken.getSecret());
            String encodedTokenSecret = StringEncoder.encode(token.getSecret());
            String keyStr = encodedConsumerSecret + '&' + encodedTokenSecret;

            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes(UTF8), HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(keySpec);
            byte[] bytes = mac.doFinal(baseString.getBytes(UTF8));

            return new String(Base64.encode(bytes, Base64.NO_WRAP)).replace(CARRIAGE_RETURN, EMPTY_STRING);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String extractParamsToString() {
        if (mParameters == null || mParameters.isEmpty())
            throw new IllegalStateException("Parameters set is null");

        try {
            String encodedVerb = StringEncoder.encode(mVerb);
            String encodedUrl = StringEncoder.encode(mSanitizedUrl);

            Collections.sort(mParameters);

            StringBuilder stringBuilder = new StringBuilder();
            for (Parameter parameter : mParameters)
                stringBuilder.append('&').append(parameter.asUrlEncodedPair());
            String encodedParams = StringEncoder.encode(stringBuilder.toString().substring(1));

            return String.format(AMPERSAND_SEPARATED_STRING, encodedVerb, encodedUrl, encodedParams);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getTimestamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return String.valueOf(timestamp);
    }

    private String getNonce(String timestampStr) {
        long timestamp = Long.parseLong(timestampStr);
        Random random = new Random();
        long nonce = timestamp + random.nextInt();
        return String.valueOf(nonce);
    }


    static class Parameter implements Comparable<Parameter> {

        String key;
        String value;
        boolean oauth;

        public Parameter(String key, String value) {
            this(key, value, true);
        }

        public Parameter(String key, String value, boolean oauth) {
            this.key = key;
            this.value = value;
            this.oauth = oauth;
        }


        public String asUrlEncodedPair() throws UnsupportedEncodingException {
            return StringEncoder.encode(key).concat("=").concat(StringEncoder.encode(value));
        }


        public boolean equals(Object other) {
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof Parameter)) return false;

            Parameter otherParam = (Parameter) other;
            return otherParam.key.equals(key) && otherParam.value.equals(value);
        }


        public int hashCode() {
            return key.hashCode() + value.hashCode();
        }

        @Override
        public int compareTo(@NonNull Parameter parameter) {
            int keyDiff = key.compareTo(parameter.key);

            return keyDiff != 0 ? keyDiff : value.compareTo(parameter.value);
        }
    }

}
