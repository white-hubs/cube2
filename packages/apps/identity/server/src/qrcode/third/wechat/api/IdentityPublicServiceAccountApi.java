package com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.api;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.config.WechatPublicServiceAccountConfig;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.dto.request.CreateWechatPublicServiceQrCodeRequest;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.dto.response.CreateWechatPublicServiceQrCodeResponse;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.dto.response.PublicServiceAccessTokenErrorResponse;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.dto.response.PublicServiceAccessTokenResponse;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.standard.exception.UnauthenticatedException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.constants.PublicServiceAccountConstant.ACCESS_TOKEN_CACHE_KEY;
import static com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.constants.PublicServiceAccountConstant.ACCESS_TOKEN_URL;
import static com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.constants.PublicServiceAccountConstant.CREATE_QR_CODE_URL;

@Slf4j
@Component
public class IdentityPublicServiceAccountApi {

    private final RestTemplate restTemplate;

    private final DataDictionaryAdapter dataDictionaryAdapter;

    private final RedisTemplate<String, String> redisTemplate;

    public IdentityPublicServiceAccountApi(RestTemplate restTemplate, DataDictionaryAdapter dataDictionaryAdapter,
                                           RedisTemplate<String, String> redisTemplate
                                          ) {
        this.restTemplate = restTemplate;
        this.dataDictionaryAdapter = dataDictionaryAdapter;
        this.redisTemplate = redisTemplate;
    }

    public CreateWechatPublicServiceQrCodeResponse createWechatPublicServiceQrCode() {
        WechatPublicServiceAccountConfig accountConfig = this.getWechatPublicServiceAccountConfig();
        String accessToken = getAccessToken(accountConfig);
        CreateWechatPublicServiceQrCodeRequest sendRequest = CreateWechatPublicServiceQrCodeRequest.builder()
                .action_name("QR_STR_SCENE")
                .expire_seconds(2592000)
                .action_info(new CreateWechatPublicServiceQrCodeRequest.ActionInfo(
                        new CreateWechatPublicServiceQrCodeRequest.Scene("test")
                ))
                .build();
        HttpEntity<CreateWechatPublicServiceQrCodeRequest> entity = new HttpEntity<>(sendRequest, getHeaders());
        ResponseEntity<CreateWechatPublicServiceQrCodeResponse> response = this.restTemplate.exchange(
                String.format(CREATE_QR_CODE_URL, accessToken),
                HttpMethod.POST,
                entity,
                CreateWechatPublicServiceQrCodeResponse.class);
        return response.getBody();
    }

    private WechatPublicServiceAccountConfig getWechatPublicServiceAccountConfig() {
        DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
        param.setCode("WX_OFFICIAL_ACCOUNT");
        param.setItemCode("THIRD_SERVICE");
        DataDictionaryDTO data = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
        if (Objects.isNull(data) || StringUtils.isBlank(data.getVariable())) {
            throw new ResourceNotFoundException("wechat_public_service_account_config not found");
        }
        return JsonUtils.deserialize(data.getVariable(), WechatPublicServiceAccountConfig.class);
    }

    private String getAccessToken(WechatPublicServiceAccountConfig accountConfig) {
        String accessTokenCacheKey = String.format(ACCESS_TOKEN_CACHE_KEY, SessionContextHolder.getRequester().getTenant());
        String accessToken = this.redisTemplate.opsForValue().get(accessTokenCacheKey);
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        String appid = accountConfig.getAppid();
        String secret = accountConfig.getSecret();
        String accessTokenUrl = String.format(ACCESS_TOKEN_URL, "client_credential", appid, secret);
        ResponseEntity<String> accessTokenResponse = this.restTemplate.getForEntity(accessTokenUrl, String.class);
        if (!accessTokenResponse.getStatusCode().is2xxSuccessful()) {
            JsonUtils.deserialize(accessTokenResponse.getBody(), PublicServiceAccessTokenResponse.class);
            throw new UnauthenticatedException("wechat_publicServiceAccount_access_token_error: %s", accessTokenResponse.toString());
        }
        String accessJson = accessTokenResponse.getBody();
        if (JSONObject.parseObject(accessJson).containsKey("errcode")) {
            //失败的情况
            PublicServiceAccessTokenErrorResponse errorResponse = JsonUtils.deserialize(accessTokenResponse.getBody(), PublicServiceAccessTokenErrorResponse.class);
            throw new UnauthenticatedException("wechat_publicServiceAccount_access_token_error: %s", errorResponse.toString());
        } else {
            PublicServiceAccessTokenResponse tokenResponse = JsonUtils.deserialize(accessJson, PublicServiceAccessTokenResponse.class);
            accessToken = Objects.requireNonNull(tokenResponse).getAccessToken();
            this.redisTemplate.opsForValue().set(accessTokenCacheKey, accessToken, tokenResponse.getExpiresIn(), TimeUnit.SECONDS);
            return accessToken;
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
