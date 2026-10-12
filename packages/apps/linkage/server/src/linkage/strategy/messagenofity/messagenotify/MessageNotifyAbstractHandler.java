package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendUserResponse;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto.LinkageMessageDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.MessageTargetDynamicHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.rocketmq.message.LinkageMessageNotifyMessage;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.rocketmq.message.LinkageMessageNotifyTargetMessage;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class MessageNotifyAbstractHandler implements MessageNotifyHandler {

    private MessageNotifyTypeEnum messageNotifyType;

    private static Map<String, String> businessMsgDefineCode = new HashMap<>();

    /**
     * 后续替换到数据库进行维护
     */
    static {
        businessMsgDefineCode.put(BusinessTypeEnum.EMERGENCY_CENTER_ALARM.toString(), "EMERGENCY_ALARM");
        businessMsgDefineCode.put(BusinessTypeEnum.DEVICE_ALARM.toString(), "ONE_DEVICE_ALARM");
        businessMsgDefineCode.put(BusinessTypeEnum.DEVICE_HEALTH_ALARM.toString(), "DEVICE_HEALTH_ALARM");
        businessMsgDefineCode.put(BusinessTypeEnum.ENERGY_ALARM.toString(), "ENERGY_ALARM");
        businessMsgDefineCode.put(BusinessTypeEnum.SCENE_CONTROL.toString(), "LINKAGE_SCENE_EVENT");
        businessMsgDefineCode.put(BusinessTypeEnum.TIMING_CONTROL.toString(), "LINKAGE_TIMING_EVENT");
        businessMsgDefineCode.put(BusinessTypeEnum.DEVICE_CONDITION.toString(), "LINKAGE_CONDITION_EVENT");
    }

    protected MessageNotifyAbstractHandler(MessageNotifyTypeEnum messageNotifyType) {
        this.messageNotifyType = messageNotifyType;
    }

    @Override
    public boolean supports(MessageNotifyTypeEnum messageNotifyType) {
        return this.messageNotifyType == messageNotifyType;
    }

    /**
     * 获取消息配置
     *
     * @param businessType
     *
     * @return
     */
    public String getMsgDefineCode(String applicationCode, String businessType) {
        String sceneCode = businessMsgDefineCode.get(businessType);
        if (BusinessTypeEnum.EMERGENCY_CENTER_ALARM.toString().equals(businessType)) {
            return applicationCode + "_" + sceneCode;
        }
        return sceneCode;
    }

    protected void sendMessage(LinkageMessageDTO linkageMessageDTO, String businessType, String businessKey, String msgParam,
                               String applicationCode, String applicationName
                              ) {
        this.sendMessage(linkageMessageDTO, businessType, businessKey, msgParam, applicationCode, applicationName, null);
    }

    protected void sendMessage(LinkageMessageDTO linkageMessageDTO, String businessType, String businessKey, String msgParam,
                               String applicationCode, String applicationName, Object businessPrincipal
                              ) {
        try {
            MessageSendClient messageSendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
            List<MessageChannelRequest> channelRequests = linkageMessageDTO.getChannelTypes().stream().map(ChannelEnum ->
                                                                                                                   MessageChannelRequest
                                                                                                                           .builder()
                                                                                                                           .channelCode(ChannelEnum)
                                                                                                                           .build()
                                                                                                          ).collect(Collectors.toList());
            Map<String, String> paramMaps = JsonUtils.deserialize(msgParam, new TypeReference<Map<String, String>>() {
            });
            if (paramMaps.containsKey("areaName")) {
                String areaName = paramMaps.get("areaName");
                if (StringUtils.isBlank(areaName)) {
                    paramMaps.replace("areaName", getTenantName());
                }
            }
            MessageSendResponse messageSendResponse = messageSendClient.sceneSpecifyTmpSend(MessageSpecifyTmpSendRequest.builder()
                                                                                                    .channel(channelRequests)
                                                                                                    .msgSceneCode(this.getMsgDefineCode(applicationCode, businessType))
                                                                                                    .businessId(businessKey)
                                                                                                    .businessPrincipal(businessPrincipal)
                                                                                                    .applicationCode(applicationCode)
                                                                                                    .applicationName(applicationName)
                                                                                                    .tmpParams(paramMaps)
                                                                                                    .messageNotifyTargets(this.buildNotifyTarget(linkageMessageDTO.getTargetModifyParams()))
                                                                                                    .tenant(SessionContextHolder
                                                                                                                    .getRequester()
                                                                                                                    .getTenant())
                                                                                                    .build());
            if ("200".equals(messageSendResponse.getCode())) {
                this.sendMqMessage(businessKey, messageSendResponse.getSendUsers());
            } else {
                throw new ServiceException(messageSendResponse.getMessage());
            }
        } catch (Exception e) {
            log.error("事件类型:{},下主键为:{},消息发送失败,失败原因:{}", businessType, businessKey, e);
            throw e;
        }
    }

    public List<MessageNotifyTarget> buildNotifyTarget(List<MessageTargetNotifyDTO> messageTargetNotifyDTOS) {
        MessageTargetDynamicHandler messageTargetDynamicHandler = ApplicationContextHolder.getBean(MessageTargetDynamicHandler.class);
        List<MessageNotifyTarget> messageNotifyTargets = new ArrayList<>(messageTargetNotifyDTOS.size());
        messageTargetNotifyDTOS.forEach(notifyTargetConfig -> {
            Set<Long> targetIds = messageTargetDynamicHandler.getTargetIds(notifyTargetConfig);
            messageNotifyTargets.add(
                    MessageNotifyTarget.builder()
                            .notifyTargetType(MessageNotifyTargetEnum.valueOf(notifyTargetConfig.getMessageTargetNotify().name()))
                            .ids(targetIds)
                            .build()
                                    );
        });
        return messageNotifyTargets;
    }

    private void sendMqMessage(String businessKey, Set<MessageSendUserResponse> sendUsers) {
        Set<LinkageMessageNotifyTargetMessage> messageNotifyTargetMessages = sendUsers.stream().map(user ->
                                                                                                            ObjectUtils.copying(user, LinkageMessageNotifyTargetMessage.class)
                                                                                                   ).collect(Collectors.toSet());
        //发送mq消息,事件产生消息通知
        RocketMQContextHolder.send(LinkageMessageNotifyMessage.TOPIC, LinkageMessageNotifyMessage
                .builder()
                .businessKey(businessKey)
                .messageNotifyTargetMessages(messageNotifyTargetMessages)
                .build());
    }

    protected LinkageMessageDTO buildMessageNotify(String notifyConfig) {
        return JsonUtils.deserialize(notifyConfig, LinkageMessageDTO.class);
    }

    private String getTenantName() {
        TenantClient tenantClient = ApplicationContextHolder.getBean(TenantClient.class);
        TenantDTO tenant = tenantClient.getByCode(SessionContextHolder.getRequester().getTenant());
        return tenant.getName();
    }
}
