package com.guhecloud.cube.srv.gen.conferencemanage.server.third.sdk;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class BaseSupport {
    //固定参数
    public static final String API_ID = "program";
    //新增节目
    public static final String INSERT_PROGRAM = "newprogram";
    //删除节目
    public static final String DELETED_PROGRAM = "deleteprogram";
    //发送节目
    public static final String SEND_PROGRAM = "programSend";

    /**
     * 构建通用参数map对象
     */
    public static List<NameValuePair> buildBaseRequestMap(String command, String token) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("accessToken", token));
        params.add(new BasicNameValuePair("apiId", API_ID));
        params.add(new BasicNameValuePair("command", command));
        return params;
    }

    public static String buildRequestUrl(String ip, int port) {
        return "http://" + ip + ":" + port + "/API.aspx";
    }
}
