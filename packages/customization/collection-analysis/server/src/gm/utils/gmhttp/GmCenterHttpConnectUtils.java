package com.guhecloud.cube.biz.collection.analysis.gm.utils.gmhttp;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.NumberUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 光明中台请求
 */
@Slf4j
@Component
public class GmCenterHttpConnectUtils {

    private String url;
    private String token;
    private Map<String, Object> paramMap;
    private Object resultObject;

    public GmCenterHttpConnectUtils url(String url) {
        this.url = url;
        return this;
    }

    public GmCenterHttpConnectUtils token(String token) {
        this.token = token;
        return this;
    }

    public GmCenterHttpConnectUtils paramMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
        return this;
    }

    public GmCenterHttpConnectUtils sendThirdGet(String url, String token) {
        return sendThirdGet(url, null, token);
    }

    public GmCenterHttpConnectUtils sendThirdGet(String url, Map<String, Object> paramMap, String token) {
        this.url = url;
        this.paramMap = paramMap;
        this.token = token;
        return sendThirdGet();
    }

    public GmCenterHttpConnectUtils sendThirdGet() {
        if (paramMap != null && paramMap.size() > 0) {
            StringBuilder sb = new StringBuilder(url + "?");
            paramMap.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
            url = sb.deleteCharAt(sb.length() - 1).toString();
        }
        HttpRequest request = HttpRequest.get(url);
        if (StringUtils.isNotBlank(token)) {
            request = request.header("Authorization", token);
        }
        HttpResponse response = request.execute();
        return getObjectBodyData(response);
    }

    public GmCenterHttpConnectUtils sendThirdPost(String url, Map<String, Object> dataMap, String token) {
        this.url = url;
        this.paramMap = dataMap;
        this.token = token;
        return sendThirdPost();
    }

    public GmCenterHttpConnectUtils sendThirdPost() {
        HttpRequest request = HttpRequest.post(url)
                .body(JSON.toJSONString(paramMap))
                .contentType("application/json;charset=UTF-8");
        if (StringUtils.isNotBlank(token)) {
            request = request.header("Authorization", token);
        }
        HttpResponse response = request.execute();
        return getObjectBodyData(response);
    }

    public <T> Pagination<T> toPage(Class<T> clazz) {
        JSONObject jsonObject = (JSONObject) resultObject;
        if (jsonObject == null || jsonObject.size() == 0) {
            return Pagination.empty();
        }
        long total = 0L;
        long pages;
        Object totalObj = jsonObject.get("total");
        if (totalObj != null) {
            total = Long.parseLong(String.valueOf(totalObj));
        }

        long pageSize = 10L;
        Object pageSizeObj = jsonObject.get("pageSize");
        if (pageSizeObj != null) {
            pageSize = Long.parseLong(String.valueOf(pageSizeObj));
        }

        if (pageSize <= 0L) {
            pages = 0L;
        } else {
            pages = (long) (Math.ceil((double) total / pageSize));
        }

        List<T> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) jsonObject.get("rows");
        if (jsonArray != null && !jsonArray.isEmpty()) {
            List<T> arrList = jsonArray.toJavaList(clazz);
            if (arrList != null && !arrList.isEmpty()) {
                list = arrList;
            }
        }
        return new Pagination<>(total, pages, list);
    }

    public <T> List<T> toList(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (resultObject == null) {
            return list;
        }
        return ((JSONArray) resultObject).toJavaList(clazz);
    }

    public <T> T toJavaBean(Class<T> clazz) {
        if (resultObject == null) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return ((JSONObject) resultObject).toJavaObject(clazz);
    }

    private GmCenterHttpConnectUtils getObjectBodyData(HttpResponse response) {
        if (response == null) {
            return this;
        }
        Object obj = response.body();
        if (obj != null) {
            log.warn("光明中台第三方返回内容：{}", obj);
        }
        if (response.isOk()) {
            JSONObject body = JSON.parseObject(response.body());
            Object status = body.get("code");
            String codeStr = String.valueOf(status);
            Assert.isUnknownError(NumberUtils.isNumber(codeStr) && 200 != Integer.parseInt(codeStr), String.valueOf(body.get("msg")));
            this.resultObject = body.get("data");
        }
        return this;
    }
}
