package com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.client.tencent.TencentSmsClient;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.TencentSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.TencentTemplate;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.cube.base.message2.server.handler.util.TemplateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.AddSmsTemplateRequest;
import com.tencentcloudapi.sms.v20210111.models.AddSmsTemplateResponse;
import com.tencentcloudapi.sms.v20210111.models.DeleteSmsTemplateRequest;
import com.tencentcloudapi.sms.v20210111.models.DeleteSmsTemplateResponse;
import com.tencentcloudapi.sms.v20210111.models.ModifySmsTemplateRequest;
import com.tencentcloudapi.sms.v20210111.models.ModifySmsTemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TencentSmsTmpHandler extends BaseSmsTmpHandler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TENCENT_ADD_SMS_TEMPLATE_TIME = "TENCENT_ADD_SMS_TEMPLATE_TIME";

    protected TencentSmsTmpHandler() {
        super(ChannelEnum.CHANNEL_SMS, ManufacturerCodeEnum.TENCENT_YUN);
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        TencentSmsAccountConfig tencentSmsAccountConfig = JsonUtils.deserialize(increaseMessageTmp.getAccountInfo(), TencentSmsAccountConfig.class);
        try {
            SmsClient smsClient = TencentSmsClient.createClient(tencentSmsAccountConfig);
            AddSmsTemplateRequest addSmsTemplateRequest = this.buildSmsTemplateRequest(increaseMessageTmp);
            AddSmsTemplateResponse addSmsTemplateResponse = smsClient.AddSmsTemplate(addSmsTemplateRequest);
            return TmpVO.builder()
                    .succeed(true)
                    .tmpConfig(JsonUtils.serialize(new TencentTemplate(addSmsTemplateResponse.getAddTemplateStatus()
                                                                               .getTemplateId())))
                    .errorMsg(addSmsTemplateResponse.getRequestId())
                    .build();
        } catch (Exception e) {
            log.error("调用腾讯云保存模板接口异常:{}", e.getMessage(), e);
            return TmpVO.failTmpVO(e.getMessage());
        } finally {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }

        }
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        TencentSmsAccountConfig tencentSmsAccountConfig = JsonUtils.deserialize(changeMessageTmp.getAccountInfo(), TencentSmsAccountConfig.class);
        try {
            SmsClient smsClient = TencentSmsClient.createClient(tencentSmsAccountConfig);
            ModifySmsTemplateRequest modifySmsTemplateRequest = this.buildModifySmsTemplateRequest(changeMessageTmp);
            ModifySmsTemplateResponse modifySmsTemplateResponse = smsClient.ModifySmsTemplate(modifySmsTemplateRequest);
            return TmpVO.builder()
                    .succeed(true)
                    .tmpConfig(JsonUtils.serialize(new TencentTemplate(String.valueOf(modifySmsTemplateResponse
                                                                                              .getModifyTemplateStatus()
                                                                                              .getTemplateId()))))
                    .errorMsg(modifySmsTemplateResponse.getRequestId())
                    .build();
        } catch (Exception e) {
            log.error("调用腾讯云修改模板接口异常:{}", e.getMessage(), e);
            return TmpVO.failTmpVO(e.getMessage());
        }
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        TencentSmsAccountConfig tencentSmsAccountConfig = JsonUtils.deserialize(deleteMessageTmp.getAccountInfo(), TencentSmsAccountConfig.class);
        TencentTemplate tencentTemplate = JsonUtils.deserialize(deleteMessageTmp.getTmpConfig(), TencentTemplate.class);
        try {
            DeleteSmsTemplateRequest deleteSmsTemplateRequest = new DeleteSmsTemplateRequest();
            deleteSmsTemplateRequest.setTemplateId(Long.valueOf(tencentTemplate.getTmpId()));
            SmsClient smsClient = TencentSmsClient.createClient(tencentSmsAccountConfig);
            DeleteSmsTemplateResponse deleteSmsTemplateResponse = smsClient.DeleteSmsTemplate(deleteSmsTemplateRequest);
            if ("return successfully!".equals(deleteSmsTemplateResponse.getDeleteTemplateStatus().getDeleteStatus())) {
                log.error("腾讯云删除模板失败,requestId{}", deleteSmsTemplateResponse.getRequestId());
            }
        } catch (Exception e) {
            log.error("调用腾讯云删除模板接口异常:{}", e.getMessage(), e);
        }
    }

    protected AddSmsTemplateRequest buildSmsTemplateRequest(IncreaseMessageTmp increaseMessageTmp) {
        String convertTemplateContent = convertTemplateContent(increaseMessageTmp.getContent());
        AddSmsTemplateRequest templateRequest = new AddSmsTemplateRequest();
        templateRequest.setTemplateName(increaseMessageTmp.getTmpName());
        templateRequest.setTemplateContent(convertTemplateContent);
        templateRequest.setSmsType(2L);
        templateRequest.setInternational(0L);
        templateRequest.setRemark(convertTemplateContent);
        return templateRequest;
    }

    protected ModifySmsTemplateRequest buildModifySmsTemplateRequest(ChangeMessageTmp changeMessageTmp) {
        String convertTemplateContent = convertTemplateContent(changeMessageTmp.getContent());
        ModifySmsTemplateRequest templateRequest = new ModifySmsTemplateRequest();
        templateRequest.setTemplateName(changeMessageTmp.getTmpName());
        templateRequest.setTemplateContent(convertTemplateContent);
        templateRequest.setSmsType(2L);
        templateRequest.setInternational(0L);
        templateRequest.setRemark(convertTemplateContent);
        return templateRequest;
    }

    @Override
    protected String convertTemplateContent(String content) {
        List<String> patternTemplateVariables = TemplateUtils.getPatternTemplateVariables(content);
        String convertTemplate = content;
        for (int i = 1; i <= patternTemplateVariables.size(); i++) {
            convertTemplate = convertTemplate.replace("${" + patternTemplateVariables.get(i - 1) + "}", "{" + i + "}");
        }
        return convertTemplate;
    }
}
