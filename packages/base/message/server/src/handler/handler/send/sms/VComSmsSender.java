package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.message.metadata.constant.RedisConstants;
import com.guhecloud.base.message.metadata.constant.TemplateParamKeyConstants;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.enumeration.CodeVerifyResultEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.VComSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.CodeVerifyDTO;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.CodeSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.vcom.VComBatchSendRequestDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.vcom.VComBatchSendResponseDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.vcom.VComSendRequestDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.VComTemplate;
import com.guhecloud.cube.base.message2.server.handler.handler.verifier.CodeVerify;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.RandomUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 集时通短信发送
 */
@Slf4j
@Component
public class VComSmsSender implements ManufacturerSender, CodeSender, CodeVerify {

    private final RestTemplate restTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String BATCH_SEND_URL = "/fri/message/batchsend";

    public VComSmsSender(RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.V_COM.name();
    }

    @Override
    public MessageSendResponse sendCode(MessageInfo messageInfo) {
        var templateContentParams = messageInfo.getTemplateContentParams();
        var code = templateContentParams.get(TemplateParamKeyConstants.CODE);
        var tmpConfigMap = JsonUtils.deserialize(messageInfo.getTmpConfig(), new TypeReference<Map<String, String>>() {});
        Assert.isParameterError(MapUtils.isEmpty(tmpConfigMap), "短信模板配置不能为空");
        var tmpContent = tmpConfigMap.get("tmpContent");
        Assert.isParameterError(StringUtils.isEmpty(tmpContent), "短信模板内容不能为空");
        Assert.isParameterError(StringUtils.isEmpty(code), "验证码不能为空");
        messageInfo.setContent(StringUtils.replaceTemplate(tmpContent, templateContentParams));
        messageInfo.setTemplateContent(tmpContent);
        var sendResponse = send(messageInfo);
        if (sendResponse.getSucceed()) {
            var receiver = messageInfo.getReceiver();
            var receiverList = new ArrayList<>(receiver);
            var receiverStr = receiverList.getFirst();
            var key = ChannelEnum.CHANNEL_SMS.name() + ":" + ManufacturerCodeEnum.V_COM.name() + ":" + receiverStr;
            //缓存验证码信息
            redisTemplate.opsForValue().set(key, code, RedisConstants.CODE_EXPIRE_TIME_MINUTES, TimeUnit.MINUTES);
        }
        return sendResponse;
    }

    @Override
    public CodeResponseDTO verifyCode(CodeVerifyDTO codeVerifyDTO) {
        var key = ChannelEnum.CHANNEL_SMS.name() + ":" + ManufacturerCodeEnum.V_COM.name() + ":" + codeVerifyDTO.receiver();
        var code = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(code)) {
            return CodeResponseDTO.failed("验证码错误", CodeVerifyResultEnum.FAILED);
        }
        if (!code.toString().equals(codeVerifyDTO.code())) {
            return CodeResponseDTO.failed("验证码错误", CodeVerifyResultEnum.INVALID);
        }
        redisTemplate.delete(key);
        return CodeResponseDTO.success();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        try {
            var config = VComSmsAccountConfig.getVComConfig(messageInfo.getSendAccountInfo());
            //解析模板配置获取模板id
            var vComTemplate = JsonUtils.deserialize(messageInfo.getTmpConfig(), VComTemplate.class);
            if (Objects.isNull(vComTemplate)) {
                throw new ServiceException("VCom模板配置不能为空");
            }
            long currentTimeMillis = DateUtils.currentTimestamp();
            int randomNonce = Integer.parseInt(RandomUtils.randomNumbers(6));
            var batchReq = new VComBatchSendRequestDTO();
            batchReq.setAccount(config.getUserName());
            batchReq.setTimestamp(currentTimeMillis);
            batchReq.setNonce(randomNonce);
            batchReq.setSignType("md5");
            batchReq.setSignature(MD5(
                    config.getUserName() + currentTimeMillis + MD5(config.getPassword()) + randomNonce).toUpperCase());
            var reqs = new ArrayList<VComSendRequestDTO>();
            for (String phone : messageInfo.getReceiver()) {
                var req = new VComSendRequestDTO();
                req.setMobile(phone);
                req.setContent(messageInfo.getContent());
                req.setTemplateId(Integer.valueOf(vComTemplate.getTmpId()));
                reqs.add(req);
            }
            batchReq.setParams(reqs);
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var entity = new HttpEntity<>(batchReq, headers);
            var resp = this.restTemplate.exchange(
                    config.getBaseUrl() + BATCH_SEND_URL, HttpMethod.POST, entity, String.class);
            if (HttpStatus.OK != resp.getStatusCode()) {
                throw new ServiceException(resp.getBody());
            }
            //解析返回对象
            var vComResponse = JsonUtils.deserialize(resp.getBody(), new TypeReference<Map<String, Object>>() {});

            if (!vComResponse.get("respCode").toString().equals("0")) {
                throw new ServiceException(resp.getBody());
            }
            //解析返回对象
            var vComMobileResponses = JsonUtils.deserialize(vComResponse.get("data"), new TypeReference<List<VComBatchSendResponseDTO>>() {});
            //从返回的响应结果里面筛选出发送失败的手机号
            var failReceivers = vComMobileResponses.stream()
                    .filter(mobileResp -> !mobileResp.getResult().equals(0))
                    .map(msgRecipientResponse ->
                                 new MessageSendResponse.Receiver(msgRecipientResponse.getMobile(), JsonUtils.serialize(msgRecipientResponse))
                        ).collect(Collectors.toList());
            var messageSendResponse = new MessageSendResponse();
            messageSendResponse.setSucceed(Boolean.TRUE);
            messageSendResponse.setFailReceivers(failReceivers);
            return messageSendResponse;
        } catch (Exception e) {
            log.error("消息发送任务id为{},集时通数据短信发送失败:", messageInfo.getId(), e);
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

    public String MD5(String encryptContent) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(encryptContent.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            result = buf.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            log.error("md5加密失败", e);
        }
        return result;
    }
}
