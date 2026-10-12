package com.guhecloud.cube.base.flow.form.util;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.platform.base.model.ErrorResponse;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: http请求工具类
 * @author: bq
 * @create: 2022-09-26 11:36
 **/
@Slf4j
public class HttpClientUtils {

    /**
     * get请求
     *
     * @param url
     * @param params
     * @param authMap
     *
     * @return
     */
    public static Map<String, Object> restGet(String url, MultiValueMap<String, String> params, Map<String, String> authMap) {
        if (!StringUtils.isNotBlank(url)) {
            throw new RuntimeException("请求的路径不能为空");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null != authMap && authMap.size() > 0) {
            authMap.forEach(headers::set);
        }
        org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(null, headers);
        //get请求拼接的参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.queryParams(params).build().toUri();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(6000);//设置超时时间           l
        requestFactory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(URLDecoder.decode(uri.toString()), HttpMethod.GET, request, String.class);
            if (responseEntity.getStatusCodeValue() != 200) {
                throw new RuntimeException("远程调用失败，code=" + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("远程调用失败", e);
            throw new RuntimeException("远程调用失败", e);
        }
        return JsonUtils.deserialize(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {});
    }

    /**
     * RestTemplate 发送post请求
     *
     * @param params
     * @param url
     *
     * @return
     */
    public static Map<String, Object> restPost(String url, Object params, Map<String, String> authMap) {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("请求的路径不能为空");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null != authMap && authMap.size() > 0) {
            authMap.forEach(headers::set);
        }
        String jsonStr = JsonUtils.serialize(params);
        org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(jsonStr, headers);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);//设置超时时间
        requestFactory.setReadTimeout(180000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> postForEntity = null;
        try {
            postForEntity = restTemplate.postForEntity(url, request, String.class);
            if (postForEntity.getStatusCodeValue() != 200) {
                throw new RuntimeException("远程调用失败");
            }
        } catch (Exception e) {
            String message = e.getMessage();
            log.info("预提交响应异常信息：{}", message);
            if (message.contains("message")) {
                message = message.substring(5);
                var results = JsonUtils.deserializeToList(message, ErrorResponse.class);
                throw new ServiceException(results.getFirst().message());
            }
            Assert.notNull(postForEntity, "预提交响应为空");
            String body = postForEntity.getBody();
            Assert.notNull(body, "预提交响应为空");
            var result = JsonUtils.deserialize(body, ErrorResponse.class);
            throw new ServiceException(result.message());
        }
        return JsonUtils.deserialize(postForEntity.getBody(), new TypeReference<Map<String, Object>>() {});
    }

    /**
     * @Author wql
     * @Description 发送post请求
     * @Date 13:48 2023/5/25
     **/
    public static Map<String, Object> restPostWithParameter(String url, Object params, Map<String, String> authMap) {
        if (!StringUtils.isNotBlank(url)) {
            throw new RuntimeException("请求的路径不能为空");
        }
        HttpResponse response = null;
        try {
            response = HttpUtil.createPost(url)
                    .setConnectionTimeout(10000)
                    .setReadTimeout(180000)
                    .contentType(ContentType.MULTIPART.getValue())
                    .addHeaders(authMap)
                    .form("prefix", params)
                    .execute();
            if (response.getStatus() != 200) {
                throw new RuntimeException("远程调用失败");
            }
        } catch (Exception e) {
            // 哎，为了先兼容没办法，忍者吧
            String message = e.getMessage();
            log.info("预提交响应异常信息：{}", message);
            if (message.contains("message")) {
                message = message.substring(5);
                var results = JsonUtils.deserializeToList(message, ErrorResponse.class);
                throw new ServiceException(results.getFirst().message());
            }
            Assert.notNull(response, "预提交响应为空");
            String body = response.body();
            Assert.notNull(body, "预提交响应为空");
            var result = JsonUtils.deserialize(body, ErrorResponse.class);
            throw new ServiceException(result.message());
        }
        if (response.body().contains("data")) {
            return JsonUtils.deserialize(response.body(), new TypeReference<Map<String, Object>>() {});
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("data", JsonUtils.deserialize(response.body(), Object.class));
            return result;
        }
    }

}
