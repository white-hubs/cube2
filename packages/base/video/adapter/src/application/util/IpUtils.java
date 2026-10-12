package com.guhecloud.cube.srv.gen.adapter.video.server.application.util;

import com.guhecloud.platform.base.util.StringUtils;

import java.net.URI;

public class IpUtils {

    private IpUtils() {

    }

    public static String replaceSchema(String url, String schema) {
        URI uri = URI.create(url);
        String urlSchema = uri.getScheme();
        return url.replace(urlSchema + "://", schema);
    }

    public static String replaceHost(String url, String host) {
        URI uri = URI.create(url);
        String uriHost = uri.getHost();
        return url.replace(uriHost, host);
    }

    public static String replacePort(String url, String port) {
        URI uri = URI.create(url);
        int urlPort = uri.getPort();
        return url.replace(String.valueOf(urlPort), port);
    }

    public static String replaceHostPort(String url, String host, String port) {
        URI uri = URI.create(url);
        int urlPort = uri.getPort();
        String replaceUrl = url;
        if (StringUtils.isNotEmpty(host)) {
            replaceUrl = replaceUrl.replace(uri.getHost(), host);
        }
        if (StringUtils.isNotEmpty(port)) {
            replaceUrl = replaceUrl.replace(String.valueOf(urlPort), port);
        }
        return replaceUrl;
    }

    public static String replaceUrl(String url, String newUrl, String endStr) {
        String reservePath = url.substring(url.lastIndexOf(endStr));
        return newUrl + reservePath;
    }

    public static String replaceHostPort(String url, String newUrl) {
        URI uri = URI.create(url);
        String replaceUrl = url;
        if (StringUtils.isNotEmpty(newUrl)) {
            replaceUrl = replaceUrl.replace(uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort(), newUrl);
        }
        return replaceUrl;
    }
}
