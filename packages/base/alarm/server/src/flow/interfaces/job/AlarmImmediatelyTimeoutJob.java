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
public class AlarmImmediatelyTimeoutJob {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmMessageSender messageSender;

    @JobAnnotation(name = "alarmWaitForHandlerTimeoutBroadcastNotifyJob", cron = "0 * * * * ?",
                   desc = "报警中心-报警超时未处理广播发送"
    )
    public void alarmWaitForHandlerTimeoutBroadcastNotifyJob(String param) {
        try {
            SessionContextHolder.clearSessionContext();
            var now = DateUtils.now();
            var minuteBegin = DateUtils.offset(now, ChronoUnit.SECONDS, -now.getSecond());
            var minuteEnd = DateUtils.offset(now, ChronoUnit.SECONDS, 59 - now.getSecond());
            var alarmRecordPOS = this.alarmRecordMapper.selectImmediatelyTimeoutAlarmList(minuteBegin, minuteEnd);
            //报警超时未处理站内广播通知
            alarmRecordPOS.forEach(this.messageSender::alarmWaitForHandlerTimeoutBroadcastSend);
        } catch (Exception e) {
            log.error("报警超时未处理发送站内广播通知失败,失败原因:", e);
        }
    }
}
