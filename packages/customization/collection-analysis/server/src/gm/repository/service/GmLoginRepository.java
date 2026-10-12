package com.guhecloud.cube.biz.collection.analysis.gm.repository.service;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.GmLoginInfoDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.GmLoginTokenDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.utils.gmhttp.GmCenterHttpConnectUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 光明中台登录
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GmLoginRepository {
    private final DataDictionaryClient dataDictionaryClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String GM_CENTER_TOKEN_INFO_KEY = "GM_CENTER_TOKEN_INFO_KEY";
    private static final String GM_CENTER_ACCOUNT = "GM_CENTER_ACCOUNT";
    private static final String LOGIN_URL = "/auth/login";

    public String getToken() {
        return this.login().getAccessToken();
    }

    public String getBaseUrl() {
        return this.login().getBaseUrl();
    }

    public GmLoginTokenDTO login() {
        Object obj = redisTemplate.opsForValue().get(GM_CENTER_TOKEN_INFO_KEY);
        if (obj != null) {
            return (GmLoginTokenDTO) obj;
        }
        DataDictionaryaramParam param = new DataDictionaryaramParam();
        param.setItemCode(GM_CENTER_ACCOUNT);
        param.setCode(GM_CENTER_ACCOUNT);
        List<DataDictionaryDTO> dictionaryList = dataDictionaryClient.getDataDictionaryByItem(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(dictionaryList), "请配光明数据中台的登录信息！");
        DataDictionaryDTO dto = dictionaryList.get(0);
        Assert.isNotAcceptable(dto.getVariable() == null, "请配光明数据中台的登录信息！");
        GmLoginInfoDTO loginInfo = JSON.parseObject(dto.getVariable(), GmLoginInfoDTO.class);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", 0);
        paramMap.put("username", loginInfo.getUsername());
        paramMap.put("password", loginInfo.getPassword()/*this.getMd5String(loginInfo.getPassword())*/);
        GmLoginTokenDTO tokenDTO = new GmCenterHttpConnectUtils().url(loginInfo.getBaseUrl() + LOGIN_URL).paramMap(paramMap).sendThirdPost()
                .toJavaBean(GmLoginTokenDTO.class);
        tokenDTO.setBaseUrl(loginInfo.getBaseUrl());
        redisTemplate.opsForValue().set(GM_CENTER_TOKEN_INFO_KEY, tokenDTO, tokenDTO.getExpiresIn() - 30, TimeUnit.SECONDS);
        return tokenDTO;
    }

    private String getMd5String(String str) {
        try {
            // 获取MD5算法的MessageDigest实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 更新摘要以包含输入字符串的字节
            md.update(str.getBytes());

            // 完成哈希计算并获取结果
            byte[] digest = md.digest();

            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("光明密码加密失败：" + e.getMessage());
        }
    }
}
