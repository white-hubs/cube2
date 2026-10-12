package com.guhecloud.platform.cloud.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.util.Objects;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    private final OAuth2ClientProperties oAuth2ClientProperties;

    public OAuth2FeignRequestInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
                                         OAuth2ClientProperties oAuth2ClientProperties
                                        ) {
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    @Override
    public void apply(RequestTemplate template) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                .withClientRegistrationId(oAuth2ClientProperties.getRegistration().keySet().iterator().next())
                .principal(oAuth2ClientProperties.getRegistration().values().iterator().next().getClientSecret())
                .build();
        OAuth2AuthorizedClient authorize = oAuth2AuthorizedClientManager.authorize(request);
        template.header("Authorization", "Bearer " + Objects.requireNonNull(authorize).getAccessToken().getTokenValue());
    }
}
