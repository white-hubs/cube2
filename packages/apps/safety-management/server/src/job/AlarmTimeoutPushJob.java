package com.guhecloud.cube.srv.gen.safety.management.job;

import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyAlarmRecordMapper;
import com.guhecloud.cube.srv.gen.safety.management.notify.AlarmNotify;
import com.guhecloud.cube.srv.gen.safety.management.rocketmq.message.AlarmRecordMessage;
import com.guhecloud.cube.srv.gen.safety.management.service.AlarmPushConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报警超时推送任务
 *
 * @author lanchun
 * @date 2024/10/24 17:58
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmTimeoutPushJob {

    private final AlarmNotify alarmNotify;

    private final SafetyAlarmRecordMapper safetyAlarmRecordMapper;

    private final AlarmPushConfigService alarmPushConfigService;

    @JobAnnotation(name = "safetyMgtAlarmTimeoutMsgPush", cron = "0 0/1 * * * ?",
                   desc = "深农总部-报警-定时推送报警处理超时消息"
    )
    public void pushSafetyAlarmRecord(String param) {
        SessionContextHolder.clearSessionContext();
        //超时的配置
        var timeoutPushConfig = this.alarmPushConfigService.getAlarmTimeoutPushConfig();
        if (CollectionUtils.isEmpty(timeoutPushConfig)) {
            return;
        }
        var timeoutAlarmRecords = this.safetyAlarmRecordMapper.getTimeoutAlarmRecords(timeoutPushConfig);
        for (var timeoutAlarmRecord : timeoutAlarmRecords) {
            this.alarmNotify.alarmNotify(ObjectUtils.copying(timeoutAlarmRecord, AlarmRecordMessage.class));
            this.safetyAlarmRecordMapper.updateDisplayedKeepUpdateTime(timeoutAlarmRecord.getId(), true);
        }
    }
}
