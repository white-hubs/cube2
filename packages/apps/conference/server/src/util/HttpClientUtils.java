package com.guhecloud.cube.srv.gen.conferencemanage.server.util;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/24 11:18
 * @Description: http请求工具类
 */
public class HttpClientUtils {

    /**
     * 获取接口路径前缀
     */
    public static String getUrlPrefix(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme(); // 获取协议前缀，例如http、https
            return scheme + "://" + uri.getAuthority();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
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
    public static String doPostJson(String url, Map<String, String> headers, String json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            HttpPost httpPost = new HttpPost(url);
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

    /**
     * 发起post请求
     *
     * @param url     请求地址
     * @param headers 请求头，键为请求头名称，值为实际传入的值（常用来鉴权，没有则传null）
     * @param params  传入的参数，类型为json字符串
     *
     * @return
     */
    public static String doPostForm(String url, Map<String, String> headers, List<NameValuePair> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            StringEntity formEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
            httpPost.setEntity(formEntity);
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
