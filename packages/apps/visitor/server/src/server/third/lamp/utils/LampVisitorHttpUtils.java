package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * 第三方请求
 */
@Slf4j
@Component
public class LampVisitorHttpUtils {

    public static Object sendThirdGet(String url, Map<String, Object> dataMap, SystemLoginInfoPO loginInfoPO) {
        if (dataMap != null && dataMap.size() > 0) {
            StringBuilder sb = new StringBuilder(url + "?");
            dataMap.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
            url = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return sendThirdGet(url, loginInfoPO);
    }

    public static Object sendThirdGet(String url, SystemLoginInfoPO loginInfoPO) {
        if (loginInfoPO == null) {
            loginInfoPO = new SystemLoginInfoPO();
        }
        HttpRequest request = HttpRequest.get(url);
        request.header("Authorization", "bearer " + loginInfoPO.getAccessToken());
        HttpResponse response = request.execute();
        return getObjectBodyData(response);
    }

    public static Object sendThirdPost(String url, Map<String, Object> dataMap, SystemLoginInfoPO loginInfoPO) {
        return sendThirdPost(url, JSON.toJSONString(dataMap), loginInfoPO);
    }

    public static Object sendThirdPost(String url, String body, SystemLoginInfoPO loginInfoPO) {
        if (loginInfoPO == null) {
            loginInfoPO = new SystemLoginInfoPO();
        }
        HttpResponse response = HttpRequest.post(url)
                .body(body)
                .header("Authorization", "bearer " + loginInfoPO.getAccessToken())
                .contentType("application/json;charset=UTF-8").execute();
        return getObjectBodyData(response);
    }

    public static Object getObjectBodyData(HttpResponse response) {
        if (response == null) {
            return null;
        }
        Object obj = response.body();
        if (obj != null) {
            log.warn("访客管理第三方返回内容：{}", obj);
        }
        if (response.isOk()) {
            JSONObject body = JSON.parseObject(response.body());
            Object status = body.get("success");
            Assert.isUnknownError(!Boolean.TRUE.equals(status), String.valueOf(body.get("errMsg")));
            return body.get("data");
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

    /**
     * 发起get请求
     *
     * @param url     请求地址
     * @param headers 请求头，键为请求头名称，值为实际传入的值（常用来鉴权，没有则传null）
     * @param param   请求参数，键为参数名称，值为实际传入的值（无参数直接传null）
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> param) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            response = httpClient.execute(httpGet);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 发起post请求
     *
     * @param url     请求地址
     * @param headers 请求头，键为请求头名称，值为实际传入的值（常用来鉴权，没有则传null）
     * @param json    传入的参数，类型为json字符串
     *
     * @return
     */
    public static String doPostJson(String url, Map<String, String> headers, Map<String, String> param, String json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            HttpPost httpPost = new HttpPost(uri);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
}
