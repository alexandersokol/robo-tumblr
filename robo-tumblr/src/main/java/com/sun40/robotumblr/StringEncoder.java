package com.sun40.robotumblr;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Alexander Sokol
 * on 26.08.15 19:30.
 */
final class StringEncoder {

    private static final String TAG = "StringEncoder";

    private static final String CHARSET = "UTF-8";
    private static final Map<String, String> ENCODING_RULES;

    static {
        Map<String, String> rules = new HashMap<>();
        rules.put("*", "%2A");
        rules.put("+", "%20");
        rules.put("%7E", "~");
        ENCODING_RULES = Collections.unmodifiableMap(rules);
    }

    protected StringEncoder() {

    }

    public static String encode(String plain) throws UnsupportedEncodingException {
        if (plain == null)
            throw new IllegalArgumentException("Cannot encode null object");
        String encoded = URLEncoder.encode(plain, CHARSET);
        for (Map.Entry<String, String> rule : ENCODING_RULES.entrySet()) {
            encoded = applyRule(encoded, rule.getKey(), rule.getValue());
        }
        return encoded;
    }


    public static String decode(String encoded) throws UnsupportedEncodingException {
        if (encoded == null)
            throw new IllegalArgumentException("Cannot encode null object");
        return URLDecoder.decode(encoded, CHARSET);
    }


    private static String applyRule(String encoded, String toReplace, String replacement) {
        return encoded.replaceAll(Pattern.quote(toReplace), replacement);
    }

}
