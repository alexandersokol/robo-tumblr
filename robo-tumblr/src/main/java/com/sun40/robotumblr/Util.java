package com.sun40.robotumblr;

/**
 * Created by Alexander Sokol
 * on 08.10.15.
 */
class Util {

    private Util() {
    }

    static String checkHostname(String hostname) {
        if (hostname != null) {
            if (!hostname.contains(TumblrExtras.HOSTNAME_SUFFIX))
                hostname += TumblrExtras.HOSTNAME_SUFFIX;
            return hostname;
        }
        return null;
    }

}
