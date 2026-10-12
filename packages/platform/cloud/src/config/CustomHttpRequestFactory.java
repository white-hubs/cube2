package com.guhecloud.platform.cloud.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

public class CustomHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

    public CustomHttpRequestFactory(HttpClient httpClient) {
        super(httpClient);
    }

    @Override
    protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {

        return new HttpUriRequestBase(httpMethod.name(), uri);
    }

}
