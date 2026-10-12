package com.guhecloud.cube.biz.emergency.server.time.core.monitor;

import com.guhecloud.cube.biz.emergency.server.time.core.plugin.ExecuteExpirePlugin;
import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.handler.EmergencyRuleEventWriterEventHandler;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.handler.EmergencyRuleTimeJobTriggerEventHandler;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleTimeJobService;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleWriterEventLogService;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleWriterEventService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Collections;

/**
 * 规则定时job监控(监控任务执行情况是否达到预期)
 *
 * @author lanchun
 * @date 2024/1/2 10:42
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleTimerTaskMonitorThread {

    private final EmergencyRuleTimeJobService emergencyRuleTimeJobService;

    private final EmergencyRuleTimeJobTriggerEventHandler emergencyRuleTimeJobTriggerEventHandler;

    private final EmergencyRuleWriterEventService emergencyRuleWriterEventService;

    private final EmergencyRuleWriterEventLogService emergencyRuleWriterEventLogService;

    private final EmergencyRuleEventWriterEventHandler emergencyRuleEventWriterEventHandler;

    private final ExecuteExpirePlugin executeExpirePlugin = new ExecuteExpirePlugin();

    @JobAnnotation(name = "emergencyRuleTimerTaskMonitor", cron = "0 0/5 * * * ?", desc = "预案管理-预案规则-定时监控规则是否下发")
    public void ruleTimerTaskMonitor(String param) {
        SessionContextHolder.clearSessionContext();
        var now = DateUtils.now();
        var lastDate = DateUtils.offset(now, ChronoUnit.MINUTES, -10);
        var triggerLastJobs = this.emergencyRuleTimeJobService.getTriggerLastJobs(lastDate, now, "open");
        for (var job : triggerLastJobs) {
            try {
                SessionContextHolder.requestTenant(job.getTenant());
                //如果已经过了关闭时间
                var closed = executeExpirePlugin.getCloseTriggered(new TimerTask(job.getId(), job.getTimeType(), job.getTimeConditions(), job.getRuleTimeConditions()), now);
                if (!closed) {
                    //验证规则是否成功下发
                    this.emergencyRuleTimeJobTriggerEventHandler.distributeRule(job);
                }
            } catch (Exception e) {
                log.error("规则是否下发成功验证失败,任务id:{}", job.getId(), e);
            } finally {
                SessionContextHolder.clearSessionContext();
            }
        }
    }

    /**
     * 事件写值重试
     */
    @JobAnnotation(name = "emergencyRuleEventWriterFailRetry", cron = "0 0/5 * * * ?", desc = "预案管理-预案规则-事件写值失败定时重试")
    public void ruleEventWriterFailRetry(String param) {
        SessionContextHolder.clearSessionContext();
        var failWriterEvents = this.emergencyRuleWriterEventService.getFailWriterEvents(5);
        for (var emergencyRuleWriterEventPO : failWriterEvents) {
            SessionContextHolder.requestTenant(emergencyRuleWriterEventPO.getTenant());
            emergencyRuleWriterEventPO.setRetryCount(emergencyRuleWriterEventPO.getRetryCount() + 1);
            var emergencyRuleWriterEventLogPO = this.emergencyRuleEventWriterEventHandler.getEmergencyRuleWriterEventLogPO(emergencyRuleWriterEventPO);
            this.emergencyRuleWriterEventService.updateById(emergencyRuleWriterEventPO);
            this.emergencyRuleWriterEventLogService.save(emergencyRuleWriterEventLogPO);
            try {
                var ruleWriteEventDTO = this.emergencyRuleEventWriterEventHandler.getRuleWriteEventDTO(emergencyRuleWriterEventPO, emergencyRuleWriterEventLogPO);
                this.emergencyRuleEventWriterEventHandler.writerIotEvent(Collections.singleton(ruleWriteEventDTO));
            } catch (Exception e) {
                log.error("规则事件重试写值下发失败,记录主键:{},失败原因:", emergencyRuleWriterEventPO.getId(), e);
            } finally {
                SessionContextHolder.clearSessionContext();
            }
        }
    }
}
