package com.sun40.robotumblr;

import com.sun40.robotumblr.token.AccessToken;
import com.sun40.robotumblr.token.ConsumerToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Alexander Sokol
 * on 27.08.15 9:10.
 */
final class OAuthClient implements Client {
    private static final int CHUNK_SIZE = 4096;

    private static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    private static final int READ_TIMEOUT_MILLIS = 20 * 1000; // 20s

    private ParamBuilder mParamBuilder;

    public OAuthClient(ConsumerToken consumerToken, AccessToken accessToken) {
        mParamBuilder = new ParamBuilder(consumerToken);
        mParamBuilder.setAccessToken(accessToken);
    }


    @Override
    public Response execute(Request request) throws IOException {
        HttpURLConnection connection = openConnection(request);
        prepareRequest(connection, request);
        return readResponse(connection);
    }

    public void addPostParameters(Map<String, String> params) {
        mParamBuilder.addAdditionalParameters(params);
    }

    protected HttpURLConnection openConnection(Request request) throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) new URL(request.getUrl()).openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
        connection.setReadTimeout(READ_TIMEOUT_MILLIS);
        return connection;
    }

    void prepareRequest(HttpURLConnection connection, Request request) throws IOException {

        connection.setRequestMethod(request.getMethod());
        connection.setDoInput(true);

        List<Header> headers = new ArrayList<>(request.getHeaders());
        headers.add(new Header(OAuthExtras.HEADER, mParamBuilder.getSignedHeader(request.getMethod(), request.getUrl())));

        for (Header header : headers) {
            connection.addRequestProperty(header.getName(), header.getValue());
        }

        TypedOutput body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", body.mimeType());
            long length = body.length();
            if (length != -1) {
                connection.setFixedLengthStreamingMode((int) length);
                connection.addRequestProperty("Content-Length", String.valueOf(length));
            } else {
                connection.setChunkedStreamingMode(CHUNK_SIZE);
            }
            body.writeTo(connection.getOutputStream());
        }
    }

    Response readResponse(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();
        String reason = connection.getResponseMessage();
        if (reason == null) reason = ""; // HttpURLConnection treats empty reason as null.

        List<Header> headers = new ArrayList<Header>();
        for (Map.Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
            String name = field.getKey();
            for (String value : field.getValue()) {
                headers.add(new Header(name, value));
            }
        }

        String mimeType = connection.getContentType();
        int length = connection.getContentLength();
        InputStream stream;
        if (status >= 400) {
            stream = connection.getErrorStream();
        } else {
            stream = connection.getInputStream();
        }
        TypedInput responseBody = new TypedInputStream(mimeType, length, stream);
        return new Response(connection.getURL().toString(), status, reason, headers, responseBody);
    }

    private static class TypedInputStream implements TypedInput {
        private final String mimeType;
        private final long length;
        private final InputStream stream;

        private TypedInputStream(String mimeType, long length, InputStream stream) {
            this.mimeType = mimeType;
            this.length = length;
            this.stream = stream;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return length;
        }

        @Override
        public InputStream in() throws IOException {
            return stream;
        }
    }
}
