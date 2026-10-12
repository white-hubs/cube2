package com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.SatAlarmMsgSendClient;
import com.guhecloud.cube.base.alarm.client.param.InitAssignAlarmRecordParam;
import com.guhecloud.cube.base.alarm.client.param.SatAlarmMsgSendParam;
import com.guhecloud.cube.base.alarm.client.param.SatAlarmMsgTargetSendParam;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageApplication;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageApplicationService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.LinkageActionAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.dto.AlarmNotifyDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.dto.LinkageAlarmTargetDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionExecuteRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionValidRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.MessageTargetDynamicHandler;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.TenantUserWorkStatusEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LinkageActionAlarmNotifyHandler extends LinkageActionAbstractHandler {

    @Autowired
    private SatAlarmMsgSendClient satAlarmMsgSendClient;

    @Autowired
    private MessageTargetDynamicHandler messageTargetDynamicHandler;

    @Autowired
    private TerminalClient terminalClient;

    @Autowired
    private AlarmRecordClient alarmRecordClient;

    @Autowired
    private LinkageApplicationService linkageApplicationService;

    public LinkageActionAlarmNotifyHandler() {
        super(LinkageActionTypeEnum.ALARM_NOTIFY_ACTION);
    }

    public AlarmNotifyDTO buildAlarmNotifies(Object actionConfig) {
        return JsonUtils.deserialize(actionConfig, AlarmNotifyDTO.class);
    }

    @Override
    public ExecuteStatusEnum eventActionExecute(LinkageActionExecuteRequest request) {
        ExecuteStatusEnum executeStatus = ExecuteStatusEnum.SUCCESSFUL_EXECUTION;
        try {
            if (Objects.isNull(request.getLinkageActionDetailRequest().getActionConfig())) {
                linkageBroadcast(request.getEvent());
                return executeStatus;
            }
            AlarmNotifyDTO alarmNotifyDTO = this.buildAlarmNotifies(request.getLinkageActionDetailRequest().getActionConfig());
            //发送对象
            List<SatAlarmMsgTargetSendParam> sendTargetParams = new ArrayList<>();
            alarmNotifyDTO.getTargetModifyParams().forEach(targetNotifyDTO -> {
                //通过策略获取发送对象列表
                List<UserDTO> userDTOS = this.messageTargetDynamicHandler.getUsers(targetNotifyDTO);
                if (!CollectionUtils.isEmpty(userDTOS)) {
                    userDTOS.forEach(userDTO -> {
                        if (sendTargetParams.stream().noneMatch(target -> target.getUserId().equals(userDTO.getId()))) {
                            SatAlarmMsgTargetSendParam satAlarmMsgTargetSendParam = new SatAlarmMsgTargetSendParam();
                            satAlarmMsgTargetSendParam.setUserId(userDTO.getId());
                            satAlarmMsgTargetSendParam.setUserName(userDTO.getName());
                            satAlarmMsgTargetSendParam.setDepartmentId(userDTO.getOrganization().getId());
                            satAlarmMsgTargetSendParam.setDepartmentName(userDTO.getOrganization().getName());
                            sendTargetParams.add(satAlarmMsgTargetSendParam);
                        }
                    });
                }
            });
            if (alarmNotifyDTO.getDutyUser()) {
                //通过用户查询当班人员
                List<UserTenantDTO> tenantUserWorkStatus = this.terminalClient.getTenantUserWorkStatus(
                        SessionContextHolder.getRequester().getTenant(),
                        sendTargetParams.stream().map(SatAlarmMsgTargetSendParam::getUserId).collect(Collectors.toSet()));
                if (CollectionUtils.isNotEmpty(tenantUserWorkStatus)) {
                    List<Long> userIds = tenantUserWorkStatus.stream()
                            .filter(userTenantDTO -> TenantUserWorkStatusEnum.WORKING.name().equals(userTenantDTO.getWorkStatus()))
                            .map(UserTenantDTO::getUserId)
                            .toList();
                    List<SatAlarmMsgTargetSendParam> dutyUsers = sendTargetParams.stream()
                            .filter(sendTargetParam -> userIds.contains(sendTargetParam.getUserId())).collect(Collectors.toList());
                    this.assignAlarm(request.getEvent(), dutyUsers, alarmNotifyDTO.getChannelTypes());
                }
            } else {
                this.assignAlarm(request.getEvent(), sendTargetParams, alarmNotifyDTO.getChannelTypes());
            }
            linkageBroadcast(request.getEvent());
        } catch (Exception e) {
            executeStatus = ExecuteStatusEnum.FAILED_EXECUTION;
            log.error("报警通知发送失败,事件主键为:{}---->堆栈信息:", request.getEvent().getId(), e);
        }
        return executeStatus;
    }

    @Override
    public void verification(LinkageActionValidRequest request) {
        if (StringUtils.isEmpty(request.getActionConfig()) && "[]".equalsIgnoreCase(request.getActionConfig())) {
            return;
        }
        AlarmNotifyDTO alarmNotify = this.buildAlarmNotifies(request.getActionConfig());
        Assert.isNotFound(alarmNotify, "报警通知配置不能为空");
    }

    public void assignAlarm(Event event, List<SatAlarmMsgTargetSendParam> sendTargetParams,
                            List<MessageChannelEnum> channelTypes
                           ) {
        var alarmRecordDTO = alarmRecordClient.getAlarmRecordByBusinessKey(event.getId().toString());
        if (Objects.isNull(alarmRecordDTO)) {
            return;
        }
        //指派报警
        var param = new InitAssignAlarmRecordParam();
        param.setId(alarmRecordDTO.getId());
        param.setAssignUsers(sendTargetParams);
        if (!CollectionUtils.isEmpty(channelTypes)) {
            param.setChannelTypes(channelTypes.stream().map(MessageChannelEnum::name).collect(Collectors.toList()));
        }
        this.satAlarmMsgSendClient.initAssignAlarmRecord(param);
        linkageBroadcast(event);
    }

    private void linkageBroadcast(Event event) {
        var linkageAlarmTargetDTO = JsonUtils.deserialize(event.getEventConfig(), LinkageAlarmTargetDTO.class);
        try {
            //触发其它应用联动
            var linkageApplications = this.linkageApplicationService.list(Wrappers.<LinkageApplication>lambdaQuery()
                                                                                  .and(q -> q
                                                                                          .eq(LinkageApplication::getRuleId, event
                                                                                                  .getRuleId()
                                                                                                  .toString())
                                                                                          .or()
                                                                                          .eq(StringUtils.isNotBlank(linkageAlarmTargetDTO.getEventTypeCode()), LinkageApplication::getRuleEventTypeCode, linkageAlarmTargetDTO.getEventTypeCode())
                                                                                          .or()
                                                                                          .eq(StringUtils.isNotBlank(linkageAlarmTargetDTO.getArea()), LinkageApplication::getRuleTargetArea, linkageAlarmTargetDTO.getArea())
                                                                                      )
                                                                         );
            if (!CollectionUtils.isEmpty(linkageApplications)) {
                for (var linkageApplication : linkageApplications) {
                    var linkageApplicationMessage = new LinkageApplicationMessage();
                    linkageApplicationMessage.setRuleIds(Collections.singleton(linkageApplication.getApplicationRuleId()));
                    RocketMQContextHolder.send(linkageApplication.getApplicationRuleTriggerTopic(), linkageApplicationMessage);
                }
            }
        } catch (Exception e) {
            log.error("trigger other application rule error,event id:{}", event.getId());
        }
    }

    public void sendAlarmMsg(Event event, List<SatAlarmMsgTargetSendParam> sendTargetParams) {
        var linkageAlarmTargetDTO = JsonUtils.deserialize(event.getEventConfig(), LinkageAlarmTargetDTO.class);
        //报警对象
        var satAlarmMsgSendParam = SatAlarmMsgSendParam
                .builder()
                .alarmLevelId(event.getTriggerLevelId())
                .alarmTargetKey(linkageAlarmTargetDTO.getTriggerTargetKey())
                .alarmTargetCode(linkageAlarmTargetDTO.getTriggerTargetCode())
                .alarmTargetName(linkageAlarmTargetDTO.getTriggerTargetName())
                .alarmTargetTypeId(linkageAlarmTargetDTO.getTriggerTargetTypeId())
                .alarmTargetTypeName(linkageAlarmTargetDTO.getTriggerTargetTypeName())
                .alarmTargetLongitude(linkageAlarmTargetDTO.getTriggerTargetLongitude())
                .alarmTargetAddress(linkageAlarmTargetDTO.getTriggerTargetAddress())
                .area(linkageAlarmTargetDTO.getArea())
                .areaName(linkageAlarmTargetDTO.getAreaName())
                .alarmThreshold(linkageAlarmTargetDTO.getTriggerThreshold())
                .alarmEvent(linkageAlarmTargetDTO.getEvent())
                .alarmEventName(linkageAlarmTargetDTO.getEventName())
                .alarmEventTypeId(linkageAlarmTargetDTO.getEventTypeId())
                .alarmEventTypeCode(linkageAlarmTargetDTO.getEventTypeCode())
                .alarmEventTypeName(linkageAlarmTargetDTO.getEventTypeName())
                .timeoutProcessTimes(linkageAlarmTargetDTO.getTimeoutProcessTimes())
                .timeoutProcessTimesUnit(linkageAlarmTargetDTO.getTimeoutProcessTimesUnit())
                .businessType(event.getBusinessType())
                .businessKey(String.valueOf(event.getId()))
                .alarmTime(event.getTriggerTime())
                .applicationCode(event.getApplicationCode())
                .applicationName(event.getApplicationName())
                .ruleConfig(event.getRuleConfig())
                .sendTargetParams(sendTargetParams)
                .snapshot(linkageAlarmTargetDTO.getSnapshot())//业务快照
                .alarmSnapshot(event.getSnapshot())//报警快照
                .ruleId(event.getRuleId())
                .build();
        //发送报警
        this.satAlarmMsgSendClient.sendMsg(satAlarmMsgSendParam);

    }

    @Data
    static class LinkageApplicationMessage {

        private Set<Long> ruleIds;
    }
}
