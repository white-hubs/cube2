package com.guhecloud.cube.biz.linkage.server.record.application.process.manager;

import com.guhecloud.cube.biz.linkage.server.record.application.process.model.ProcessContext;
import com.guhecloud.cube.biz.linkage.server.record.application.process.step.EventRecordProcessStep;
import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EventProcessManager {

    private final List<EventRecordProcessStep> processSteps;

    public EventProcessManager(List<EventRecordProcessStep> processSteps) {
        this.processSteps = processSteps.stream()
                .sorted(Comparator.comparingInt(step -> {
                    var order = step.getClass().getAnnotation(Order.class);
                    return order != null ? order.value() : Integer.MAX_VALUE;
                }))
                .collect(Collectors.toList());
    }

    /**
     * 执行完整流程
     */
    public void executeProcess(EventRecord eventRecord, Integer tenant) {
        var context = new ProcessContext(eventRecord, tenant);
        for (var step : processSteps) {
            try {
                var result = step.execute(context);
                if (!result.shouldContinue()) {
                    throw new ServiceException(step.getName() + ":" + result.message());
                }
            } catch (Exception e) {
                handleStepError(step, e);
                break;
            }
        }
    }

    public void continueProcess(EventRecord record, Class<? extends EventRecordProcessStep> stepClass, Integer tenant) {
        var context = new ProcessContext(record, tenant);
        //从步骤历史中恢复，或从特定步骤继续
        executeFromStep(context, stepClass);
    }

    public void continueProcessWith(EventRecord record, Class<? extends EventRecordProcessStep> stepClass, Integer tenant) {
        var context = new ProcessContext(record, tenant);
        executeFromStepWith(context, stepClass);
    }

    protected void executeFromStep(ProcessContext context,
                                   Class<? extends EventRecordProcessStep> stepClass
                                  ) {
        //指定步骤开始执行
        var found = false;
        for (var step : processSteps) {
            try {
                var targetClass = AopProxyUtils.ultimateTargetClass(step);
                var name = targetClass.getName();
                if (name.equals(stepClass.getName())) {
                    found = true;
                }
                if (found) {
                    var result = step.execute(context);
                    if (!result.shouldContinue()) {
                        throw new ServiceException(step.getName() + ":" + result.message());
                    }
                }
            } catch (Exception e) {
                handleStepError(step, e);
                break;
            }
        }
    }

    protected void executeFromStepWith(ProcessContext context,
                                       Class<? extends EventRecordProcessStep> stepClass
                                      ) {
        //找到指定步骤后开始执行
        var found = false;
        for (var step : processSteps) {
            try {
                if (found) {
                    var result = step.execute(context);
                    if (!result.shouldContinue()) {
                        throw new ServiceException(step.getName() + ":" + result.message());
                    }
                }
                var targetClass = AopProxyUtils.ultimateTargetClass(step);
                var name = targetClass.getName();
                if (name.equals(stepClass.getName())) {
                    found = true;
                }
            } catch (Exception e) {
                handleStepError(step, e);
                break;
            }
        }
    }

    private void handleStepError(EventRecordProcessStep step, Exception e) {
        log.error("Step {} execution failed: ", step.getName(), e);
    }
}
