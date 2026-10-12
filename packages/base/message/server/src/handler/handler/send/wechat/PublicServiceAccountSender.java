package com.guhecloud.cube.base.message2.server.handler.handler.send.wechat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.wechat.WechatPublicServiceAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.BaseMsgSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.dto.request.PublicServiceSendRequest;
import com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.dto.response.PublicServiceAccessTokenErrorResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.dto.response.PublicServiceAccessTokenResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.dto.response.PublicServiceSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.wechat.dto.WechatPublicServiceAccountTemplate;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.UnauthenticatedException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.constants.PublicServiceAccountConstant.ACCESS_TOKEN_CACHE_KEY;
import static com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.constants.PublicServiceAccountConstant.ACCESS_TOKEN_URL;
import static com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.constants.PublicServiceAccountConstant.TEMPLATE_SEND_URL;

@Slf4j
@Component
public class PublicServiceAccountSender extends BaseMsgSender {

    private final RestTemplate restTemplate;

    private final RedisTemplate<String, String> redisTemplate;

    public PublicServiceAccountSender(RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        super.channelCode = ChannelEnum.CHANNEL_WE_PUBLIC_SERVICE_ACCOUNT;
    }

    @Override
    public MessageSendResponse doSend(MessageInfo messageInfo) {
        var responses = batchSend(messageInfo);
        var failReceivers = responses.stream().filter(response -> !"0".equals(response.getErrCode()))
                .map(response ->
                             new MessageSendResponse.Receiver(response.getReceiver(),
                                                              String.format("errCode:%s,errMsg:%s", response.getErrCode(), response.getErrMsg()))
                    ).collect(Collectors.toList());
        return MessageSendResponse
                .builder()
                .id(messageInfo.getId())
                .succeed(true)
                .failReceivers(failReceivers)
                .build();
    }

    private List<PublicServiceSendResponse> batchSend(MessageInfo messageInfo) {
        var template = JsonUtils.deserialize(messageInfo.getTmpConfig(), WechatPublicServiceAccountTemplate.class);
        var sendAccountInfo = messageInfo.getSendAccountInfo();
        var accountConfig = WechatPublicServiceAccountConfig.getAccountConfig(sendAccountInfo);
        var accessToken = getAccessToken(accountConfig, messageInfo.getConfigTenant());
        List<PublicServiceSendResponse> responses = new ArrayList<>(messageInfo.getReceiver().size());
        //模板参数构造
        var templateParamMap = buildTemplateParam(messageInfo.getTemplateContentParams());
        for (var receiver : messageInfo.getReceiver()) {
            try {
                var sendResponse = this.send(receiver, accessToken, template.getTmpId(), templateParamMap);
                sendResponse.setReceiver(receiver);
                responses.add(sendResponse);
            } catch (Exception e) {
                log.error("微信公众号服务号发送模板消息失败:", e);
                responses.add(PublicServiceSendResponse
                                      .builder()
                                      .receiver(receiver)
                                      .errCode("500")
                                      .errMsg(e.getMessage())
                                      .build());
            }
        }
        return responses;
    }

    private PublicServiceSendResponse send(String openId, String accessToken, String tmpId,
                                           Map<String, PublicServiceSendRequest.TemplateParam> templateParamMap
                                          ) {
        var sendRequest = PublicServiceSendRequest.builder()
                .touser(openId)
                .template_id(tmpId)
                .data(templateParamMap)
                .build();
        var entity = new HttpEntity<>(sendRequest, getHeaders());
        var response = this.restTemplate.exchange(
                String.format(TEMPLATE_SEND_URL, accessToken),
                HttpMethod.POST,
                entity,
                PublicServiceSendResponse.class);
        return response.getBody();
    }

    private String getAccessToken(WechatPublicServiceAccountConfig accountConfig, Integer tenant) {
        var accessTokenCacheKey = String.format(ACCESS_TOKEN_CACHE_KEY, tenant.toString());
        var accessToken = this.redisTemplate.opsForValue().get(accessTokenCacheKey);
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        var appid = accountConfig.getAppid();
        var secret = accountConfig.getSecret();
        var accessTokenUrl = String.format(ACCESS_TOKEN_URL, "client_credential", appid, secret);
        var accessTokenResponse = this.restTemplate.getForEntity(accessTokenUrl, String.class);
        if (!accessTokenResponse.getStatusCode().is2xxSuccessful()) {
            JsonUtils.deserialize(accessTokenResponse.getBody(), PublicServiceAccessTokenResponse.class);
            throw new UnauthenticatedException("wechat_publicServiceAccount_access_token_error: %s", accessTokenResponse.toString());
        }
        var accessJson = accessTokenResponse.getBody();
        var access = JsonUtils.deserialize(accessJson, new TypeReference<Map<String, Object>>() {});
        if (MapUtils.isNotEmpty(access) && access.containsKey("errcode")) {
            //失败的情况
            var errorResponse = JsonUtils.deserialize(accessTokenResponse.getBody(), PublicServiceAccessTokenErrorResponse.class);
            throw new UnauthenticatedException("wechat_publicServiceAccount_access_token_error: %s", errorResponse.toString());
        } else {
            var tokenResponse = JsonUtils.deserialize(accessJson, PublicServiceAccessTokenResponse.class);
            accessToken = Objects.requireNonNull(tokenResponse).getAccessToken();
            this.redisTemplate.opsForValue().set(accessTokenCacheKey, accessToken, tokenResponse.getExpiresIn(), TimeUnit.SECONDS);
            return accessToken;
        }
    }

    private Map<String, PublicServiceSendRequest.TemplateParam> buildTemplateParam(Map<String, String> templateContentParams) {
        Map<String, PublicServiceSendRequest.TemplateParam> wechatTemplateParamMap = new HashMap<>(templateContentParams.size());
        templateContentParams.forEach((key, value) ->
                                              wechatTemplateParamMap.putIfAbsent(key, new PublicServiceSendRequest.TemplateParam(value))
                                     );
        return wechatTemplateParamMap;
    }

    private HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
