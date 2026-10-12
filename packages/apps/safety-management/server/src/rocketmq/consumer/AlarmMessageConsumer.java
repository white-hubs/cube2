package com.guhecloud.cube.srv.gen.safety.management.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyAlarmRecordMapper;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.notify.AlarmNotify;
import com.guhecloud.cube.srv.gen.safety.management.po.AlarmPushConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyAlarmRecordPO;
import com.guhecloud.cube.srv.gen.safety.management.rocketmq.message.AlarmMessage;
import com.guhecloud.cube.srv.gen.safety.management.rocketmq.message.AlarmRecordMessage;
import com.guhecloud.cube.srv.gen.safety.management.service.AlarmPushConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "alarm_message_push", topic = AlarmMessage.TOPIC)
public class AlarmMessageConsumer implements RocketMQListener<AlarmMessage> {

    private final AlarmNotify alarmNotify;

    private final AlarmPushConfigService alarmPushConfigService;

    private final SafetyAlarmRecordMapper safetyAlarmRecordMapper;

    @Override
    public void onMessage(AlarmMessage message) {
        SessionContextHolder.clearSessionContext();
        List<AlarmPushConfigPO> alarmPushConfigs = this.alarmPushConfigService.list();
        for (AlarmRecordMessage alarmRecord : message.getDataList()) {
            SessionContextHolder.requestTenant(alarmRecord.getTenant());
            if (Objects.equals(Boolean.TRUE, alarmRecord.getDisplayed())) {
                this.alarmNotify.alarmNotify(alarmRecord);
                continue;
            }
            //如果配备了超时配置
            Optional<AlarmPushConfigPO> pushConfigPO = alarmPushConfigs.stream()
                    .filter(alarmPushConfig -> alarmPushConfig.getAlarmTypeCode().equals(alarmRecord.getAlarmTypeCode())
                                               &&
                                               alarmPushConfig.getAlarmLevelCode().equals(alarmRecord.getAlarmLevelCode()))
                    .findAny();
            if (pushConfigPO.isPresent()) {
                AlarmPushConfigPO config = pushConfigPO.get();
                if (Objects.nonNull(config.getTimeout()) && Objects.equals(Boolean.TRUE, config.getTimeout())) {
                    if (alarmRecord.getTimeoutEquipped() && Objects.nonNull(alarmRecord.getTimeoutProcessTime())) {
                        var timeoutProcessDate = alarmRecord.getTimeoutProcessTime().toLocalDate();
                        var alarmHandlerDate = alarmRecord.getAlarmHandlerTime().toLocalDate();
                        var now = LocalDate.now();
                        //超时处理
                        boolean handlerTimeout = AlarmStatusEnum.ALARM_HANDLER.name().equals(alarmRecord.getAlarmStatus()) &&
                                                 (timeoutProcessDate.isBefore(alarmHandlerDate) ||
                                                  timeoutProcessDate.isEqual(alarmHandlerDate));
                        //未处理超时
                        boolean unHandlerTimeOut = !AlarmStatusEnum.ALARM_HANDLER.name().equals(alarmRecord.getAlarmStatus()) &&
                                                   (timeoutProcessDate.isBefore(now) || timeoutProcessDate.isEqual(now));
                        //已超时
                        if (handlerTimeout || unHandlerTimeOut) {
                            SafetyAlarmRecordPO safetyAlarmRecordPO = safetyAlarmRecordMapper.selectById(alarmRecord.getId());
                            if (Objects.nonNull(safetyAlarmRecordPO)) {
                                //设置为展示
                                this.safetyAlarmRecordMapper.updateDisplayedKeepUpdateTime(safetyAlarmRecordPO.getId(), true);
                                this.alarmNotify.alarmNotify(alarmRecord);
                            }
                        }
                    } else {
                        //设置为展示
                        SafetyAlarmRecordPO safetyAlarmRecordPO = safetyAlarmRecordMapper.selectById(alarmRecord.getId());
                        this.safetyAlarmRecordMapper.updateDisplayedKeepUpdateTime(safetyAlarmRecordPO.getId(), true);
                        this.alarmNotify.alarmNotify(alarmRecord);
                    }
                } else {
                    SafetyAlarmRecordPO safetyAlarmRecordPO = safetyAlarmRecordMapper.selectById(alarmRecord.getId());
                    if (Objects.nonNull(safetyAlarmRecordPO)) {
                        //设置为展示
                        this.safetyAlarmRecordMapper.updateDisplayedKeepUpdateTime(safetyAlarmRecordPO.getId(), true);
                        this.alarmNotify.alarmNotify(alarmRecord);
                    }
                }
            }
        }
    }
}
