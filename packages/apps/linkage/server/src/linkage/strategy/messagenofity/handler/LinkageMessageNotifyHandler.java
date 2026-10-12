package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.enumeration.EventTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.LinkageActionAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionExecuteRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionValidRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto.LinkageMessageDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.MessageNotifyDynamicHandler;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class LinkageMessageNotifyHandler extends LinkageActionAbstractHandler {

    @Autowired
    private MessageNotifyDynamicHandler messageNotifyDynamicHandler;

    @Autowired
    private AlarmRecordClient alarmRecordClient;

    public LinkageMessageNotifyHandler() {
        super(LinkageActionTypeEnum.MESSAGE_NOTIFY_ACTION);
    }

    public List<LinkageMessageDTO> buildMessageNotifies(Object o) {
        return JsonUtils.deserialize(o, new TypeReference<List<LinkageMessageDTO>>() {
        });
    }

    /**
     * 消息发送动作执行
     *
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecuteStatusEnum eventActionExecute(LinkageActionExecuteRequest request) {
        ExecuteStatusEnum executeStatusEnum = ExecuteStatusEnum.SUCCESSFUL_EXECUTION;
        try {
            if (Objects.isNull(request.getLinkageActionDetailRequest().getActionConfig())) {
                //动作执行结果
                return executeStatusEnum;
            }
            Object businessPrincipal = null;
            if (EventTypeEnum.ALARM_RULE == request.getEvent().getType()) {
                businessPrincipal = this.alarmRecordClient.getAlarmRecordByBusinessKey(request.getEvent().getId().toString());
            }
            Object sendBusinessPrincipal = businessPrincipal;
            List<LinkageMessageDTO> linkageMessageDTOS = this.buildMessageNotifies(request.getLinkageActionDetailRequest()
                                                                                           .getActionConfig());
            sendLinkageMessage(request.getEvent(), sendBusinessPrincipal, linkageMessageDTOS);
        } catch (Exception e) {
            executeStatusEnum = ExecuteStatusEnum.FAILED_EXECUTION;
            log.error("消息发送失败,事件主键为:{}---->堆栈信息:", request.getEvent().getId(), e);
        }
        //动作执行结果
        return executeStatusEnum;
    }

    public void sendLinkageMessage(Event event, Object sendBusinessPrincipal, List<LinkageMessageDTO> linkageMessageDTOS) {
        try {
            linkageMessageDTOS.forEach(linkageMessageDTO ->
                                               this.messageNotifyDynamicHandler.messageNotify(
                                                       event,
                                                       linkageMessageDTO,
                                                       sendBusinessPrincipal)
                                      );
        } catch (Exception e) {
            log.error("消息发送失败,事件主键为:{}---->堆栈信息:", event.getId(), e);
        }
    }

    @Override
    public void verification(LinkageActionValidRequest request) {
        if (StringUtils.isEmpty(request.getActionConfig()) || "[]".equalsIgnoreCase(request.getActionConfig())) {
            return;
        }
        List<LinkageMessageDTO> linkageMessageDTOS = this.buildMessageNotifies(request.getActionConfig());
        Assert.isParameterError(CollectionUtils.isEmpty(linkageMessageDTOS), "消息通知配置不能为空");
        linkageMessageDTOS.forEach(linkageMessageDTO -> Assert.isParameterError(
                Objects.isNull(linkageMessageDTO.getNotifyType()) ||
                CollectionUtils.isEmpty(linkageMessageDTO.getChannelTypes()),
                "通知配置不能为空"));
    }
}
