package com.sun40.robotumblr;

import com.sun40.robotumblr.token.AccessToken;
import com.sun40.robotumblr.token.RequestToken;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alexander Sokol
 * on 26.08.15 19:04.
 */
interface OAuthExtras {

    String REQUEST_TOKEN_VERB = "POST";
    String ACCESS_TOKEN_VERB = "POST";

    String AUTHORIZE_URL = "https://www.tumblr.com/oauth/authorize?oauth_token=%s";
    String REQUEST_TOKEN_URL = "http://www.tumblr.com/oauth/request_token";
    String ACCESS_TOKEN_URL = "http://www.tumblr.com/oauth/access_token";

    String OAUTH_ENDPOINT = "http://www.tumblr.com/oauth";

    String VERSION_10 = "1.0";
    String SIGNATURE_METHOD = "HMAC-SHA1";

    String TIMESTAMP = "oauth_timestamp";
    String NONCE = "oauth_nonce";
    String CONSUMER_KEY = "oauth_consumer_key";
    String SIGN_METHOD = "oauth_signature_method";
    String VERSION = "oauth_version";

    String SIGNATURE = "oauth_signature";

    String CALLBACK = "oauth_callback";

    String TOKEN = "oauth_token";
    String VERIFIER = "oauth_verifier";
    String HEADER = "Authorization";

    class Extractor{
        private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
        private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");

        public static RequestToken extractRequestToken(String response){
            String token = extract(response, TOKEN_REGEX);
            String secret = extract(response, SECRET_REGEX);
            return new RequestToken(token, secret);
        }

        public static AccessToken extractAccessToken(String response){
            String token = extract(response, TOKEN_REGEX);
            String secret = extract(response, SECRET_REGEX);
            return new AccessToken(token, secret);
        }

        public static String extractVerifier(String responseUrl){
            int start = responseUrl.lastIndexOf("=") + 1;
            int end = responseUrl.length();

            return responseUrl.substring(start, end);
        }

        public static String getAuthorizeUrl(RequestToken requestToken){
            return String.format(AUTHORIZE_URL, requestToken.getToken());
        }

        private static String extract(String response, Pattern p)
        {
            Matcher matcher = p.matcher(response);
            if (matcher.find() && matcher.groupCount() >= 1)
            {
                try {
                    return StringEncoder.decode(matcher.group(1));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Response body is incorrect. Can't extract token and secret from this: '" + response + "'", null);
                }
            }
            else
            {
                throw new IllegalArgumentException("Response body is incorrect. Can't extract token and secret from this: '" + response + "'", null);
            }
        }
    }
}
