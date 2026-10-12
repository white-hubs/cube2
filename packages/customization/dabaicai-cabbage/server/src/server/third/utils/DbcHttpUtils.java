package com.guhecloud.cube.srv.gen.cabbage.server.third.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.util.SecureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

/**
 * 大白菜第三方请求
 */
@Slf4j
@Component
public class DbcHttpUtils {
    // 用户标识
    @Value("${dbc-third.login.access-key:#{null}}")
    private String accessKey;
    // 密钥
    @Value("${dbc-third.login.access-secret:#{null}}")
    private String accessSecret;
    // 请求基地址
    @Value("${dbc-third.request.url.base-url:#{null}}")
    private String baseUrl;

    public Object sendDBCPost(String url, Map<String, Object> dataMap) {
        log.warn("大白菜请求地址：{}", baseUrl + url);
        // 签名
        StringBuilder sign = new StringBuilder(accessSecret);
        // 入参
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("accessKey", accessKey);
        map.put("nonce", "random");
        map.put("timestamp", System.currentTimeMillis());
        map.put("data", JSON.toJSON(dataMap));
        /* 处理签名 */
        map.forEach((k, v) -> sign.append(k).append("=").append(v).append("&"));
        sign.deleteCharAt(sign.length() - 1).append(accessSecret);
        // 添加加密的签名参数
        map.put("sign", SecureUtils.md5(sign.toString()).toLowerCase());
        // 发送请求
        HttpResponse response = HttpRequest.post(baseUrl + url).body(JSON.toJSONString(map)).contentType("application/json;charset=UTF-8")
                .execute();
        if (response.isOk()) {
            JSONObject body = JSON.parseObject(response.body());
            log.warn("大白菜请求message：{}", body.get("msg"));
            Object status = body.get("success");
            if (Boolean.TRUE.equals(status)) {
                return body.get("data");
            }
        }
        return null;
    }
}
