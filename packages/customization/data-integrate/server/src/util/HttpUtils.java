package com.guhecloud.cube.base.dataintegrate.server.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.server.config.SSL;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class HttpUtils {

    //获取resttemplate
    public static RestTemplate getRestTemplate() {
        var factory = new SSL();
        factory.setReadTimeout(60000);
        factory.setConnectTimeout(30000);//单位为ms
        return new RestTemplate(factory);
    }

    public static <T> T restGet(String url, MultiValueMap<String, String> params, Map<String, String> authMap, TypeReference<T> type) {
        if (!StringUtils.isNotBlank(url)) {
            throw new RuntimeException("请求的路径不能为空");
        }
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null != authMap && !authMap.isEmpty()) {
            authMap.forEach(headers::set);
        }
        var request = new HttpEntity<>(null, headers);
        //get请求拼接的参数
        var builder = UriComponentsBuilder.fromUriString(url);
        var uri = builder.queryParams(params).build().toUri();
        try {
            var responseEntity = getRestTemplate().exchange(URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8), HttpMethod.GET, request, String.class);
            if (responseEntity.getStatusCode().value() != 200) {
                throw new RuntimeException("远程调用失败，code=" + responseEntity.getStatusCode().value());
            }
            return getResponse(type, responseEntity);
        } catch (Exception e) {
            throw new RuntimeException("远程调用失败", e);
        }
    }

    public static <T> T restPost(String url, Object params, Map<String, String> authMap, MediaType mediaType, TypeReference<T> type) {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("请求的路径不能为空");
        }
        var headers = new HttpHeaders();
        headers.setContentType(ObjectUtils.getOrElse(ObjectUtils::isNotNull, mediaType, MediaType.APPLICATION_JSON));
        if (null != authMap && !authMap.isEmpty()) {
            authMap.forEach(headers::set);
        }
        var request = new HttpEntity<>(params, headers);
        try {
            var postForEntity = getRestTemplate().postForEntity(url, request, String.class);
            if (postForEntity.getStatusCode().value() != 200) {
                throw new RuntimeException("远程调用失败");
            }
            return getResponse(type, postForEntity);
        } catch (Exception e) {
            throw new RuntimeException("远程调用失败", e);
        }
    }

    private static <T> T getResponse(TypeReference<T> type, ResponseEntity<String> postForEntity) {
        var body = JsonUtils.deserialize(postForEntity.getBody());
        if (body != null && Objects.nonNull(body.get("data"))) {
            return JsonUtils.deserialize(body.get("data"), type);
        }
        return JsonUtils.deserialize(postForEntity.getBody(), type);
    }
}
