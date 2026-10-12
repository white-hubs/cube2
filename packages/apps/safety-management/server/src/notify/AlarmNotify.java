package com.guhecloud.cube.srv.gen.safety.management.notify;

import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.srv.gen.safety.management.notify.dto.vo.AlarmNoticeVO;
import com.guhecloud.cube.srv.gen.safety.management.notify.service.AlarmNoticeService;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyAlarmRecordPO;
import com.guhecloud.cube.srv.gen.safety.management.rocketmq.message.AlarmRecordMessage;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmNotify {

    private final MessageSendClient messageSendClient;

    private final AlarmNoticeService alarmNoticeService;

    private final MarketConfigService marketConfigService;

    private static final String ALARM_NOTIFY_TYPE = "SAFETY_ALARM_NOTIFY";

    private static final String ALARM_TIMEOUT_NOTIFY_TYPE = "SAFETY_ALARM_TIMEOUT_NOTIFY";

    public List<AlarmNoticeVO> getAlarmTimeoutNoticeConfig() {
        return this.alarmNoticeService.getEnabledAlarmNoticeConfig(ALARM_TIMEOUT_NOTIFY_TYPE);
    }

    public void alarmNotify(AlarmRecordMessage alarmRecordMessage) {
        SessionContextHolder.requestTenant(alarmRecordMessage.getTenant());
        AlarmNoticeVO alarmNoticeConfig = alarmNoticeService.getEnabledAlarmNotice(ALARM_NOTIFY_TYPE, alarmRecordMessage.getAlarmLevelCode());
        if (Objects.nonNull(alarmNoticeConfig)) {
            List<MarketConfigPO> marketConfigList = this.marketConfigService.getMarketConfigList(alarmRecordMessage.getMarketTenant());
            Map<String, String> templateParams = new HashMap<>(3);
            templateParams.put("marketTenantName", marketConfigList.get(0).getMarketName());
            templateParams.put("areaName", alarmRecordMessage.getAreaName());
            templateParams.put("eventName", alarmRecordMessage.getAlarmEventName());
            this.messageSendClient.sceneSpecifyTmpSend(MessageSpecifyTmpSendRequest.builder()
                                                               .tenant(alarmRecordMessage.getTenant())
                                                               .msgSceneCode(alarmNoticeConfig.getSceneCode())
                                                               .messageNotifyTargets(this.convertNoticeObj(alarmNoticeConfig.getNoticeObjs()))
                                                               .businessPrincipal(alarmRecordMessage)
                                                               .businessId(alarmRecordMessage.getId().toString())
                                                               .tmpParams(templateParams)
                                                               .channel(this.convertNoticeWay(alarmNoticeConfig.getNoticeWays()))
                                                               .applicationName(alarmRecordMessage.getApplicationName())
                                                               .applicationCode(alarmRecordMessage.getApplicationCode())
                                                               .build());
        }
    }

    public void alarmTimeoutNotify(SafetyAlarmRecordPO alarmRecordPO, String timeoutProcessTime) {
        AlarmNoticeVO alarmNoticeConfig = alarmNoticeService.getEnabledAlarmNotice(ALARM_TIMEOUT_NOTIFY_TYPE, alarmRecordPO.getAlarmLevelCode());
        if (Objects.nonNull(alarmNoticeConfig)) {
            List<MarketConfigPO> marketConfigList = this.marketConfigService.getMarketConfigList(alarmRecordPO.getMarketTenant());
            Map<String, String> templateParams = new HashMap<>(3);
            templateParams.put("marketTenantName", marketConfigList.get(0).getMarketName());
            templateParams.put("areaName", alarmRecordPO.getAreaName());
            templateParams.put("eventName", alarmRecordPO.getAlarmEventName());
            templateParams.put("timeoutProcessTime", timeoutProcessTime);
            this.messageSendClient.sceneSpecifyTmpSend(MessageSpecifyTmpSendRequest.builder()
                                                               .tenant(alarmRecordPO.getTenant())
                                                               .msgSceneCode(alarmNoticeConfig.getSceneCode())
                                                               .messageNotifyTargets(this.convertNoticeObj(alarmNoticeConfig.getNoticeObjs()))
                                                               .businessId(alarmRecordPO.getId().toString())
                                                               .businessPrincipal(alarmRecordPO)
                                                               .tmpParams(templateParams)
                                                               .channel(this.convertNoticeWay(alarmNoticeConfig.getNoticeWays()))
                                                               .applicationName(alarmRecordPO.getApplicationName())
                                                               .applicationCode(alarmRecordPO.getApplicationCode())
                                                               .build());
        }
    }

    private List<MessageNotifyTarget> convertNoticeObj(List<AlarmNoticeVO.AlarmNoticeObjVO> alarmNoticeObjVOS) {
        List<MessageNotifyTarget> messageNotifyTargets = new ArrayList<>(alarmNoticeObjVOS.size());
        for (AlarmNoticeVO.AlarmNoticeObjVO noticeObj : alarmNoticeObjVOS) {
            MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
            notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.valueOf(noticeObj.getObjType()));
            String objId = noticeObj.getObjId();
            if (StringUtils.isNotBlank(objId)) {
                String[] split = objId.split(",");
                Set<Long> set = new HashSet<>();
                Arrays.stream(split).mapToLong(Long::parseLong).forEach(set::add);
                notifyTarget.setIds(set);
            }
            messageNotifyTargets.add(notifyTarget);
        }
        return messageNotifyTargets;
    }

    private List<MessageChannelRequest> convertNoticeWay(List<AlarmNoticeVO.AlarmNoticeWayVO> alarmNoticeWayVOS) {
        List<MessageChannelRequest> messageChannelRequests = new ArrayList<>(alarmNoticeWayVOS.size());
        for (AlarmNoticeVO.AlarmNoticeWayVO noticeWay : alarmNoticeWayVOS) {
            messageChannelRequests.add(MessageChannelRequest.builder()
                                               .channelCode(MessageChannelEnum.valueOf(noticeWay.getNoticeWay()))
                                               .tmpId(noticeWay.getTemplateId())
                                               .build());
        }
        return messageChannelRequests;
    }
}
