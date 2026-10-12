package com.guhecloud.cube.biz.emergency.server.time.core.task;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.time.core.entry.TimerTaskEntry;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.EmergencyRuleTimeJobTriggerEvent;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.handler.EmergencyRuleTimeJobTriggerEventHandler;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleTimeJobLogService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

@Slf4j
@Data
public class TimerTask implements Runnable {

    /**
     * 任务id
     */
    private Long id;

    /**
     * 时间条件类型
     */
    private EmergencyConditionTimeTypeEnum timeType;

    /**
     * 时间条件
     */
    private String timeConditions;

    /**
     * 所有时间条件
     */
    private String allTimeConditions;

    /**
     * 开启规则or关闭规则
     */
    private String openOrClose;

    /**
     * 延时时间，单位：秒
     */
    private long delayMs;

    /**
     * 执行时间
     */
    private long executeTime;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 任务所在的entry
     */
    private TimerTaskEntry timerTaskEntry;

    public TimerTask(Long id, EmergencyConditionTimeTypeEnum timeType, String timeConditions) {
        this.id = id;
        this.timeType = timeType;
        this.timeConditions = timeConditions;
    }

    public TimerTask(Long id, EmergencyConditionTimeTypeEnum timeType, String timeConditions, String allTimeConditions) {
        this.id = id;
        this.timeType = timeType;
        this.timeConditions = timeConditions;
        this.allTimeConditions = allTimeConditions;
    }

    public TimerTask(Long id, EmergencyConditionTimeTypeEnum timeType, String timeConditions, String allTimeConditions, String openOrClose,
                     long executeTime, Integer tenant
                    ) {
        this.id = id;
        this.timeType = timeType;
        this.timeConditions = timeConditions;
        this.allTimeConditions = allTimeConditions;
        this.openOrClose = openOrClose;
        this.executeTime = executeTime;
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(executeTime);
        var executeDateTime = DateUtils.parse(executeTime);
        this.delayMs = DateUtils.between(DateUtils.beginOfDay(executeDateTime), executeDateTime, ChronoUnit.MILLIS);
        this.tenant = tenant;
        this.timerTaskEntry = null;
    }

    public synchronized void setTimerTaskEntry(TimerTaskEntry entry) {
        // 如果这个timeTask已经被一个已存在的TimerTaskEntry持有,先移除一个
        if (timerTaskEntry != null && timerTaskEntry != entry) {
            timerTaskEntry.remove();
        }
        timerTaskEntry = entry;
    }

    public synchronized TimerTaskEntry getTimerTaskEntry() {
        return timerTaskEntry;
    }

    @Override
    public void run() {
        log.info("============={}任务执行", id);
        SessionContextHolder.requestTenant(tenant);
        //记录执行记录
        EmergencyRuleTimeJobLogService emergencyRuleTimeJobLogService = ApplicationContextHolder.getBean(EmergencyRuleTimeJobLogService.class);
        Long jobLogId = emergencyRuleTimeJobLogService.saveLog(id, openOrClose, DateUtils.now());
        //发送任务创建事件
        EmergencyRuleTimeJobTriggerEventHandler triggerEventHandler = ApplicationContextHolder.getBean(EmergencyRuleTimeJobTriggerEventHandler.class);
        triggerEventHandler.triggerJob(new EmergencyRuleTimeJobTriggerEvent(this, this, jobLogId));
        log.info("============={}任务执行结束", id);
    }
}
