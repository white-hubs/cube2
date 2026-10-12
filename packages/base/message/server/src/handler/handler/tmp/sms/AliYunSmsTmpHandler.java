package com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms;

import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.DeleteSmsTemplateRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.DeleteSmsTemplateResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.DeleteSmsTemplateResponseBody;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.client.alibaba.AliYunSmsClient;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.AliYunSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.AliYunTemplate;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AliYunSmsTmpHandler extends BaseSmsTmpHandler {

    protected AliYunSmsTmpHandler() {
        super(ChannelEnum.CHANNEL_SMS, ManufacturerCodeEnum.ALI_YUN);
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        return null;
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        this.removeTmp(DeleteMessageTmp.builder()
                               .accountInfo(changeMessageTmp.getAccountInfo())
                               .tmpConfig(changeMessageTmp.getTmpConfig())
                               .build());
        return this.increaseTmp(IncreaseMessageTmp.builder()
                                        .tmpName(changeMessageTmp.getTmpName())
                                        .content(changeMessageTmp.getContent())
                                        .accountInfo(changeMessageTmp.getAccountInfo())
                                        .build());
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        if (StringUtils.isEmpty(deleteMessageTmp.getTmpConfig())) {
            return;
        }
        AliYunSmsAccountConfig aliConfig = AliYunSmsAccountConfig.getAliConfig(deleteMessageTmp.getAccountInfo());
        try (AsyncClient client = AliYunSmsClient.createClient(aliConfig.getAccessKeyId(), aliConfig.getAccessKeySecret())) {
            AliYunTemplate aliYunTemplate = JsonUtils.deserialize(deleteMessageTmp.getTmpConfig(), AliYunTemplate.class);
            CompletableFuture<DeleteSmsTemplateResponse> deleteSmsTemplateFuture = client.deleteSmsTemplate(DeleteSmsTemplateRequest
                                                                                                                    .builder()
                                                                                                                    .templateCode(aliYunTemplate.getTmpId())
                                                                                                                    .build());
            DeleteSmsTemplateResponse deleteSmsTemplateResponse = deleteSmsTemplateFuture.get(5, TimeUnit.SECONDS);
            DeleteSmsTemplateResponseBody deleteSmsTemplateResponseBody = deleteSmsTemplateResponse.getBody();
            if (!"OK".equals(deleteSmsTemplateResponseBody.getCode())) {
                log.error("阿里云删除模板失败,失败原因:{}", deleteSmsTemplateResponseBody.getMessage());
            }
        } catch (Exception e) {
            log.error("请求阿里云删除模板失败,失败原因:{}", e.getMessage());
        }
    }

    @Override
    protected String convertTemplateContent(String content) {
        return content;
    }
}
