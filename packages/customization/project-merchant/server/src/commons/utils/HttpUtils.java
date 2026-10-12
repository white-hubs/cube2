package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @program: iops
 * @description: http请求工具类
 * @author: hyq
 * @create: 2020-09-08 11:36
 **/
@Slf4j
public class HttpUtils {


    /**
     * get请求
     *
     * @param url
     * @param params
     * @param authMap
     * @return
     */
    public static JSONObject restGet(String url, MultiValueMap<String, String> params, Map<String, String> authMap) {
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
        return JSONObject.parseObject(responseEntity.getBody());
    }


    /**
     * RestTemplate 发送post请求
     *
     * @param params
     * @param url
     * @return
     */
    public static JSONObject restPost(String url, Object params, Map<String, String> authMap) {
        if (!StringUtils.isNotBlank(url)) {
            throw new RuntimeException("请求的路径不能为空");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null != authMap && authMap.size() > 0) {
            authMap.forEach(headers::set);
        }
        String jsonStr = JSON.toJSONString(params);
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
            log.error("远程调用失败", e);
            throw new RuntimeException("远程调用失败");
        }
        String body = postForEntity.getBody();
        return JSONObject.parseObject(body);
    }


}
