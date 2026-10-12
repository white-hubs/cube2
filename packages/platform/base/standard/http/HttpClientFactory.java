package com.guhecloud.platform.base.standard.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HttpClientFactory {

    private static final Integer DEFAULT_MAX_CONN_TOTAL = 50;
    private static final Integer DEFAULT_MAX_CONN_PER_ROUTE = 5;
    private static final Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS = 200;
    private static final Integer DEFAULT_READ_TIMEOUT_MS = 20000;
    private static final Integer DEFAULT_CONNECTION_TIMEOUT_MS = 3000;

    /**
     * 创建HttpComponentsClientHttpRequestFactory
     *
     * @param maxConnTotal             连接池最大连接数
     * @param maxConnPerRoute          连接池同路由的并发数
     * @param connectionRequestTimeout 连接请求超时时间
     * @param readTimeout              读取超时时间
     * @param connectTimeout           连接超时时间
     *
     * @return ClientHttpRequestFactory
     */
    public static ClientHttpRequestFactory createHttpComponentsClientHttpRequestFactory(Integer maxConnTotal,
                                                                                        Integer maxConnPerRoute,
                                                                                        Integer connectionRequestTimeout,
                                                                                        Integer readTimeout,
                                                                                        Integer connectTimeout
                                                                                       ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // 创建一个不验证主机名的SSLSocketFactory
        var connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
        connectionManagerBuilder.setTlsSocketStrategy(createNoopHostnameTls())
                .setMaxConnTotal(maxConnTotal)
                .setMaxConnPerRoute(maxConnPerRoute);
        // 创建自定义的HttpClient
        var httpClient = HttpClients.custom()
                .setConnectionManager(connectionManagerBuilder.build())
                .build();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        requestFactory.setReadTimeout(readTimeout);
        requestFactory.setConnectTimeout(connectTimeout);
        return new BufferingClientHttpRequestFactory(requestFactory);
    }

    /**
     * 创建HttpComponentsClientHttpRequestFactory
     *
     * @param maxConnTotal             连接池最大连接数
     * @param maxConnPerRoute          连接池同路由的并发数
     * @param connectionRequestTimeout 连接请求超时时间
     * @param readTimeout              读取超时时间
     * @param connectTimeout           连接超时时间
     *
     * @return ClientHttpRequestFactory
     */
    public static ClientHttpRequestFactory createHttpComponentsClientHttpRequestFactory(Integer maxConnTotal,
                                                                                        Integer maxConnPerRoute,
                                                                                        Integer connectionRequestTimeout,
                                                                                        Integer readTimeout,
                                                                                        Integer connectTimeout,
                                                                                        boolean keepAlive
                                                                                       ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // 创建一个不验证主机名的SSLSocketFactory
        var connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
        connectionManagerBuilder.setTlsSocketStrategy(createNoopHostnameTls())
                .setMaxConnTotal(maxConnTotal)
                .setMaxConnPerRoute(maxConnPerRoute);
        var httpClientBuilder = HttpClients.custom()
                .setConnectionManager(connectionManagerBuilder.build());
        if (!keepAlive) {
            httpClientBuilder.setConnectionReuseStrategy((request, response, context) -> false);
        }
        // 创建自定义的HttpClient
        var httpClient = httpClientBuilder.build();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        requestFactory.setReadTimeout(readTimeout);
        requestFactory.setConnectTimeout(connectTimeout);
        return new BufferingClientHttpRequestFactory(requestFactory);
    }

    /**
     * 创建http连接池
     *
     * @param maxTotal           连接池最大连接数
     * @param defaultMaxPerRoute 连接池同路由的并发数
     * @param socketTimeout      socket超时时间
     * @param connectTimeout     连接超时时间
     *
     * @return PoolingHttpClientConnectionManager
     */
    public static PoolingHttpClientConnectionManager createPoolingManager(final int maxTotal,
                                                                          final int defaultMaxPerRoute,
                                                                          final int socketTimeout,
                                                                          final int connectTimeout
                                                                         ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        PoolingHttpClientConnectionManagerBuilder builder = PoolingHttpClientConnectionManagerBuilder.create();
        builder.setTlsSocketStrategy(createNoopHostnameTls());
        return builder
                .setMaxConnTotal(maxTotal)
                .setMaxConnPerRoute(defaultMaxPerRoute)
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                                                    .setSocketTimeout(Timeout.ofMilliseconds(socketTimeout))
                                                    .setConnectTimeout(Timeout.ofMilliseconds(connectTimeout))
                                                    .build())
                .build();
    }

    /**
     * tls 配置
     */
    public static SSLContext configureTls() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new SSLContextBuilder()
                .loadTrustMaterial(null, (chain, authType) -> true)
                .build();
    }

    /**
     * 创建一个不校验主机的tls策略
     *
     * @return DefaultClientTlsStrategy
     */
    public static DefaultClientTlsStrategy createNoopHostnameTls() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new DefaultClientTlsStrategy(Objects.requireNonNull(configureTls()), NoopHostnameVerifier.INSTANCE);
    }

    /**
     * 创建restTemplate默认的消息转换
     *
     * @param restTemplate restTemplate
     * @param objectMapper objectMapper
     *
     * @return HttpMessageConverter
     */
    public static List<HttpMessageConverter<?>> createDefaultConverts(RestTemplate restTemplate, ObjectMapper objectMapper) {
        return restTemplate.getMessageConverters().stream()
                .map(converter -> {
                    if (converter instanceof StringHttpMessageConverter) {
                        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
                    } else if (converter instanceof MappingJackson2HttpMessageConverter) {
                        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                                new MappingJackson2HttpMessageConverter(objectMapper);
                        mappingJackson2HttpMessageConverter
                                .setSupportedMediaTypes(
                                        Arrays.asList(
                                                MediaType.TEXT_PLAIN,
                                                MediaType.TEXT_HTML,
                                                MediaType.APPLICATION_JSON,
                                                MediaType.APPLICATION_FORM_URLENCODED,
                                                new MediaType("application", "*+json")
                                                     )
                                                       );
                        return mappingJackson2HttpMessageConverter;
                    }
                    return converter;
                }).toList();
    }

    /**
     * 创建一个restTemplate
     *
     * @param objectMapper objectMapper
     *
     * @return RestTemplate
     *
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     */
    public static RestTemplate createRestTemplate(ObjectMapper objectMapper
                                                 ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return createRestTemplate(DEFAULT_MAX_CONN_TOTAL,
                                  DEFAULT_MAX_CONN_PER_ROUTE,
                                  DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS,
                                  DEFAULT_READ_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS,
                                  objectMapper);
    }

    /**
     * 创建一个默认的restTemplate
     *
     * @return RestTemplate
     */
    public static RestTemplate createDefaultRestTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return createRestTemplate(DEFAULT_MAX_CONN_TOTAL,
                                  DEFAULT_MAX_CONN_PER_ROUTE,
                                  DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS,
                                  DEFAULT_READ_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS,
                                  new ObjectMapper());
    }

    /**
     * 创建restTemplate
     *
     * @param maxConnTotal             连接池最大连接数
     * @param maxConnPerRoute          连接池同路由的并发数
     * @param connectionRequestTimeout 连接请求超时时间
     * @param readTimeout              读取超时时间
     * @param connectTimeout           连接超时时间
     *
     * @return RestTemplate
     */
    public static RestTemplate createRestTemplate(Integer maxConnTotal,
                                                  Integer maxConnPerRoute,
                                                  Integer connectionRequestTimeout,
                                                  Integer readTimeout,
                                                  Integer connectTimeout,
                                                  ObjectMapper objectMapper
                                                 ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        var restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(HttpClientFactory.createHttpComponentsClientHttpRequestFactory(maxConnTotal,
                                                                                                      maxConnPerRoute,
                                                                                                      connectionRequestTimeout,
                                                                                                      readTimeout,
                                                                                                      connectTimeout));
        restTemplate.setMessageConverters(HttpClientFactory.createDefaultConverts(restTemplate, objectMapper));
        return restTemplate;
    }

    /**
     * 创建restTemplate
     *
     * @param maxConnTotal             连接池最大连接数
     * @param maxConnPerRoute          连接池同路由的并发数
     * @param connectionRequestTimeout 连接请求超时时间
     * @param readTimeout              读取超时时间
     * @param connectTimeout           连接超时时间
     *
     * @return RestTemplate
     */
    public static RestTemplate createRestTemplate(Integer maxConnTotal,
                                                  Integer maxConnPerRoute,
                                                  Integer connectionRequestTimeout,
                                                  Integer readTimeout,
                                                  Integer connectTimeout,
                                                  boolean keepAlive,
                                                  ObjectMapper objectMapper
                                                 ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        var restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(HttpClientFactory.createHttpComponentsClientHttpRequestFactory(maxConnTotal,
                                                                                                      maxConnPerRoute,
                                                                                                      connectionRequestTimeout,
                                                                                                      readTimeout,
                                                                                                      connectTimeout,
                                                                                                      keepAlive));
        restTemplate.setMessageConverters(HttpClientFactory.createDefaultConverts(restTemplate, objectMapper));
        return restTemplate;
    }
}
