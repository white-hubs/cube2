package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import com.guhecloud.base.message.metadata.constant.RedisConstants;
import com.guhecloud.base.message.metadata.constant.TemplateParamKeyConstants;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.enumeration.CodeVerifyResultEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.client.tencent.TencentSmsClient;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.TencentSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.CodeVerifyDTO;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.CodeSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.TencentTemplate;
import com.guhecloud.cube.base.message2.server.handler.handler.verifier.CodeVerify;
import com.guhecloud.cube.base.message2.server.handler.util.TemplateUtils;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 腾讯云短信发送
 *
 * @author lanchun
 * @date 2024/9/10 19:09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TencentSmsSender implements ManufacturerSender, CodeSender, CodeVerify {
    private final StringRedisTemplate redisTemplate;

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.TENCENT_YUN.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        TencentTemplate tencentTemplate = JsonUtils.deserialize(messageInfo.getTmpConfig(), TencentTemplate.class);
        TencentSmsAccountConfig tencentSmsAccountConfig = JsonUtils.deserialize(messageInfo.getSendAccountInfo(), TencentSmsAccountConfig.class);
        try {
            List<SendSmsResponse> sendSmsResponses = new ArrayList<>();
            SmsClient smsClient = TencentSmsClient.createClient(tencentSmsAccountConfig);
            List<SendSmsRequest> sendSmsRequests = buildSmsRequests(tencentSmsAccountConfig, tencentTemplate, messageInfo);
            for (SendSmsRequest sendSmsRequest : sendSmsRequests) {
                SendSmsResponse sendSmsResponse = smsClient.SendSms(sendSmsRequest);
                sendSmsResponses.add(sendSmsResponse);
            }
            return getMessageSendResponse(sendSmsResponses);
        } catch (Exception e) {
            log.error("腾讯云短信发送失败,任务主键:{},失败原因:{}", messageInfo.getId(), e.getMessage());
            return MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(false)
                    .failReceivers(messageInfo.getReceiver().stream().map(phone -> new MessageSendResponse.Receiver(phone, e.getMessage()))
                                           .collect(Collectors.toList()))
                    .build();
        }
    }

    @Override
    public MessageSendResponse sendCode(MessageInfo messageInfo) {
        var templateContentParams = messageInfo.getTemplateContentParams();
        var code = templateContentParams.get(TemplateParamKeyConstants.CODE);

        Assert.isParameterError(StringUtils.isEmpty(code), "验证码不能为空");
        var sendResponse = send(messageInfo);
        if (sendResponse.getSucceed()) {
            var receiver = messageInfo.getReceiver();
            var receiverList = new ArrayList<>(receiver);
            var receiverStr = receiverList.getFirst();

            var key = ChannelEnum.CHANNEL_SMS.name() + ":" + ManufacturerCodeEnum.TENCENT_YUN.name() + ":" + receiverStr;
            //缓存验证码信息
            redisTemplate.opsForValue().set(key, code, RedisConstants.CODE_EXPIRE_TIME_MINUTES, TimeUnit.MINUTES);
        }
        return sendResponse;
    }

    @Override
    public CodeResponseDTO verifyCode(CodeVerifyDTO codeVerifyDTO) {
        var key = ChannelEnum.CHANNEL_SMS.name() + ":" + ManufacturerCodeEnum.TENCENT_YUN.name() + ":" + codeVerifyDTO.receiver();
        var code = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(code)) {
            return CodeResponseDTO.failed("验证码错误", CodeVerifyResultEnum.FAILED);
        }
        if (!code.equals(codeVerifyDTO.code())) {
            return CodeResponseDTO.failed("验证码错误", CodeVerifyResultEnum.INVALID);
        }
        redisTemplate.delete(key);
        return CodeResponseDTO.success();
    }

    private MessageSendResponse getMessageSendResponse(List<SendSmsResponse> sendSmsResponses) {
        MessageSendResponse messageSendResponse = new MessageSendResponse();
        messageSendResponse.setSucceed(Boolean.TRUE);
        messageSendResponse.setFailReceivers(new ArrayList<>());
        //可能存在部分用户发送成功,部分用户发送失败的情况
        //从返回的响应结果里面筛选出发送失败的手机号
        sendSmsResponses.forEach(sendSmsResponse -> {
            log.debug("tencent send requestId {}", sendSmsResponse.getRequestId());
            List<MessageSendResponse.Receiver> failReceivers = Arrays.stream(sendSmsResponse.getSendStatusSet())
                    .filter(sendStatus -> !"Ok".equals(sendStatus.getCode()) || !"send success".equals(sendStatus.getMessage()))
                    .map(sendStatus -> {
                        String phone = sendStatus.getPhoneNumber().replace("+86", "");
                        return new MessageSendResponse.Receiver(phone, JsonUtils.serialize(sendStatus));
                    }).collect(Collectors.toList());
            messageSendResponse.getFailReceivers().addAll(failReceivers);
        });
        return messageSendResponse;
    }

    private List<SendSmsRequest> buildSmsRequests(TencentSmsAccountConfig tencentSmsAccountConfig,
                                                  TencentTemplate tencentTemplate,
                                                  MessageInfo messageInfo
                                                 ) {
        List<String[]> phoneGroups = this.buildPhones(messageInfo.getReceiver());
        return phoneGroups.stream().map(phones -> {
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppId(tencentSmsAccountConfig.getAppId());
            req.setSignName(tencentSmsAccountConfig.getSignName());
            req.setTemplateId(tencentTemplate.getTmpId());
            req.setTemplateParamSet(buildTemplateParam(messageInfo.getTemplateContent(), messageInfo.getTemplateContentParams()));
            req.setPhoneNumberSet(phones);
            return req;
        }).collect(Collectors.toList());
    }

    protected String[] buildTemplateParam(String templateContent, Map<String, String> templateContentParams) {
        //提取模板里面的变量
        List<String> templateContentVariables = TemplateUtils.getPatternTemplateVariables(templateContent);
        String[] templateParams = new String[templateContentVariables.size()];
        for (int i = 0; i < templateContentVariables.size(); i++) {
            String param = templateContentParams.get(templateContentVariables.get(i));
            if (StringUtils.isBlank(param)) {
                templateParams[i] = "";
            } else {
                templateParams[i] = param;
            }
        }
        return templateParams;
    }

    protected List<String[]> buildPhones(Set<String> phones) {
        /* 下发手机号码，采用 E.164 标准，+[国家或地区码][手机号]
         * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号 */
        //单次请求最多支持200个手机号
        int smsRequests = phones.size() / 200;
        if (phones.size() % 200 != 0) {
            smsRequests = smsRequests + 1;
        }
        List<String[]> phonesGroup = new ArrayList<>(smsRequests);
        for (int smsRequest = 0; smsRequest < smsRequests; smsRequest++) {
            int skip = smsRequest * 200;
            phonesGroup.add(phones.stream().skip(skip).limit(200).toArray(String[]::new));
        }
        return phonesGroup;
    }
}
