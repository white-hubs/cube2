package com.guhecloud.platform.cloud.config;

import feign.Feign;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass({ Feign.class })
@AutoConfigureBefore(FeignAutoConfiguration.class)
@EnableConfigurationProperties(OAuth2ClientProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeignSecurityConfigurer {

    @Bean
    public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                       OAuth2AuthorizedClientService oAuth2AuthorizedClientService
                                                                      ) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
        List<ClientRegistration> clientRegistrations = new ArrayList<>();
        properties.getRegistration().forEach((key, value) -> {
            OAuth2ClientProperties.Provider provider = properties.getProvider().get(key);
            ClientRegistration registration = ClientRegistration.withRegistrationId(key)
                    .clientId(value.getClientId())
                    .clientSecret(value.getClientSecret())
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .tokenUri(provider.getTokenUri())
                    .build();
            clientRegistrations.add(registration);
        });
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }
}
