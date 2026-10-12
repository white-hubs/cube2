package com.guhecloud.cube.srv.gen.library.management.server.third.lamp.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.library.management.server.po.SystemLoginInfoPO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方请求
 */
@Slf4j
@Component
public class ThirdHttpLibraryUtils {

    private ThirdHttpLibraryUtils() {
    }

    public static Object sendThirdGet(String url, Map<String, Object> dataMap, SystemLoginInfoPO loginInfoPO) {
        return sendThirdGet(url, JSON.toJSONString(dataMap), loginInfoPO);
    }

    public static Object sendThirdGet(String url, String body, SystemLoginInfoPO loginInfoPO) {
        HttpResponse response = HttpRequest.get(url)
                .body(body)
                .header("Authorization", "bearer " + loginInfoPO.getAccessToken())
                .execute();
        return getObjectBodyData(response);
    }

    /**
     * post请求
     *
     * @param url         这里是方法路径
     * @param dataMap     参数
     * @param loginInfoPO 鉴权对象
     *
     * @return 结果
     */
    public static Object sendThirdPost(String url, Map<String, Object> dataMap, SystemLoginInfoPO loginInfoPO) {
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }
        dataMap.put("signature", loginInfoPO.getAccessToken());
        dataMap.put("service", url);
        HttpResponse response = HttpRequest.post(loginInfoPO.getBaseUrl())
                .body(JSON.toJSONString(dataMap))
                .header("Authorization", "bearer " + loginInfoPO.getAccessToken())
                .contentType("application/json;charset=UTF-8").execute();
        return getObjectBodyData(response);
    }

    public static Object getObjectBodyData(HttpResponse response) {
        if (response == null) {
            return null;
        }
        if (response.isOk()) {
            JSONObject body = JSON.parseObject(response.body());
            Object status = body.get("ret");
            if (status != null && Integer.parseInt(String.valueOf(status)) == 200) {
                return body.get("data");
            } else {
                throw new ServiceException(String.valueOf(body.get("msg")));
            }
        }
        log.error("图书管理连接失败！：" + response.toString());

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
