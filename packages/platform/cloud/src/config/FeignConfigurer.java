package com.guhecloud.platform.cloud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.platform.client.CustomFeignErrorDecoder;
import com.guhecloud.platform.cloud.interceptor.FeignRequestInterceptor;
import com.guhecloud.platform.cloud.interceptor.FeignResponseInterceptor;
import com.guhecloud.platform.security.config.FeignSecurityProperties;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.optionals.OptionalDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

/**
 * Feign配置
 *
 * @author Woody
 * @date 2021/7/17
 */
@Configuration
@ConditionalOnClass({ Feign.class })
@AutoConfigureBefore(FeignAutoConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeignConfigurer {

    private final ObjectFactory<HttpMessageConverters> messageConverters;
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    private final FeignSecurityProperties feignSecurityProperties;

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
                                                            OAuth2ClientProperties properties
                                                           ) {
        return new FeignRequestInterceptor(feignSecurityProperties, oAuth2AuthorizedClientManager, properties);
    }

    @Bean
    public Decoder decoder() {
        ObjectMapper serializer = this.mappingJackson2HttpMessageConverter.getObjectMapper();
        Decoder delegate = new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
        return new FeignResponseInterceptor(delegate, serializer);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
