package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.SystemLoginInfoMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampLoginService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils.LampVisitorHttpUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils.RSAUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.ThirdTokenVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 留灯书屋
 * 登录
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LampLoginServiceImpl implements LampLoginService {
    private final SystemLoginInfoMapper systemLoginInfoMapper;

    private static final String LAMP_SCENE_CODE = "lamp-visitor";
    private static final String PUBLIC_KEY_URL = "/evo-apigw/evo-oauth/1.0.0/oauth/public-key";
    private static final String TOKEN_URL = "/evo-apigw/evo-oauth/1.0.0/oauth/extend/token";

    /**
     * 获取公钥
     *
     * @return 公钥
     */
    private String getPublicKey(String baseUrl) {
        HttpResponse response = HttpRequest.get(baseUrl + PUBLIC_KEY_URL).contentType("application/json;charset=UTF-8").execute();
        Object obj = LampVisitorHttpUtils.getObjectBodyData(response);
        if (ObjectUtils.isEmpty(obj)) {
            return null;
        }
        return ((JSONObject) obj).get("publicKey").toString();
    }

    private ThirdTokenVO getToken(String username, String password, String clientId, String clientSecret, String baseUrl) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("grant_type", "password");
        dataMap.put("username", username);
        String publicKey = this.getPublicKey(baseUrl);
        dataMap.put("password", RSAUtils.RSAPublicKey(password, publicKey));
        dataMap.put("client_id", clientId);
        dataMap.put("client_secret", clientSecret);
        dataMap.put("public_key", publicKey);
        Object obj = LampVisitorHttpUtils.sendThirdPost(baseUrl + TOKEN_URL, dataMap, null);
        Assert.isNotAcceptable(obj == null, "鉴权连接失败！");
        JSONObject data = (JSONObject) obj;
        return data.toJavaObject(ThirdTokenVO.class);
    }

    /**
     * 登录（过期自动刷新token）
     *
     * @return 登录信息
     */
    @Override
    public SystemLoginInfoPO loginToken() {
        SystemLoginInfoPO loginInfoPO = systemLoginInfoMapper.selectOne(Wrappers.<SystemLoginInfoPO>lambdaQuery()
                                                                                .eq(SystemLoginInfoPO::getSceneCode, LAMP_SCENE_CODE)
                                                                                .last(" limit 1 ")
                                                                       );
        Assert.isParameterError(loginInfoPO == null, "授权信息缺失！");
        SessionContextHolder.requestTenant(loginInfoPO.getTenant());
        // token没有过期时间
        boolean notExpiresBool = loginInfoPO.getAccessToken() != null && loginInfoPO.getTokenExpires() == null;
        // token未过期
        boolean effectiveTimeBool = loginInfoPO.getAccessToken() != null && loginInfoPO.getTokenExpires() != null &&
                                    loginInfoPO.getTokenExpires().isAfter(LocalDateTime.now());
        if (notExpiresBool || effectiveTimeBool) {
            return loginInfoPO;
        }
        ThirdTokenVO thirdTokenVO = this.getToken(loginInfoPO.getUsername(), loginInfoPO.getPassword(), loginInfoPO.getClientId(), loginInfoPO.getClientSecret(), loginInfoPO.getBaseUrl());
        Assert.isNotAcceptable(thirdTokenVO == null || StringUtils.isBlank(thirdTokenVO.getAccessToken()), "授权失败！");
        loginInfoPO.setAccessToken(thirdTokenVO.getAccessToken());
        loginInfoPO.setTokenExpires(DateUtils.parse(System.currentTimeMillis() + ((thirdTokenVO.getExpiresIn() - 30) * 1000)));
        loginInfoPO.setRefreshToken(thirdTokenVO.getRefreshToken());
        loginInfoPO.setUserId(thirdTokenVO.getUserId());
        systemLoginInfoMapper.updateById(loginInfoPO);
        return loginInfoPO;
    }

}
