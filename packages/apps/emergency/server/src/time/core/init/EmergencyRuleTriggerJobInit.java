package com.guhecloud.cube.biz.emergency.server.time.core.init;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;
import com.guhecloud.cube.biz.emergency.server.time.core.timer.RuleTimer;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleTimeJobService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class EmergencyRuleTriggerJobInit {

    @Autowired
    private EmergencyRuleTimeJobService emergencyRuleTimeJobService;

    public void initRefreshNextTriggerCloseRuleTime() {
        long nowTime = System.currentTimeMillis();
        List<EmergencyRuleTimeJobPO> emergencyRuleTimeJobPOS = emergencyRuleTimeJobService.list(Wrappers
                                                                                                        .<EmergencyRuleTimeJobPO>lambdaQuery()
                                                                                                        .lt(EmergencyRuleTimeJobPO::getTriggerNextTime, nowTime)
                                                                                                        .ne(EmergencyRuleTimeJobPO::getTriggerNextTime, 0)
                                                                                                        .orderByAsc(EmergencyRuleTimeJobPO::getTriggerNextTime));
        try {
            for (EmergencyRuleTimeJobPO job : emergencyRuleTimeJobPOS) {
                //丢入时间轮
                RuleTimer.getInstance().add(new TimerTask(job.getId(),
                                                          job.getTimeType(),
                                                          job.getTimeConditions(),
                                                          job.getRuleTimeConditions(),
                                                          job.getOperateType(),
                                                          job.getTriggerNextTime(),
                                                          job.getTenant()));
            }
            for (EmergencyRuleTimeJobPO job : emergencyRuleTimeJobPOS) {
                SessionContextHolder.requestTenant(job.getTenant());
                if (EmergencyConditionTimeTypeEnum.ALL != job.getTimeType()) {
                    LocalDateTime nextValidTimeAfter = new CronExpression(job.getCronExpression()).getNextValidTimeAfter(DateUtils.now());
                    job.setTriggerLastTime(job.getTriggerNextTime());
                    job.setTriggerNextTime(DateUtils.toTimestamp(nextValidTimeAfter));
                }
                this.emergencyRuleTimeJobService.updateById(job);
                SessionContextHolder.clearSessionContext();
            }
        } catch (Exception e) {
            log.error("初始化job失败", e);
        }
    }
}
