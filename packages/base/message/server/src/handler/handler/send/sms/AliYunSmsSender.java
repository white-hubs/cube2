package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.guhecloud.base.message.metadata.constant.RedisConstants;
import com.guhecloud.base.message.metadata.constant.TemplateParamKeyConstants;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.enumeration.CodeVerifyResultEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.client.alibaba.AliYunSmsClient;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.AliYunSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.CodeVerifyDTO;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.CodeSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.AliYunTemplate;
import com.guhecloud.cube.base.message2.server.handler.handler.verifier.CodeVerify;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 阿里云短信发送
 *
 * @author lanchun
 * @date 2023/10/19 14:09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliYunSmsSender implements ManufacturerSender, CodeSender, CodeVerify {
    private final StringRedisTemplate redisTemplate;

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.ALI_YUN.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        //解析模板配置获取模板id
        AliYunTemplate aliYunTemplate = JsonUtils.deserialize(messageInfo.getTmpConfig(), AliYunTemplate.class);
        AliYunSmsAccountConfig aliConfig = AliYunSmsAccountConfig.getAliConfig(messageInfo.getSendAccountInfo());
        try (AsyncClient client = AliYunSmsClient.createClient(aliConfig.getAccessKeyId(), aliConfig.getAccessKeySecret())) {
            //aliyun一次发送最多只支持1000个号码,后续优化(暂不考虑)
            CompletableFuture<SendSmsResponse> sendSmsFuture = client.sendSms(SendSmsRequest.builder()
                                                                                      .phoneNumbers(String.join(",", messageInfo.getReceiver()))
                                                                                      .signName(aliConfig.getSignName())
                                                                                      .templateCode(aliYunTemplate.getTmpId())
                                                                                      .templateParam(JsonUtils.serialize(messageInfo.getTemplateContentParams()))
                                                                                      .build());
            SendSmsResponse sendSmsResponse = sendSmsFuture.get(5, TimeUnit.SECONDS);
            SendSmsResponseBody sendSmsResponseBody = sendSmsResponse.getBody();
            if ("OK".equals(sendSmsResponseBody.getCode())) {
                return MessageSendResponse
                        .builder()
                        .id(messageInfo.getId())
                        .succeed(true)
                        .message(sendSmsResponseBody.getBizId())
                        .build();
            }
            throw new ServiceException(JsonUtils.serialize(sendSmsResponseBody));
        } catch (Exception e) {
            log.error("阿里云短信发送失败,任务主键:{},失败原因:{}", messageInfo.getId(), e.getMessage());
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

            var key = ChannelEnum.CHANNEL_SMS.name() + ":" + ManufacturerCodeEnum.ALI_YUN.name() + ":" + receiverStr;
            //缓存验证码信息
            redisTemplate.opsForValue().set(key, code, RedisConstants.CODE_EXPIRE_TIME_MINUTES, TimeUnit.MINUTES);
        }
        return sendResponse;
    }

    @Override
    public CodeResponseDTO verifyCode(CodeVerifyDTO codeVerifyDTO) {
        var key = ChannelEnum.CHANNEL_SMS.name() + ":" + ManufacturerCodeEnum.ALI_YUN.name() + ":" + codeVerifyDTO.receiver();
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
}
