package com.guhecloud.cube.srv.gen.adapter.onecard.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 11:06
 * @Description:
 */
@Slf4j
public class OneCardHttpClientUtils {

    private static final CloseableHttpClient DEFAULT_HTTP_CLIENT;

    static {
        try {
            // 创建一个不验证主机名的SSLSocketFactory
            var connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
            var sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (chain, authType) -> true)
                    .build();
            connectionManagerBuilder.setTlsSocketStrategy(new DefaultClientTlsStrategy(sslContext, NoopHostnameVerifier.INSTANCE))
                    .setMaxConnTotal(50)
                    .setMaxConnPerRoute(5);

            // 创建自定义的HttpClient
            DEFAULT_HTTP_CLIENT = HttpClients.custom()
                    .setConnectionManager(connectionManagerBuilder.build())
                    .evictExpiredConnections()  // 自动清理过期连接
                    .evictIdleConnections(TimeValue.of(60, TimeUnit.SECONDS)) // 清理空闲连接
                    .build();

            // 添加JVM关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    DEFAULT_HTTP_CLIENT.close();
                    log.info("HttpClient已关闭");
                } catch (IOException e) {
                    log.error("关闭HttpClient失败", e);
                }
            }));

        } catch (Exception e) {
            throw new RuntimeException("初始化HttpClient失败", e);
        }
    }

    /**
     * 发起get请求
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, Object> param) {
        String resultString = "";
        try (CloseableHttpResponse response = executeGet(url, headers, param)) {
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.error("GET请求失败: url={}, headers={}, param={}", url, headers, param, e);
            throw new RuntimeException("HTTP请求失败", e);
        }
        return resultString;
    }

    /**
     * 发起post请求
     */
    public static String doPostJson(String url, Map<String, String> headers, String json) {
        String resultString = "";
        try (CloseableHttpResponse response = executePost(url, headers, json)) {
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error("POST请求失败: url={}, headers={}, json={}", url, headers, json, e);
            throw new RuntimeException("HTTP请求失败", e);
        }
        return resultString;
    }

    /**
     * 执行GET请求（分离出来便于异常处理）
     */
    private static CloseableHttpResponse executeGet(String url, Map<String, String> headers,
                                                    Map<String, Object> param
                                                   ) throws Exception {
        URIBuilder builder = new URIBuilder(url);
        if (param != null) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                builder.addParameter(entry.getKey(),
                                     entry.getValue() != null ? entry.getValue().toString() : "");
            }
        }

        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return DEFAULT_HTTP_CLIENT.execute(httpGet);
    }

    /**
     * 执行POST请求（分离出来便于异常处理）
     */
    private static CloseableHttpResponse executePost(String url, Map<String, String> headers,
                                                     String json
                                                    ) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        return DEFAULT_HTTP_CLIENT.execute(httpPost);
    }
}
