package com.guhecloud.platform.dcm.config;

import com.guhecloud.platform.dcm.auth.AuthTokenProperties;
import com.guhecloud.platform.dcm.auth.AuthTokenRunner;
import com.guhecloud.platform.dcm.auth.FeignRequestInterceptor;
import com.guhecloud.platform.dcm.auth.OuaaTokenStorage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthTokenProperties.class)
public class AuthConfig {

    @Bean
    AuthTokenRunner authTokenRunner(AuthTokenProperties authTokenProperties, OuaaTokenStorage tokenStorage) {
        AuthTokenRunner runner = new AuthTokenRunner(authTokenProperties, tokenStorage);
        runner.start();
        return runner;
    }

    @Bean
    public OuaaTokenStorage ouaaTokenStorage() {
        return new OuaaTokenStorage();
    }

    @Bean
    public FeignRequestInterceptor feignRequestInterceptor(OuaaTokenStorage tokenStorage, AuthTokenProperties authTokenProperties) {
        return new FeignRequestInterceptor(tokenStorage, authTokenProperties);
    }

}
