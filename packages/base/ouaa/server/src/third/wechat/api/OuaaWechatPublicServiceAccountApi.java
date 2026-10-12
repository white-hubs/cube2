package com.guhecloud.cube.biz.ouaa.server.third.wechat.api;

import com.guhecloud.cube.biz.ouaa.server.third.wechat.api.config.WechatPublicServiceAccountConfig;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.api.dto.response.WechatPublicServiceSnsAccessTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static com.guhecloud.cube.biz.ouaa.server.third.wechat.api.constants.WechatApiUrlConstant.WECHAT_PUBLIC_SERVICE_SNS_ACCESS_TOKEN_URL;

@Slf4j
@Component
public class OuaaWechatPublicServiceAccountApi {

    private final RestTemplate restTemplate;

    public OuaaWechatPublicServiceAccountApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WechatPublicServiceSnsAccessTokenResponse getPublicServiceSnsAccessTokenByCode(String code,
                                                                                          WechatPublicServiceAccountConfig config
                                                                                         ) {
        String url = String.format(WECHAT_PUBLIC_SERVICE_SNS_ACCESS_TOKEN_URL,
                                   config.getAppid(),
                                   config.getSecret(),
                                   code);
        HttpEntity<?> entity = new HttpEntity<>(null, getHeaders());
        ResponseEntity<WechatPublicServiceSnsAccessTokenResponse> response = this.restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                WechatPublicServiceSnsAccessTokenResponse.class);
        return response.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
