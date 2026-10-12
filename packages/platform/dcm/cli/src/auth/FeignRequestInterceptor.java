package com.guhecloud.platform.dcm.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignRequestInterceptor implements RequestInterceptor {

    private AuthTokenProperties authTokenProperties;

    private OuaaTokenStorage tokenStorage;

    public FeignRequestInterceptor(OuaaTokenStorage tokenStorage, AuthTokenProperties authTokenProperties) {
        this.tokenStorage = tokenStorage;
        this.authTokenProperties = authTokenProperties;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("tenant", String.valueOf(authTokenProperties.getTenant()));
        requestTemplate.header("Authorization", "bearer " + tokenStorage.getToken());
        requestTemplate.header("terminal", authTokenProperties.getTerminal());
    }

}
