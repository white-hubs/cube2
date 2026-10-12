package com.guhecloud.cube.base.alarm.server.riskscore.job;

import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.riskscore.calculate.AlarmSituationCalculateScore;
import com.guhecloud.cube.base.alarm.server.riskscore.calculate.AlarmUnconfirmedCalculateScore;
import com.guhecloud.cube.srv.gen.riskscore.sdk.service.CalculateScoreStorage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRiskScoreJob {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmSituationCalculateScore alarmSituationCalculateScore;

    private final AlarmUnconfirmedCalculateScore alarmUnconfirmedCalculateScore;

    /**
     * 报警分数计算定时任务
     *
     * @param param
     *
     * @return
     */
    @JobAnnotation(name = "alarmRiskScoreCalculate", cron = "0 10 0 ? * 2", desc = "报警中心-报警风险指数计算")
    public void alarmRiskScoreCalculate(String param) {
        var tenants = this.alarmRecordMapper.selectAlarmTenant();
        tenants.forEach(tenant -> {
            SessionContextHolder.requestTenant(tenant);
            //报警情况计算
            CalculateScoreStorage.calculateRiskIndexScore(alarmSituationCalculateScore);
            //报警响应情况计算
            CalculateScoreStorage.calculateRiskIndexScore(alarmUnconfirmedCalculateScore);
        });
        SessionContextHolder.clearSessionContext();
    }
}
