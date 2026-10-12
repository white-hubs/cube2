package com.guhecloud.cube.base.alarm.server.flow.infrastructure.message;

import com.guhecloud.cube.base.alarm.client.message.AlarmRecordOccurredMessage;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordNotifyChannelService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordPushConfigService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordService;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFilterScopeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordNotifyChannelPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPushConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.dto.AlarmRecordMsgDTO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.message.DeviceLifeEventMessage;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmAboutToTimeoutVO;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.SiteMessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.request.SiteBroadcastSendRequest;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmMessageSender {

    private final AssemblyNoticeClient assemblyNoticeClient;

    private final MessageSendClient messageSendClient;

    private final SiteMessageSendClient siteMessageSendClient;

    private final AlarmRecordService alarmRecordService;

    private final AlarmRecordNotifyChannelService alarmRecordNotifyChannelService;

    private final AlarmRecordPushConfigService alarmRecordPushConfigService;

    private final TenantClient tenantClient;

    private static final String ALARM_ASSIGN_NOTIFY_SCENE_CODE = "ALARM_ASSIGN_NOTIFY";

    private static final String ALARM_TIMEOUT_NOTIFY_SCENE_CODE = "ALARM_TIMEOUT_NOTIFY";

    private static final String ALARM_NOTIFY_BUSINESS_TYPE = "ALARM";

    private static final String DISPATCH_TERMINAL_CODE = "command-dispatch-app";

    /**
     * 获取超时通知配置
     *
     * @return
     */
    public List<MessageSendConfig> alarmTimeoutNotifyConfig(String applicationCode) {
        return this.assemblyNoticeClient.getSendMsgConfig(MessageSendSceneQueryPram.builder()
                                                                  .businessType(this.appendCode(applicationCode, ALARM_NOTIFY_BUSINESS_TYPE))
                                                                  .sceneCodes(Collections
                                                                                      .singletonList(this.appendCode(applicationCode, ALARM_TIMEOUT_NOTIFY_SCENE_CODE)))
                                                                  .build());
    }

    /**
     * 报警指派时发送
     *
     * @param alarmRecord
     * @param userIds
     */
    public void alarmAssignSend(AlarmRecord alarmRecord, Set<Long> userIds) {
        try {
            AlarmRecordPO alarmRecordPO = this.alarmRecordService.getById(alarmRecord.getAlarmInfo().getId());
            Map<String, String> tmpParams = new HashMap<>();
            tmpParams.put("alarmTime", DateUtils.format(alarmRecord.getAlarmInfo().getAlarmTime()));
            tmpParams.put("areaName", alarmRecord.getArea().getAreaName());
            tmpParams.put("alarmTargetName", alarmRecord.getAlarmTarget().getAlarmTargetName());
            tmpParams.put("eventName", alarmRecord.getAlarmInfo().getAlarmEventName());
            tmpParams.put("alarmLevel", alarmRecordPO.getAlarmLevelName());
            String areaName = alarmRecordPO.getAreaName();
            if (StringUtils.isBlank(areaName)) {
                TenantDTO tenantDTO = tenantClient.getByCode(SessionContextHolder.getRequester().getTenant());
                areaName = tenantDTO.getName();
            }
            AlarmRecordMsgDTO alarmRecordMsg = ObjectUtils.copying(alarmRecordPO, AlarmRecordMsgDTO.class);
            if (Objects.nonNull(alarmRecord.getAlarmPushConfig())) {
                alarmRecordMsg.setTurnWarningSound(alarmRecord.getAlarmPushConfig().getTurnWarningSound());
            }
            tmpParams.put("areaName", areaName);
            tmpParams.put("alarmTargetAddress", areaName);
            if (CollectionUtils.isEmpty(alarmRecord.getChannelTypes())) {
                //场景发送
                sceneSend(alarmRecord, userIds, alarmRecordMsg, tmpParams);
            } else {
                //指定通道发送
                sceneChannelSend(alarmRecord, userIds, alarmRecordMsg, tmpParams);
            }
        } catch (Exception e) {
            log.error("报警指派通知发送失败,失败原因:{}", e.getMessage());
        }

    }

    private void sceneChannelSend(AlarmRecord alarmRecord, Set<Long> userIds, AlarmRecordMsgDTO alarmRecordMsg,
                                  Map<String, String> tmpParams
                                 ) {
        List<MessageChannelRequest> channelRequests = alarmRecord.getChannelTypes().stream().map(
                ChannelEnum ->
                        MessageChannelRequest
                                .builder()
                                .channelCode(MessageChannelEnum.valueOf(ChannelEnum))
                                .build()).collect(Collectors.toList());
        //指定通道发送
        messageSendClient.sceneSpecifyTmpSend(MessageSpecifyTmpSendRequest.builder()
                                                      .channel(channelRequests)
                                                      .msgSceneCode(this.appendCode(alarmRecord.getAlarmType()
                                                                                            .getApplicationCode(), ALARM_ASSIGN_NOTIFY_SCENE_CODE))
                                                      .businessId(alarmRecord.getAlarmInfo().getId().toString())
                                                      .businessPrincipal(alarmRecordMsg)
                                                      .applicationCode(alarmRecord.getAlarmType().getApplicationCode())
                                                      .applicationName(alarmRecord.getAlarmType().getApplicationName())
                                                      .tmpParams(tmpParams)
                                                      .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget
                                                                                                              .builder()
                                                                                                              .ids(userIds)
                                                                                                              .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                              .build()))
                                                      .tenant(SessionContextHolder
                                                                      .getRequester()
                                                                      .getTenant())
                                                      .build());
    }

    private void sceneSend(AlarmRecord alarmRecord, Set<Long> userIds, AlarmRecordMsgDTO alarmRecordMsg, Map<String, String> tmpParams) {
        messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                            .msgSceneCode(this.appendCode(alarmRecord.getAlarmType()
                                                                                  .getApplicationCode(), ALARM_ASSIGN_NOTIFY_SCENE_CODE))
                                            .applicationCode(alarmRecord.getAlarmType().getApplicationCode())
                                            .applicationName(alarmRecord.getAlarmType().getApplicationName())
                                            .businessId(alarmRecord.getAlarmInfo().getId().toString())
                                            .businessPrincipal(alarmRecordMsg)
                                            .tmpParams(tmpParams)
                                            .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget
                                                                                                    .builder()
                                                                                                    .ids(userIds)
                                                                                                    .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                    .build()))
                                            .tenant(SessionContextHolder.getRequester().getTenant())
                                            .build());
    }

    /**
     * 报警超时发送
     *
     * @param messageSendConfig
     * @param alarmRecord
     * @param timeoutProcessTime
     * @param userIds
     */
    public void alarmTimeoutSend(MessageSendConfig messageSendConfig, AlarmAboutToTimeoutVO alarmRecord,
                                 String timeoutProcessTime, Set<Long> userIds
                                ) {
        try {
            AlarmRecordPO alarmRecordPO = this.alarmRecordService.getById(alarmRecord.getId());
            AlarmRecordNotifyChannelPO alarmNotifyChannel = this.alarmRecordNotifyChannelService.getByAlarmRecordId(alarmRecord.getId());
            AlarmRecordPushConfigPO alarmPushConfig = this.alarmRecordPushConfigService.getAlarmRecordPushConfigPO(alarmRecord.getId());
            Map<String, String> tmpParams = new HashMap<>();
            tmpParams.put("alarmTime", DateUtils.format(alarmRecord.getAlarmTime()));
            tmpParams.put("timeoutProcessTime", timeoutProcessTime);
            tmpParams.put("alarmTargetName", alarmRecord.getAlarmTargetName());
            tmpParams.put("eventName", alarmRecord.getAlarmEventName());
            AlarmRecordMsgDTO alarmRecordMsg = ObjectUtils.copying(alarmRecordPO, AlarmRecordMsgDTO.class);
            if (Objects.nonNull(alarmPushConfig)) {
                alarmRecordMsg.setTurnWarningSound(alarmPushConfig.getTurnWarningSound());
            }
            if (Objects.nonNull(alarmNotifyChannel) && StringUtils.isNotBlank(alarmNotifyChannel.getChannelTypes())) {
                List<MessageChannelRequest> channelRequests = new ArrayList<>();
                String[] channelTypes = alarmNotifyChannel.getChannelTypes().split(",");
                for (String channelType : channelTypes) {
                    boolean hasChannel = messageSendConfig.getNoticeWayDTOS()
                            .stream()
                            .anyMatch(assemblyNoticeWayDTO -> assemblyNoticeWayDTO.getNoticeWay().equals(channelType));
                    if (hasChannel) {
                        channelRequests.add(MessageChannelRequest
                                                    .builder()
                                                    .channelCode(MessageChannelEnum.valueOf(channelType))
                                                    .build());
                    }
                }
                //指定通道发送
                messageSendClient.sceneSpecifyTmpSend(MessageSpecifyTmpSendRequest.builder()
                                                              .channel(channelRequests)
                                                              .msgSceneCode(messageSendConfig.getSceneCode())
                                                              .businessId(alarmRecord.getId().toString())
                                                              .businessPrincipal(alarmRecordMsg)
                                                              .applicationCode(alarmRecord.getApplicationCode())
                                                              .applicationName(alarmRecord.getApplicationName())
                                                              .tmpParams(tmpParams)
                                                              .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget
                                                                                                                      .builder()
                                                                                                                      .ids(userIds)
                                                                                                                      .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                                      .build()))
                                                              .tenant(SessionContextHolder
                                                                              .getRequester()
                                                                              .getTenant())
                                                              .build());
            } else {
                assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                     .notifyId(messageSendConfig.getNotifyId())
                                                     .businessId(alarmRecord.getId().toString())
                                                     .businessPrincipal(alarmRecordMsg)
                                                     .tempParams(tmpParams)
                                                     .notifyUsers(userIds)
                                                     .applicationCode(alarmRecord.getApplicationCode())
                                                     .applicationName(alarmRecord.getApplicationName())
                                                     .build());
            }
        } catch (Exception e) {
            log.error("报警超时通知发送失败,失败原因:{}", e.getMessage());
        }
    }

    /**
     * 报警广播发送
     *
     * @param alarmRecord
     */
    public void alarmBroadcastSend(AlarmRecord alarmRecord) {
        AlarmRecordPO alarmRecordPO = this.alarmRecordService.getById(alarmRecord.getAlarmInfo().getId());
        AlarmRecordMsgDTO alarmRecordMsg = ObjectUtils.copying(alarmRecordPO, AlarmRecordMsgDTO.class);
        if (Objects.nonNull(alarmRecord.getAlarmPushConfig())) {
            alarmRecordMsg.setTurnWarningSound(alarmRecord.getAlarmPushConfig().getTurnWarningSound());
        }
        SiteBroadcastSendRequest request = SiteBroadcastSendRequest.builder()
                .eventType("ALARM_NOTICE")
                .businessPrincipal(alarmRecordMsg)
                .tenant(alarmRecordPO.getTenant())
                .build();
        //不发送指挥调度端
        if (Objects.nonNull(alarmRecord.getAlarmFilterScope()) &&
            AlarmFilterScopeEnum.DISPATCH == alarmRecord.getAlarmFilterScope().getFilterScope()) {
            request.setNonMatchTerminals(Collections.singleton(DISPATCH_TERMINAL_CODE));
        }
        this.siteMessageSendClient.broadcastSend(request);
    }

    /**
     * 报警长时间无人接收广播通知
     *
     * @param alarmRecordPO
     */
    public void alarmUnConfirmedBroadcastSend(AlarmRecordPO alarmRecordPO) {
        AlarmRecordPushConfigPO alarmPushConfig = this.alarmRecordPushConfigService.getAlarmRecordPushConfigPO(alarmRecordPO.getId());
        AlarmRecordMsgDTO alarmRecordMsg = ObjectUtils.copying(alarmRecordPO, AlarmRecordMsgDTO.class);
        if (Objects.nonNull(alarmPushConfig)) {
            alarmRecordMsg.setTurnWarningSound(alarmPushConfig.getTurnWarningSound());
        }
        SiteBroadcastSendRequest request = SiteBroadcastSendRequest.builder()
                .content("报警长时间无人接收，请及时跟进处理")
                .eventType("ALARM_TIMEOUT_UN_CONFIRMED_NOTICE")
                .businessPrincipal(alarmRecordMsg)
                .tenant(alarmRecordPO.getTenant())
                .build();
        //不发送指挥调度端
        if (Objects.nonNull(alarmRecordPO.getFilterScope()) &&
            AlarmFilterScopeEnum.DISPATCH == alarmRecordPO.getFilterScope()) {
            request.setNonMatchTerminals(Collections.singleton(DISPATCH_TERMINAL_CODE));
        }
        this.siteMessageSendClient.broadcastSend(request);
    }

    /**
     * 报警即将超时广播通知
     *
     * @param alarmRecordPO
     */
    public void alarmImmediatelyTimeoutBroadcastSend(AlarmRecordPO alarmRecordPO) {
        AlarmRecordPushConfigPO alarmPushConfig = this.alarmRecordPushConfigService.getAlarmRecordPushConfigPO(alarmRecordPO.getId());
        AlarmRecordMsgDTO alarmRecordMsg = ObjectUtils.copying(alarmRecordPO, AlarmRecordMsgDTO.class);
        if (Objects.nonNull(alarmPushConfig)) {
            alarmRecordMsg.setTurnWarningSound(alarmPushConfig.getTurnWarningSound());
        }
        SiteBroadcastSendRequest request = SiteBroadcastSendRequest.builder()
                .content("报警即将超时，请及时跟进处理")
                .eventType("ALARM_IMMEDIATELY_TIMEOUT_NOTICE")
                .businessPrincipal(alarmRecordMsg)
                .tenant(alarmRecordPO.getTenant())
                .build();
        //不发送指挥调度端
        if (Objects.nonNull(alarmRecordPO.getFilterScope()) &&
            AlarmFilterScopeEnum.DISPATCH == alarmRecordPO.getFilterScope()) {
            request.setNonMatchTerminals(Collections.singleton(DISPATCH_TERMINAL_CODE));
        }
        this.siteMessageSendClient.broadcastSend(request);
    }

    /**
     * 报警超时未处理广播通知
     *
     * @param alarmRecordPO
     */
    public void alarmWaitForHandlerTimeoutBroadcastSend(AlarmRecordPO alarmRecordPO) {
        AlarmRecordPushConfigPO alarmPushConfig = this.alarmRecordPushConfigService.getAlarmRecordPushConfigPO(alarmRecordPO.getId());
        AlarmRecordMsgDTO alarmRecordMsg = ObjectUtils.copying(alarmRecordPO, AlarmRecordMsgDTO.class);
        if (Objects.nonNull(alarmPushConfig)) {
            alarmRecordMsg.setTurnWarningSound(alarmPushConfig.getTurnWarningSound());
        }
        SiteBroadcastSendRequest request = SiteBroadcastSendRequest.builder()
                .content("报警已超时还且未处理，请及时跟进处理")
                .eventType("ALARM_TIMEOUT_UN_HANDLER_NOTICE")
                .businessPrincipal(alarmRecordMsg)
                .tenant(alarmRecordPO.getTenant())
                .build();
        //不发送指挥调度端
        if (Objects.nonNull(alarmRecordPO.getFilterScope()) &&
            AlarmFilterScopeEnum.DISPATCH == alarmRecordPO.getFilterScope()) {
            request.setNonMatchTerminals(Collections.singleton(DISPATCH_TERMINAL_CODE));
        }
        this.siteMessageSendClient.broadcastSend(request);
    }

    private String appendCode(String applicationCode, String scene) {
        return applicationCode + "_" + scene;
    }

    public void alarmMqMsgSend(AlarmRecord alarmRecord, LocalDateTime date) {
        DeviceLifeEventMessage deviceLifeEventMessage = DeviceLifeEventMessage
                .builder()
                .tenant(SessionContextHolder.getRequester().getTenant())
                .deviceIds(Collections.singleton(alarmRecord.getAlarmTarget().getAlarmTargetKey()))
                .eventId(alarmRecord.getAlarmInfo().getId())
                .eventName(alarmRecord.getAlarmInfo().getAlarmEventName())
                .eventStatus(alarmRecord.getAlarmStatus().getAlarmStatus().getDescription())
                .eventType(String.valueOf(alarmRecord.getAlarmType().getId()))
                .eventTypeName(alarmRecord.getAlarmType().getName())
                .eventTime(date)
                .alarmSnapshot(Objects.nonNull(alarmRecord.getAlarmSnapshot()) ? alarmRecord.getAlarmSnapshot().getAlarmSnapshot()
                                                                               : null)
                .applicationCode(alarmRecord.getAlarmType().getApplicationCode())
                .applicationName(alarmRecord.getAlarmType().getApplicationName())
                .build();
        RocketMQContextHolder.send(DeviceLifeEventMessage.TOPIC, deviceLifeEventMessage);
    }

    public void alarmRecordOccurredSend(AlarmRecord alarmRecord) {
        try {
            AlarmRecordPO alarmRecordPO = this.alarmRecordService.getById(alarmRecord.getAlarmInfo().getId());
            if (Objects.isNull(alarmRecordPO)) {
                log.warn("alarm record occurred message skipped, alarm record not found: {}", alarmRecord.getAlarmInfo().getId());
                return;
            }
            AlarmRecordOccurredMessage message = AlarmRecordOccurredMessage.builder()
                    .tenant(alarmRecordPO.getTenant())
                    .alarmRecordId(alarmRecordPO.getId())
                    .code(alarmRecordPO.getCode())
                    .alarmLevelId(alarmRecordPO.getAlarmLevelId())
                    .alarmLevelCode(alarmRecordPO.getAlarmLevelCode())
                    .alarmLevelName(alarmRecordPO.getAlarmLevelName())
                    .alarmLevelSort(alarmRecordPO.getAlarmLevelSort())
                    .alarmTypeId(alarmRecordPO.getAlarmTypeId())
                    .alarmTypeCode(alarmRecordPO.getAlarmTypeCode())
                    .alarmTypeName(alarmRecordPO.getAlarmTypeName())
                    .alarmTypeFileId(alarmRecordPO.getAlarmTypeFileId())
                    .alarmTargetKey(alarmRecordPO.getAlarmTargetKey())
                    .alarmTargetCode(alarmRecordPO.getAlarmTargetCode())
                    .alarmTargetName(alarmRecordPO.getAlarmTargetName())
                    .alarmTargetTypeId(alarmRecordPO.getAlarmTargetTypeId())
                    .alarmTargetTypeName(alarmRecordPO.getAlarmTargetTypeName())
                    .alarmTargetLongitude(alarmRecordPO.getAlarmTargetLongitude())
                    .alarmTargetAddress(alarmRecordPO.getAlarmTargetAddress())
                    .businessType(alarmRecordPO.getBusinessType())
                    .businessKey(alarmRecordPO.getBusinessKey())
                    .alarmTime(alarmRecordPO.getAlarmTime())
                    .area(alarmRecordPO.getArea())
                    .areaName(alarmRecordPO.getAreaName())
                    .alarmThreshold(alarmRecordPO.getAlarmThreshold())
                    .alarmEvent(alarmRecordPO.getAlarmEvent())
                    .alarmEventName(alarmRecordPO.getAlarmEventName())
                    .alarmEventTypeId(alarmRecordPO.getAlarmEventTypeId())
                    .alarmEventTypeCode(alarmRecordPO.getAlarmEventTypeCode())
                    .alarmEventTypeName(alarmRecordPO.getAlarmEventTypeName())
                    .alarmStatus(Objects.nonNull(alarmRecordPO.getAlarmStatus()) ? alarmRecordPO.getAlarmStatus().name() : null)
                    .alarmStatusName(
                            Objects.nonNull(alarmRecordPO.getAlarmStatus()) ? alarmRecordPO.getAlarmStatus().getDescription() : null)
                    .alarmStatusOrder(alarmRecordPO.getAlarmStatusOrder())
                    .applicationId(alarmRecordPO.getApplicationId())
                    .applicationCode(alarmRecordPO.getApplicationCode())
                    .applicationName(alarmRecordPO.getApplicationName())
                    .snapshot(alarmRecordPO.getSnapshot())
                    .alarmSnapshot(alarmRecordPO.getAlarmSnapshot())
                    .businessSnapshot(alarmRecordPO.getBusinessSnapshot())
                    .filterScope(Objects.nonNull(alarmRecordPO.getFilterScope()) ? alarmRecordPO.getFilterScope().name() : null)
                    .ruleId(alarmRecordPO.getRuleId())
                    .channelTypes(alarmRecord.getChannelTypes())
                    .turnWarningSound(
                            Objects.nonNull(alarmRecord.getAlarmPushConfig()) ? alarmRecord.getAlarmPushConfig().getTurnWarningSound()
                                                                              : null)
                    .handlerUsers(CollectionUtils.isNotEmpty(alarmRecord.getAlarmHandlerUsers()) ? alarmRecord.getAlarmHandlerUsers()
                            .stream()
                            .map(user -> AlarmRecordOccurredMessage.UserMessage.builder()
                                    .userId(user.getId())
                                    .userName(user.getUserName())
                                    .departmentId(user.getDepartmentId())
                                    .departmentName(user.getDepartmentName())
                                    .build())
                            .collect(Collectors.toList()) : Collections.emptyList())
                    .notifyUsers(CollectionUtils.isNotEmpty(alarmRecord.getAlarmNotifyUsers()) ? alarmRecord.getAlarmNotifyUsers()
                            .stream()
                            .map(user -> AlarmRecordOccurredMessage.UserMessage.builder()
                                    .userId(user.getId())
                                    .userName(user.getUserName())
                                    .departmentId(user.getDepartmentId())
                                    .departmentName(user.getDepartmentName())
                                    .build())
                            .collect(Collectors.toList()) : Collections.emptyList())
                    .deleted(alarmRecordPO.getDeleted())
                    .createBy(alarmRecordPO.getCreateBy())
                    .createTime(alarmRecordPO.getCreateTime())
                    .updateBy(alarmRecordPO.getUpdateBy())
                    .updateTime(alarmRecordPO.getUpdateTime())
                    .build();
            RocketMQContextHolder.send(AlarmRecordOccurredMessage.TOPIC, message);
        } catch (Exception e) {
            log.error("alarm record occurred message send failed, alarm record id: {}", alarmRecord.getAlarmInfo().getId(), e);
        }
    }
}
