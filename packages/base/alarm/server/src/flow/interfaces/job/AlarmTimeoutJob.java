package com.guhecloud.cube.base.alarm.server.flow.interfaces.job;

import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmTimeoutJob {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmMessageSender messageSender;

    @JobAnnotation(name = "alarmTimeoutNotifyJob", cron = "0 * * * * ?",
                   desc = "报警中心-报警任务超时通知"
    )
    public void alarmTimeoutNotifyJob(String param) {
        try {
            SessionContextHolder.clearSessionContext();
            var tenants = this.alarmRecordMapper.selectAlarmTenant();
            tenants.forEach(tenant -> {
                SessionContextHolder.requestTenant(tenant);
                var applicationCodes = this.alarmRecordMapper.selectAlarmApplicationCode();
                applicationCodes.forEach(applicationCode -> {
                    var messageSendConfigs = this.messageSender.alarmTimeoutNotifyConfig(applicationCode);
                    if (!CollectionUtils.isEmpty(messageSendConfigs)) {
                        messageSendConfigs.forEach(messageSendConfig -> {
                            send(applicationCode, messageSendConfig);
                        });
                    }
                });
            });
        } catch (Exception e) {
            log.error("报警任务超时定时扫描发送通知失败,失败原因:", e);
        }
    }

    private void send(String applicationCode, MessageSendConfig messageSendConfig) {
        var timeoutProcessTime = DateUtils.offset(DateUtils.now(), ChronoUnit.MINUTES, messageSendConfig.getValue());
        var minuteBegin = DateUtils.offset(timeoutProcessTime, ChronoUnit.SECONDS, -timeoutProcessTime.getSecond());
        var minuteEnd = DateUtils.offset(timeoutProcessTime, ChronoUnit.SECONDS, 59 - timeoutProcessTime.getSecond());
        var alarmRecordPOS = this.alarmRecordMapper
                .selectAboutToTimeoutAlarmList(minuteBegin, minuteEnd, applicationCode);
        alarmRecordPOS.forEach(alarmRecordPO ->
                                       this.messageSender.alarmTimeoutSend(messageSendConfig,
                                                                           alarmRecordPO,
                                                                           messageSendConfig.getValue() +
                                                                           "分钟",
                                                                           alarmRecordPO.getUserIds())
                              );
    }
}
