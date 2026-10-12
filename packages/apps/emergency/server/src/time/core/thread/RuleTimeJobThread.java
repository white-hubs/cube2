package com.guhecloud.cube.biz.emergency.server.time.core.thread;

import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;
import com.guhecloud.cube.biz.emergency.server.time.core.timer.RuleTimer;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleTimeJobService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class RuleTimeJobThread implements Runnable {

    private volatile boolean threadToStop = false;

    public void threadToStop() {
        threadToStop = true;
    }

    @Override
    public void run() {
        while (!threadToStop) {
            long nowTime = System.currentTimeMillis();
            LocalDateTime nowDateTime = DateUtils.beginOfDay(DateUtils.parse(nowTime));
            LocalDateTime endOfDay = DateUtils.endOfDay(nowDateTime);
            try {
                EmergencyRuleTimeJobService emergencyRuleTimeJobService = ApplicationContextHolder.getBean(EmergencyRuleTimeJobService.class);
                //查询生效的报警时间
                List<EmergencyRuleTimeJobPO> jobs = emergencyRuleTimeJobService.getTriggerNextJobs(nowDateTime, endOfDay);
                for (EmergencyRuleTimeJobPO job : jobs) {
                    //丢入时间轮
                    RuleTimer.getInstance().add(new TimerTask(job.getId(),
                                                              job.getTimeType(),
                                                              job.getTimeConditions(),
                                                              job.getRuleTimeConditions(),
                                                              job.getOperateType(),
                                                              job.getTriggerNextTime(),
                                                              job.getTenant()));
                }
            } catch (Exception e) {
                log.error("预案报警时段关闭job失败,失败原因:", e);
            }
            try {
                //休眠到指定时间后再次执行
                Thread.sleep(DateUtils.toTimestamp(endOfDay) - System.currentTimeMillis() + 1000);
            } catch (InterruptedException e) {
                log.error("Interrupted!", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
