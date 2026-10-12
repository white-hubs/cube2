package com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 第三方请求
 */
@Slf4j
@Component
public class InteVisitorHttpUtils {

    public static Object sendThirdGet(String url, Map<String, Object> dataMap, SystemLoginInfoPO loginInfoPO) {
        return sendThirdGet(url, JSON.toJSONString(dataMap), loginInfoPO);
    }

    public static Object sendThirdGet(String url, String body, SystemLoginInfoPO loginInfoPO) {
        if (loginInfoPO == null) {
            loginInfoPO = new SystemLoginInfoPO();
        }
        HttpResponse response;
        try {
            response = HttpRequest.get(url)
                    .body(body)
                    .header("auth-token", loginInfoPO.getAccessToken())
                    .header("Accept-Language", "zh-CN")
                    .execute();
        } catch (Exception e) {
            log.error("三方请求出错" + url + "。body:" + body + "。错误信息：" + e);
            return null;
        }

        return getObjectBodyData(response);
    }

    public static Object sendThirdPost(String url, Map<String, Object> dataMap, SystemLoginInfoPO loginInfoPO) {
        return sendThirdPost(url, JSON.toJSONString(dataMap), loginInfoPO);
    }

    public static Object sendThirdPost(String url, String body, SystemLoginInfoPO loginInfoPO) {
        if (loginInfoPO == null) {
            loginInfoPO = new SystemLoginInfoPO();
        }
        log.warn(url + "：" + body + "：" + JSON.toJSONString(loginInfoPO));
        HttpResponse response;
        try {
            response = HttpRequest.post(url)
                    .body(body)
                    .header("auth-token", loginInfoPO.getAccessToken())
                    .header("Accept-Language", "zh-CN")
                    .contentType("application/json;charset=UTF-8").execute();
        } catch (Exception e) {
            log.error("三方请求出错" + url + "。body:" + body + "。错误信息：" + e);
            return null;
        }
        return getObjectBodyData(response);
    }

    public static Object getObjectBodyData(HttpResponse response) {
        if (response == null) {
            return null;
        }
        if (response.isOk()) {
            JSONObject body = JSON.parseObject(response.body());
            Object code = body.get("code");
            if ("0".equals(code)) {
                return body.get("data");
            } else {
                throw new ServiceException(String.valueOf(body.get("message")));
            }
        }
        return null;
    }

    public static JSONObject getJSONObjectBodyData(HttpResponse response) {
        Object obj = getObjectBodyData(response);
        if (ObjectUtils.isNotEmpty(obj)) {
            return (JSONObject) obj;
        }
        return new JSONObject();
    }

    public static JSONArray getJSONArrayBodyData(HttpResponse response) {
        Object obj = getObjectBodyData(response);
        if (ObjectUtils.isNotEmpty(obj)) {
            return (JSONArray) obj;
        }
        return new JSONArray();
    }
}
