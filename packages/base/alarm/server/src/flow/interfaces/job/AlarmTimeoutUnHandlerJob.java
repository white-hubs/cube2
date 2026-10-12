package com.guhecloud.cube.base.alarm.server.flow.interfaces.job;

import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.platform.base.context.SessionContextHolder;
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
public class AlarmTimeoutUnHandlerJob {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmMessageSender messageSender;

    @JobAnnotation(name = "alarmImmediatelyTimeoutBroadcastNotifyJob", cron = "0 * * * * ?",
                   desc = "报警中心-报警即将超时广播发送",
                   param = "5"
    )
    public void alarmImmediatelyBroadcastNotifyJob(String param) {
        try {
            SessionContextHolder.clearSessionContext();
            var minutes = Integer.parseInt(param);
            var timeoutProcessTime = DateUtils.offset(DateUtils.now(), ChronoUnit.MINUTES, minutes);
            var alarmRecordPOS = this.alarmRecordMapper.selectImmediatelyTimeoutAlarmList(
                    DateUtils.offset(timeoutProcessTime, ChronoUnit.SECONDS, -timeoutProcessTime.getSecond()),
                    DateUtils.offset(timeoutProcessTime, ChronoUnit.SECONDS, 59 - timeoutProcessTime.getSecond()));
            //发送即将超时站内广播通知
            alarmRecordPOS.forEach(this.messageSender::alarmImmediatelyTimeoutBroadcastSend);
        } catch (Exception e) {
            log.error("报警即将超时发送站内广播通知失败,失败原因:", e);
        }
    }
}
