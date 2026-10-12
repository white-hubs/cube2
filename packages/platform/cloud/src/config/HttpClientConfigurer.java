package com.guhecloud.platform.cloud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.platform.base.standard.http.HttpClientFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Http客户端配置
 *
 * @author Woody
 * @date 2021/7/23
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HttpClientConfigurer {
    private final ObjectMapper objectMapper;
    private final HttpClientPoolProperties httpClientPoolProperties;

    @Bean
    //    @LoadBalanced
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        var restTemplate = new RestTemplate(factory);
        this.modifyDefaultMessageConverters(restTemplate);
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        var clientHttpRequestFactory = new CustomHttpRequestFactory(this.httpClient());
        // 连接超时
        clientHttpRequestFactory.setConnectTimeout(this.httpClientPoolProperties.getConnectTimeout());
        // 数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(this.httpClientPoolProperties.getSocketTimeout());
        // 从连接池获取请求连接的超时时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(this.httpClientPoolProperties.getConnectionRequestTimeout());
        return clientHttpRequestFactory;
    }

    @Bean
    public HttpClient httpClient() {
        //使用Httpclient连接池的方式配置(推荐)，同时支持netty，okHttp以及其他http框架
        PoolingHttpClientConnectionManager poolingManager = null;
        try {
            poolingManager = HttpClientFactory.createPoolingManager(httpClientPoolProperties.getMaxTotal(),
                                                                    httpClientPoolProperties.getDefaultMaxPerRoute(),
                                                                    httpClientPoolProperties.getSocketTimeout(),
                                                                    httpClientPoolProperties.getConnectionRequestTimeout());
        } catch (GeneralSecurityException e) {
            log.warn("SSL setting failed: {}", e.getMessage());
        }
        //构建客户端
        return HttpClientBuilder.create()
                .setConnectionManager(poolingManager)
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .evictExpiredConnections()//设置后台线程剔除失效连接
                .evictIdleConnections(TimeValue.of(this.httpClientPoolProperties.getMaxIdleTime(), TimeUnit.MILLISECONDS))
                .build();
    }

    @Bean
    public HttpRequestRetryStrategy httpRequestRetryStrategy() {
        return new DefaultHttpRequestRetryStrategy(this.httpClientPoolProperties.getRetryCount(), TimeValue.of(0, TimeUnit.MILLISECONDS));
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return new DefaultConnectionKeepAliveStrategy() {
            @Override
            public TimeValue getKeepAliveDuration(HttpResponse response, HttpContext context) {
                var duration = super.getKeepAliveDuration(response, context);
                return duration.getDuration() < 0 ? TimeValue.of(httpClientPoolProperties.getKeepAliveTime(), TimeUnit.MILLISECONDS)
                                                  : duration;
            }
        };
    }

    /**
     * 修改默认的消息转换配置
     *
     * @param restTemplate RestTemplate实例
     */
    private void modifyDefaultMessageConverters(@NonNull RestTemplate restTemplate) {
        var messageConverters = restTemplate.getMessageConverters().stream()
                .map(converter -> {
                    if (converter instanceof StringHttpMessageConverter) {
                        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
                    } else if (converter instanceof MappingJackson2HttpMessageConverter) {
                        var mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(this.objectMapper);
                        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                                Arrays.asList(
                                        MediaType.TEXT_PLAIN,
                                        MediaType.APPLICATION_JSON,
                                        MediaType.APPLICATION_FORM_URLENCODED,
                                        new MediaType("application", "*+json")
                                             )
                                                                                  );
                        return mappingJackson2HttpMessageConverter;
                    }
                    return converter;
                }).collect(Collectors.toList());
        restTemplate.setMessageConverters(messageConverters);
    }
}
