package com.guhecloud.cube.base.cloud.qr.code.sdk.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.cloud.qr.code.sdk.config.CloudQrCodeSdkConfig;
import com.guhecloud.cube.base.cloud.qr.code.sdk.exception.CloudQrCodeSdkException;
import com.guhecloud.platform.base.standard.jackson.ObjectMapperConfig;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP客户端
 * <p>
 * 兼容SSL（https）和非SSL（http）场景
 *
 * @author system
 */
public class HttpClient {

    private final CloudQrCodeSdkConfig config;
    private final java.net.http.HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String accessToken;
    private long tokenExpireTime;

    public HttpClient(CloudQrCodeSdkConfig config) {
        this.config = config;
        this.objectMapper = ObjectMapperConfig.getDefaultObjectMapper();
        this.httpClient = createHttpClient(config);
    }

    /**
     * 创建HttpClient实例
     * <p>
     * 根据serverUrl判断是否使用SSL：
     * - https开头：使用SSL配置（信任所有证书，适用于自签名证书）
     * - http开头：不使用SSL
     */
    private java.net.http.HttpClient createHttpClient(CloudQrCodeSdkConfig config) {
        java.net.http.HttpClient.Builder builder = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(config.getConnectTimeout()));

        // 如果是https，配置SSL上下文以支持所有证书（包括自签名证书）
        if (config.getServerUrl() != null && config.getServerUrl().startsWith("https://")) {
            try {
                // 创建信任所有证书的TrustManager
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }

                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }

                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };

                // 安装信任所有证书的TrustManager
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                builder.sslContext(sslContext);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                // SSL配置失败，使用默认配置
                throw new CloudQrCodeSdkException("SSL_CONFIG_ERROR", "SSL配置失败", e);
            }
        }

        return builder.build();
    }

    /**
     * 获取访问令牌
     */
    private synchronized String getAccessToken() {
        // 如果token未过期，直接返回
        if (accessToken != null && System.currentTimeMillis() < tokenExpireTime) {
            return accessToken;
        }

        try {
            // OAuth2 客户端模式认证 - client信息放在请求体中
            Map<String, String> formData = new HashMap<>();
            formData.put("grant_type", "client_credentials");
            formData.put("client_id", config.getAppId());
            formData.put("client_secret", config.getAppSecret());

            String body = formData.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .reduce((a, b) -> a + "&" + b)
                    .orElse("");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + "/ouaa/oauth2/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> result = objectMapper.readValue(response.body(), new TypeReference<>() {
                });
                this.accessToken = (String) result.get("access_token");
                Integer expiresIn = (Integer) result.get("expires_in");
                // 提前5分钟过期
                this.tokenExpireTime = System.currentTimeMillis() + (expiresIn - 300) * 1000L;
                return this.accessToken;
            } else {
                throw new CloudQrCodeSdkException("AUTH_FAILED", "获取访问令牌失败: " + response.body());
            }
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("AUTH_ERROR", "认证过程发生错误", e);
        }
    }

    /**
     * GET请求
     */
    public <T> T get(String path, Class<T> responseType) {
        return get(path, null, responseType);
    }

    /**
     * GET请求（带查询参数）
     */
    public <T> T get(String path, Map<String, String> queryParams, Class<T> responseType) {
        try {
            String url = config.getServerUrl() + path;
            if (queryParams != null && !queryParams.isEmpty()) {
                String query = queryParams.entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .reduce((a, b) -> a + "&" + b)
                        .orElse("");
                url += "?" + query;
            }

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .GET();

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return handleResponse(response, responseType);
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("REQUEST_ERROR", "GET请求失败: " + path, e);
        }
    }

    /**
     * POST请求
     */
    public <T> T post(String path, Object body, Class<T> responseType) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + path))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return handleResponse(response, responseType);
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("REQUEST_ERROR", "POST请求失败: " + path, e);
        }
    }

    /**
     * 泛型POST请求
     */
    public <T> T post(String path, Object body, TypeReference<T> typeReference) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + path))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), typeReference);
            } else if (response.statusCode() == 401) {
                this.accessToken = null;
                throw new CloudQrCodeSdkException("AUTH_EXPIRED", "访问令牌已过期");
            } else {
                throw new CloudQrCodeSdkException("HTTP_" + response.statusCode(), "请求失败: " + response.body());
            }
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("REQUEST_ERROR", "POST请求失败: " + path, e);
        }
    }

    /**
     * PUT请求
     */
    public <T> T put(String path, Object body, Class<T> responseType) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + path))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return handleResponse(response, responseType);
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("REQUEST_ERROR", "PUT请求失败: " + path, e);
        }
    }

    /**
     * 泛型PUT请求
     */
    public <T> T put(String path, Object body, TypeReference<T> typeReference) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + path))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), typeReference);
            } else if (response.statusCode() == 401) {
                this.accessToken = null;
                throw new CloudQrCodeSdkException("AUTH_EXPIRED", "访问令牌已过期");
            } else {
                throw new CloudQrCodeSdkException("HTTP_" + response.statusCode(), "请求失败: " + response.body());
            }
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("REQUEST_ERROR", "PUT请求失败: " + path, e);
        }
    }

    /**
     * DELETE请求
     */
    public void delete(String path) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + path))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .DELETE();

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new CloudQrCodeSdkException("REQUEST_ERROR", "DELETE请求失败: " + response.body());
            }
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("REQUEST_ERROR", "DELETE请求失败: " + path, e);
        }
    }

    /**
     * DELETE请求（带请求体）
     */
    public void delete(String path, Object body) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(config.getServerUrl() + path))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new CloudQrCodeSdkException("REQUEST_ERROR", "DELETE请求失败: " + response.body());
            }
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("REQUEST_ERROR", "DELETE请求失败: " + path, e);
        }
    }

    /**
     * 处理响应
     */
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) throws IOException {
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            if (responseType == Void.class || responseType == void.class) {
                return null;
            }
            return objectMapper.readValue(response.body(), responseType);
        } else if (response.statusCode() == 401) {
            // token过期，清空并重试一次
            this.accessToken = null;
            throw new CloudQrCodeSdkException("AUTH_EXPIRED", "访问令牌已过期");
        } else {
            throw new CloudQrCodeSdkException("HTTP_" + response.statusCode(), "请求失败: " + response.body());
        }
    }

    /**
     * 泛型GET请求
     */
    public <T> T get(String path, TypeReference<T> typeReference) {
        return get(path, null, typeReference);
    }

    /**
     * 泛型GET请求（带查询参数）
     */
    public <T> T get(String path, Map<String, String> queryParams, TypeReference<T> typeReference) {
        try {
            String url = config.getServerUrl() + path;
            if (queryParams != null && !queryParams.isEmpty()) {
                String query = queryParams.entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .reduce((a, b) -> a + "&" + b)
                        .orElse("");
                url += "?" + query;
            }

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .GET();

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), typeReference);
            } else if (response.statusCode() == 401) {
                this.accessToken = null;
                throw new CloudQrCodeSdkException("AUTH_EXPIRED", "访问令牌已过期");
            } else {
                throw new CloudQrCodeSdkException("HTTP_" + response.statusCode(), "请求失败: " + response.body());
            }
        } catch (Exception e) {
            throw new CloudQrCodeSdkException("REQUEST_ERROR", "GET请求失败: " + path, e);
        }
    }
}
