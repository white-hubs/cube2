package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.linkage.server.linkage.service.impl.EventTriggerService;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessChangeHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessDelHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEnabledHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessIncreaseHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.dto.RuleTimingDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.dto.RuleTimingEventDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.entity.RuleTiming;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.job.RuleTimingJob;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.service.RuleTimingService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 定时策略
 *
 * @author lanchun
 * @date 2022/6/29 16:17
 */
@Slf4j
@Component
@EventBusListen(order = 2)
public class RuleTimingHandler extends RuleBusinessAbstractHandler {

    @Autowired
    private RuleTimingService ruleTimingService;

    @Autowired
    private EventTriggerService eventTriggerService;

    @Autowired
    private JobService jobService;

    @Autowired
    private RuleService ruleService;

    private static final String JOB_NAME = "ruleTimingExecute";
    private static final String CALL_BACK_METHOD = "ruleTimingExecute";

    public RuleTimingHandler() {
        super(BusinessTypeEnum.TIMING_CONTROL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleAnalysis(RuleBusinessIncreaseHandlerRequest request) {
        //获取规则信息
        var rule = request.getRule();
        //获取规则json
        var ruleConfigStr = rule.getRuleConfig();
        var ruleTimingDTO = JsonUtils.deserialize(ruleConfigStr, RuleTimingDTO.class);
        var ruleTiming = ObjectUtils.copying(ruleTimingDTO, RuleTiming.class);
        ruleTiming.setId(IdWorker.getId());
        ruleTiming.setRuleId(request.getRule().getId());
        //创建Job
        this.createJob(request.getRule().getName(), ruleTiming);
        if (request.getRule().getEnabled()) {
            jobService.startJob(getJobName(ruleTiming.getJobId()));
        }
        this.ruleTimingService.save(ruleTiming);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleUpdate(RuleBusinessChangeHandlerRequest request) {
        RuleTiming ruleTiming = this.ruleTimingService.getByRuleId(request.getRule().getId());
        Assert.isNotFound(ruleTiming, "定时规则不存在");
        Long ruleTimingId = ruleTiming.getId();
        //获取规则json
        String ruleConfigStr = request.getRule().getRuleConfig();
        RuleTimingDTO ruleTimingDTO = JsonUtils.deserialize(ruleConfigStr, RuleTimingDTO.class);
        ObjectUtils.copying(ruleTimingDTO, ruleTiming);
        //修改Job
        this.updateJob(request.getRule().getName(), ruleTiming, ruleTiming.getJobId());
        if (request.getRule().getEnabled()) {
            jobService.startJob(getJobName(ruleTiming.getJobId()));
        } else {
            jobService.stopJob(getJobName(ruleTiming.getJobId()));
        }
        ruleTiming.setId(ruleTimingId);
        this.ruleTimingService.updateById(ruleTiming);
    }

    @Override
    public void ruleEnabledUpdate(RuleBusinessEnabledHandlerRequest request) {
        RuleTiming ruleTiming = this.ruleTimingService.getByRuleId(request.getRule().getId());
        Assert.isNotFound(ruleTiming, "定时规则不存在");
        if (request.getRule().getEnabled()) {
            jobService.startJob(getJobName(ruleTiming.getJobId()));
        } else {
            jobService.stopJob(getJobName(ruleTiming.getJobId()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleRemove(RuleBusinessDelHandlerRequest request) {
        RuleTiming ruleTiming = this.ruleTimingService.getByRuleId(request.getRuleId());
        Assert.isNotFound(ruleTiming, "定时规则不存在");
        //删除job
        jobService.removeJob(getJobName(ruleTiming.getJobId()));
        DatabaseContextHolder.softDelete(ruleTimingService, ruleTiming);
    }

    @Override
    public void ruleEvent(RuleBusinessEventHandlerRequest request) {
        SessionContextHolder.requestTenant(request.getRule().getTenant());
        RuleTimingEventDTO ruleTimingEventDTO = RuleTimingEventDTO
                .builder()
                .name(request.getRule().getName())
                .eventName(request.getRule().getName())
                .triggerTime(DateUtils.format(request.getTriggerTime()))
                .build();
        this.ruleService.addTriggerNum(request.getRule().getId());
        //生成事件
        this.eventTriggerService.eventTrigger(Collections.singletonList(request.getRule()), request.getTriggerTime(), JsonUtils.serialize(ruleTimingEventDTO));
    }

    private void updateJob(String name, RuleTiming ruleTiming, String jobId) {
        //单次任务
        if (!ruleTiming.getLooped()) {
            ruleTiming.setCron(DateUtils.format(ruleTiming.getExecuteTime(), "ss mm HH dd MM ? yyyy"));
        }
        jobService.removeJob(getJobName(jobId));
        //请求xxl添加任务接口获取到任务id
        createJob(name, ruleTiming);
    }

    private void createJob(String desc, RuleTiming ruleTiming) {
        //单次任务
        if (!ruleTiming.getLooped()) {
            ruleTiming.setCron(DateUtils.format(ruleTiming.getExecuteTime(), "ss mm HH dd MM ? yyyy"));
        }
        if (StringUtils.isBlank(ruleTiming.getJobId())) {
            ruleTiming.setJobId(ObjectIdUtils.next());
        }
        //添加job
        jobService.addJob(getJobName(ruleTiming.getJobId()),
                          ApplicationContextHolder.getBean(RuleTimingJob.class),
                          CALL_BACK_METHOD,
                          ruleTiming.getCron(),
                          desc,
                          String.valueOf(ruleTiming.getRuleId()),
                          false);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void initRegistryJob(SysReadEvent event) {
        SessionContextHolder.clearSessionContext();
        var list = this.ruleTimingService.list();
        for (RuleTiming timing : list) {
            if (!timing.getLooped() && !timing.getExecuteTime().isAfter(DateUtils.now())) {
                continue;
            }
            SessionContextHolder.requestTenant(timing.getTenant());
            var rule = ruleService.getById(timing.getRuleId());
            if (StringUtils.isBlank(timing.getJobId())) {
                timing.setJobId(ObjectIdUtils.next());
            }
            createJob(rule.getName(), timing);
            if (rule.getEnabled()) {
                jobService.startJob(getJobName(timing.getJobId()));
            } else {
                jobService.stopJob(getJobName(timing.getJobId()));
            }
            this.ruleTimingService.updateById(timing);
            SessionContextHolder.clearSessionContext();
        }
    }

    private String getJobName(String id) {
        return String.format("%s_%s", JOB_NAME, id);
    }
}
