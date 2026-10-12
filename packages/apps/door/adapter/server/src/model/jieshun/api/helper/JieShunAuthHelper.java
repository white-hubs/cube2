package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.helper;

import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.config.JieShunConfig;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.response.JieShunResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.response.SignData;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.JieShunResponseCodeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.ManufacturerEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * author: ht
 * create: 2025-05-22 11:52
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JieShunAuthHelper {
    private final static String SIGN_URI = "/internal/sign";
    private final RestTemplate restTemplate;
    private final ManufacturerConfigHelper manufacturerConfigHelper;

    public Map<String, String> getRequiredHeaders() {
        Map<String, String> headers = new HashMap<>();
        //获取uuid，用于random
        String random = UUID.randomUUID().toString();
        //获取时间戳，用于timestamp
        long curSeconds = Instant.now().getEpochSecond();
        //获取签名数据
        SignData signData = signDataRequest();
        //获取appid，用于生成头部参数
        String appId = signData.getAppId();
        //获取key，用于生成sign
        String key = signData.getKey();
        //获取sign，用于头部参数
        String sign = generateMd5Sign(random, curSeconds, key);

        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("appId", appId);
        headers.put("v", "1.0");

        headers.put("random", random);
        headers.put("timestamp", String.valueOf(curSeconds));
        headers.put("sign", sign);
        return headers;
    }

    private String generateMd5Sign(String random, long curSeconds, String key) {
        //使用md5加密
        return md5Encrypt("random" + random + "timestamp" + curSeconds + "key" + key
                          //使用md5加密
                         ).toUpperCase();
    }

    // 实现MD5加密
    private String md5Encrypt(String input) {
        try {
            // 获取MD5算法的MessageDigest实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 更新数据到摘要
            md.update(input.getBytes());

            // 完成哈希计算并获取字节数组
            byte[] digest = md.digest();

            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString(); // 返回MD5加密后的字符串
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密过程中发生错误", e);
        }
    }

    private SignData signDataRequest() {

        //获取厂商配置
        JieShunConfig jieShunConfig = manufacturerConfigHelper.getManufacturerConfig(ManufacturerEnum.JIE_SHUN, JieShunConfig.class);
        //用于访问三方的域名
        String host = jieShunConfig.getHost();
        //用户名，用于访问获取key的请求参数
        String username = jieShunConfig.getUsername();
        //密码，用于访问获取key的请求参数
        String password = jieShunConfig.getPassword();
        //设置请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        log.info("【捷顺】获取签名key，请求体URL：{}", host + SIGN_URI);
        log.info("【捷顺】获取签名key，请求头参数：{}", httpHeaders);
        log.info("【捷顺】获取签名key，请求参数：{}", requestBody);
        //访问三方厂家获取签名数据
        try {
            //访问三方厂家获取签名数据
            ResponseEntity<JieShunResponse<List<SignData>>> response = restTemplate.exchange(
                    host + SIGN_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<JieShunResponse<List<SignData>>>() {});
            //成功返回签名数据，失败抛出异常
            log.info("【捷顺】获取签名key，http请求成功");
            JieShunResponse<List<SignData>> jieShunResponse = Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new ServiceException("【捷顺】获取签名key为返回响应体"));
            log.info("【捷顺】获取签名key，响应内容:{}", jieShunResponse);
            //验证业务是否获取成功
            String code = jieShunResponse.getCode();
            Assert.isUnknownError(!JieShunResponseCodeEnum.SUCCESS.eql(code), "【捷顺】获取签名key，业务请求失败,失败码【%s】，失败原因【%s】", jieShunResponse.getCode(), jieShunResponse.getMessage());
            List<SignData> signDataList = jieShunResponse.getData();
            //验证是否为空
            Assert.isNotAcceptable(ListUtils.isEmpty(signDataList), "【捷顺】获取签名key为返回data数据为空");
            log.info("【捷顺】获取签名key，业务请求成功");
            return signDataList.getFirst();
        } catch (RestClientException e) {
            throw new ServiceException("【捷顺】获取签名key，http请求失败,失败原因：" + e.getMessage());
        }
    }

}
