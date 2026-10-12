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
public class AlarmUnConfirmedJob {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmMessageSender messageSender;

    @JobAnnotation(name = "alarmUnConfirmedBroadcastNotifyJob", cron = "0 * * * * ?",
                   desc = "报警中心-报警报警长时间无人接收广播接收",
                   param = "5"
    )
    public void alarmUnConfirmedBroadcastNotifyJob(String param) {
        try {
            SessionContextHolder.clearSessionContext();
            var minutes = Integer.parseInt(param);
            var timeoutProcessTime = DateUtils.offset(DateUtils.now(), ChronoUnit.MINUTES, -minutes);
            var minuteBegin = DateUtils.offset(timeoutProcessTime, ChronoUnit.SECONDS, -timeoutProcessTime.getSecond());
            var minuteEnd = DateUtils.offset(timeoutProcessTime, ChronoUnit.SECONDS, 59 - timeoutProcessTime.getSecond());
            var alarmRecordPOS = this.alarmRecordMapper.selectAboutToUnConfirmedAlarmList(minuteBegin, minuteEnd);
            //发送未接收站内广播通知
            alarmRecordPOS.forEach(this.messageSender::alarmUnConfirmedBroadcastSend);
        } catch (Exception e) {
            log.error("报警报警长时间无人接收发送站内广播通知失败,失败原因:", e);
        }
    }
}
