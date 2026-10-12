package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.LeXinSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.LeXinTemplate;
import com.guhecloud.cube.base.message2.server.handler.util.TemplateUtils;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LeXinSmsSender implements ManufacturerSender {

    private static final String TEMPLATE_SEND_URL = "/sendByTemplate";

    private final RestTemplate restTemplate;

    public LeXinSmsSender(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.DYNAMIC_THINKING_LE_XIN.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        try {
            LeXinSmsAccountConfig leXinSmsAccountConfig = LeXinSmsAccountConfig.getLeXinConfig(messageInfo.getSendAccountInfo());
            LeXinTemplate leXinTemplate = JsonUtils.deserialize(messageInfo.getTmpConfig(), LeXinTemplate.class);
            String sendPhones = this.buildSendPhones(messageInfo.getReceiver(), messageInfo.getTemplateContent(), messageInfo.getTemplateContentParams());
            MultiValueMap<String, Object> req = new LinkedMultiValueMap<>();
            req.add("AppID", leXinSmsAccountConfig.getAppId());
            req.add("AppSecret", leXinSmsAccountConfig.getAppSecret());
            req.add("TemplateParam", sendPhones);
            req.add("TemplateCode", leXinTemplate.getTmpCode());
            req.add("SignName", leXinSmsAccountConfig.getSignName());
            req.add("Format", "JSON");
            ResponseEntity<String> response = getStringResponseEntity(req, leXinSmsAccountConfig);
            //解析响应结果
            if (HttpStatus.OK == response.getStatusCode()) {
                MessageSendResponse messageSendResponse = new MessageSendResponse();
                Map<String, Object> leXinResponse = JsonUtils.deserialize(response.getBody(), new TypeReference<Map<String, Object>>() {});
                String code = leXinResponse.get("Code").toString();
                if ("OK".equalsIgnoreCase(code)) {
                    messageSendResponse.setSucceed(true);
                } else {
                    log.error("乐信短信发送结果为失败:{}", leXinResponse);
                    messageSendResponse.setSucceed(false);
                }
                return messageSendResponse;
            }
            throw new ServiceException(response.getBody());
        } catch (Exception e) {
            log.error("消息发送任务id为{}乐信短信发送失败:", messageInfo.getId(), e);
            return MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(false)
                    .message(e.getMessage())
                    .failReceivers(messageInfo.getReceiver().stream().map(phone -> new MessageSendResponse.Receiver(phone, e.getMessage()))
                                           .collect(Collectors.toList()))
                    .build();
        }
    }

    private ResponseEntity<String> getStringResponseEntity(MultiValueMap<String, Object> req, LeXinSmsAccountConfig leXinSmsAccountConfig) {
        //构建请求体
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> entity = new HttpEntity<>(req, headers);
        return this.restTemplate.exchange(
                leXinSmsAccountConfig.getRequestBaseUrl() + TEMPLATE_SEND_URL, HttpMethod.POST, entity, String.class);
    }

    private String buildSendPhones(Set<String> phones, String templateContent, Map<String, String> templateContentParams) {
        String templateParamValues = buildTemplateParamValues(templateContent, templateContentParams);
        Map<String, String> sendPhones = new HashMap<>();
        for (String phone : phones) {
            sendPhones.put(phone, templateParamValues);
        }
        return JsonUtils.serialize(sendPhones);
    }

    protected String buildTemplateParamValues(String templateContent, Map<String, String> templateContentParams) {
        //提取模板里面的变量
        List<String> templateContentVariables = TemplateUtils.getPatternTemplateVariables(templateContent);
        List<String> templateParams = new ArrayList<>();
        for (String templateContentVariable : templateContentVariables) {
            String param = templateContentParams.get(templateContentVariable);
            if (StringUtils.isBlank(param)) {
                templateParams.add("");
            } else {
                templateParams.add(param);
            }
        }
        return String.join("##", templateParams);
    }
}
