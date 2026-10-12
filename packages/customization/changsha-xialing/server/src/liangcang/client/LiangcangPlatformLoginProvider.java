package com.guhecloud.cube.customization.xialing.server.liangcang.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.LiangcangSystemDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangPlatformLoginCredentialDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLoginInfoVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** 调用平台账号密码登录接口获取 token 和租户信息。 */
@Component
@RequiredArgsConstructor
public class LiangcangPlatformLoginProvider {

    private static final String GRANT_TYPE_PASSWORD = "pwd";
    private static final String GRANT_TYPE = "grant_type";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TENANT = "tenant";
    private static final String TERMINAL = "terminal";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String TOKEN_TYPE = "token_type";
    private static final String EXPIRES_IN = "expires_in";
    private static final String TENANTS = "tenants";

    private final RestTemplate restTemplate;
    private final SysDictionaryClient sysDictionaryClient;

    public WarehouseLoginInfoVO loginInfo() {
        LiangcangPlatformLoginCredentialDTO credential = loadCredential();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE, GRANT_TYPE_PASSWORD);
        body.add(USERNAME, resolveUsername(credential));
        body.add(PASSWORD, credential.getPassword().trim());
        if (credential.getTenant() != null) {
            body.add(TENANT, String.valueOf(credential.getTenant()));
        }
        if (StringUtils.isNotBlank(credential.getTerminal())) {
            body.add(TERMINAL, credential.getTerminal().trim());
        }

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        Map<String, Object> response;
        try {
            var resp = restTemplate.postForEntity(resolveRequestUrl(credential), new HttpEntity<>(body, headers), Object.class);
            Assert.isParameterError(!(resp.getStatusCode().is2xxSuccessful()), "平台登录接口非成功响应: " + resp.getStatusCode());
            response = JsonUtils.deserialize(resp.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            ServiceException ex = new ServiceException("平台账号密码登录失败，请检查系统字典账号密码与 OAuth 客户端配置");
            ex.initCause(e);
            throw ex;
        }
        Assert.isParameterError(response == null || !response.containsKey(ACCESS_TOKEN), "平台登录未返回 access_token");
        return toLoginInfo(response, credential);
    }

    private LiangcangPlatformLoginCredentialDTO loadCredential() {
        var dict = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                                                                         LiangcangSystemDictionary.CODE_PLATFORM_LOGIN);
        Assert.isParameterError(dict == null || StringUtils.isBlank(dict.getVariable()), "系统字典未配置粮仓平台登录信息");
        try {
            LiangcangPlatformLoginCredentialDTO credential =
                    JsonUtils.deserialize(dict.getVariable(), LiangcangPlatformLoginCredentialDTO.class);
            Assert.isParameterError(credential == null, "系统字典粮仓平台登录信息为空");
            Assert.isParameterError(StringUtils.isBlank(resolveUsername(credential)) || StringUtils.isBlank(credential.getPassword()),
                                    "系统字典粮仓平台登录账号或密码为空");
            Assert.isParameterError(StringUtils.isBlank(resolveRequestUrl(credential)), "系统字典粮仓平台登录请求地址为空");
            return credential;
        } catch (ServiceException e) {
            throw e;
        } catch (RuntimeException e) {
            ServiceException ex = new ServiceException("系统字典粮仓平台登录信息 variable 解析失败");
            ex.initCause(e);
            throw ex;
        }
    }

    private static WarehouseLoginInfoVO toLoginInfo(Map<String, Object> response, LiangcangPlatformLoginCredentialDTO credential) {
        var out = new WarehouseLoginInfoVO();
        out.setToken(asString(response.get(ACCESS_TOKEN)));
        out.setRefreshToken(asString(response.get(REFRESH_TOKEN)));
        out.setTokenType(asString(response.get(TOKEN_TYPE)));
        out.setExpiresIn(asLong(response.get(EXPIRES_IN)));
        out.setTenant(credential.getTenant());
        out.setTenants(asList(response.get(TENANTS)));
        out.setRaw(response);
        return out;
    }

    private static String resolveUsername(LiangcangPlatformLoginCredentialDTO credential) {
        if (credential == null) {
            return null;
        }
        return StringUtils.isNotBlank(credential.getUsername()) ? credential.getUsername().trim()
                                                                : credential.getAccount();
    }

    private static String resolveRequestUrl(LiangcangPlatformLoginCredentialDTO credential) {
        if (credential == null) {
            return null;
        }
        if (StringUtils.isNotBlank(credential.getRequestUrl())) {
            return credential.getRequestUrl().trim();
        }
        if (StringUtils.isNotBlank(credential.getUrl())) {
            return credential.getUrl().trim();
        }
        return StringUtils.isNotBlank(credential.getAuthServerUri()) ? credential.getAuthServerUri().trim() : null;
    }

    private static String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private static Long asLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value == null || StringUtils.isBlank(String.valueOf(value))) {
            return null;
        }
        try {
            return Long.valueOf(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static List<Object> asList(Object value) {
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof List<?> list) {
            return new ArrayList<>(list);
        }
        return Collections.singletonList(value);
    }
}
