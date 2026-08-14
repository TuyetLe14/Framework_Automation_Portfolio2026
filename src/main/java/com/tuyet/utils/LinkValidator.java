package com.tuyet.utils;

import java.net.HttpURLConnection;
import java.net.URI;

public final class LinkValidator {

    private static final int HTTP_TIMEOUT_MS = 5000;

    private LinkValidator() {
        // Utility class
    }

    public static int getStatusCode(String url) {

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection)
                    URI.create(url)
                            .toURL()
                            .openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(HTTP_TIMEOUT_MS);
            connection.setReadTimeout(HTTP_TIMEOUT_MS);

            return connection.getResponseCode();

        } catch (Exception e) {
            return 404;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
