package com.guhecloud.cube.srv.gen.safety.management.notify.job;

import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyAlarmRecordMapper;
import com.guhecloud.cube.srv.gen.safety.management.notify.AlarmNotify;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyAlarmTimeoutJob {

    private final AlarmNotify alarmNotify;

    private final SafetyAlarmRecordMapper safetyAlarmRecordMapper;

    @JobAnnotation(name = "safetyAlarmTimeoutNotifyJob", cron = "0 * * * * ?", desc = "深农总部-报警超时通知")
    public void safetyAlarmTimeoutNotifyJob(String param) {
        SessionContextHolder.clearSessionContext();
        //报警超时配置
        var alarmTimeoutNoticeConfig = this.alarmNotify.getAlarmTimeoutNoticeConfig();
        alarmTimeoutNoticeConfig.forEach(alarmNoticeVO -> {
            SessionContextHolder.requestTenant(alarmNoticeVO.getTenant());
            if (Objects.isNull(alarmNoticeVO.getValue())) {
                alarmNoticeVO.setValue(0);
            }
            var timeoutProcessTime = DateUtils.offset(LocalDateTime.now(), ChronoUnit.MINUTES, -alarmNoticeVO.getValue());
            var minuteBegin = DateUtils.offset(timeoutProcessTime, ChronoUnit.SECONDS, -timeoutProcessTime.getSecond());
            var minuteEnd = DateUtils.offset(timeoutProcessTime, ChronoUnit.SECONDS, 59 - timeoutProcessTime.getSecond());
            var alarmRecordPOS = this.safetyAlarmRecordMapper.selectLevelTimeoutAlarms(alarmNoticeVO.getAlarmLevelCode(), minuteBegin, minuteEnd);
            alarmRecordPOS.forEach(alarmRecordPO -> {
                this.alarmNotify.alarmTimeoutNotify(alarmRecordPO, alarmNoticeVO.getValue() + "分钟");
            });
        });
    }
}
