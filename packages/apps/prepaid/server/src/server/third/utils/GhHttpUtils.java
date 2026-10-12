package com.guhecloud.cube.srv.prepaid.server.third.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.prepaid.common.constants.Constants;
import com.guhecloud.platform.base.util.SecureUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GhHttpUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    // 广汇请求基地址
    @Value("${gh-third.request.url.base-url}")
    private String baseUrl;
    // 登录接口
    private final String LOGIN_URL = "/api/Yun/UserLogin";
    // 广汇登录token在redis中的key
    private static final String GH_LOGIN_TOKEN_KEY = "ghLoginTokenKey";
    // 广汇登录账号
    @Value("${gh-third.login.username}")
    private String username;
    // 广汇登录密码
    @Value("${gh-third.login.password}")
    private String password;

    /**
     * 广汇获取token
     * 会重置token过期时间
     *
     * @return token
     */
    public String getGhToken() {
        // 先获取缓存里面的
        Object objToken = redisTemplate.opsForValue().get(GH_LOGIN_TOKEN_KEY);
        if (objToken != null) {
            String token = (String) objToken;
            redisTemplate.opsForValue().set(GH_LOGIN_TOKEN_KEY, token, 1400, TimeUnit.MINUTES);
            return token;
        }

        return loginOn();
    }

    public String loginOn() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("username", username);
        dataMap.put("type", Constants.ONE_INT); // 目前只有1管理员权限（2是普通权限）
        // 加密后的密码
        String md5Password = SecureUtils.md5(password);
        dataMap.put("password", md5Password);
        // 请求
        HttpResponse response = HttpRequest.post(this.baseUrl + LOGIN_URL).body(JSON.toJSONString(dataMap))
                .contentType("application/json;charset=UTF-8").execute();

        if (response.isOk()) { // http成功
            JSONObject bodyResp = JSON.parseObject(response.body());
            Integer status = (Integer) bodyResp.get("status");
            if (status != null && status == 200) { // 返回为成功
                JSONObject data = (JSONObject) bodyResp.get("data");
                String token = (String) data.get("Token");
                redisTemplate.opsForValue().set(GH_LOGIN_TOKEN_KEY, token, 1400, TimeUnit.MINUTES);
                return token;
            }
        }
        return null;
    }

    /**
     * 请求
     *
     * @param methodUrl 地址
     * @param dataMap   参数
     *
     * @return 数据（结果中的data)
     */
    public Object doubleRequestAgain(String methodUrl, Map<String, Object> dataMap) {
        String token = this.getGhToken();
        HttpResponse response = this.requestGetAgan(this.baseUrl + methodUrl, dataMap, token);
        if (response.isOk()) {
            JSONObject body = JSON.parseObject(response.body());
            Integer status = (Integer) body.get("status");
            if (status != null && status == 200) {
                log.info("广汇第一次htt请求成功url：{}。", this.baseUrl + methodUrl);
                return body.get("data");
            } else {
                log.warn("广汇第一次htt请求失败url：{}。result：{}", this.baseUrl + methodUrl, body);
                // 尝试再次获取token请求
                token = loginOn();
                response = this.requestGetAgan(this.baseUrl + methodUrl, dataMap, token);
                if (response.isOk()) {
                    JSONObject bodyTow = JSON.parseObject(response.body());
                    Integer statusTow = (Integer) bodyTow.get("status");
                    if (statusTow != null && statusTow == 200) {
                        return bodyTow.get("data");
                    }
                    log.error("广汇第二次htt请求失败url：{}。result：{}", this.baseUrl + methodUrl, bodyTow);
                }

            }
        }
        log.error("广汇http请求失败url：{}。token：{}。参数：{}。error:{}", token, this.baseUrl + methodUrl, dataMap, response.body());
        return null;
    }

    public HttpResponse requestGetAgan(String url, Map<String, Object> dataMap, String token) {

        // 设置header的token
        HttpRequest request = HttpRequest.get(url)
                .header("token", token);

        // 如果有参数
        if (dataMap != null && !dataMap.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            dataMap.forEach((k, v) -> stringBuilder.append(k).append("=").append(v).append("&"));

            request.body(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        }
        // 请求并返回
        return request.execute();
    }
}
