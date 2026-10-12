package com.guhecloud.cube.base.alarm.server.flow.application.process.manager;

import com.guhecloud.cube.base.alarm.server.flow.application.process.model.ProcessContext;
import com.guhecloud.cube.base.alarm.server.flow.application.process.step.AlarmProcessStep;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
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
public class AlarmProcessManager {

    private final List<AlarmProcessStep> processSteps;

    public AlarmProcessManager(List<AlarmProcessStep> processSteps) {
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
    public void executeProcess(AlarmRecord alarmRecord, Integer tenant) {
        var context = new ProcessContext(alarmRecord, tenant);
        for (AlarmProcessStep step : processSteps) {
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

    public void continueProcess(AlarmRecord record, Class<? extends AlarmProcessStep> stepClass, Integer tenant) {
        var context = new ProcessContext(record, tenant);
        //从步骤历史中恢复，或从特定步骤继续
        executeFromStep(context, stepClass);
    }

    public void continueProcessWith(AlarmRecord record, Class<? extends AlarmProcessStep> stepClass, Integer tenant) {
        var context = new ProcessContext(record, tenant);
        executeFromStepWith(context, stepClass);
    }

    protected void executeFromStep(ProcessContext context,
                                   Class<? extends AlarmProcessStep> stepClass
                                  ) {
        //指定步骤开始执行
        boolean found = false;
        for (AlarmProcessStep step : processSteps) {
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
                                       Class<? extends AlarmProcessStep> stepClass
                                      ) {
        //找到指定步骤后开始执行
        boolean found = false;
        for (AlarmProcessStep step : processSteps) {
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

    private void handleStepError(AlarmProcessStep step, Exception e) {
        log.error("Step {} execution failed: ", step.getName(), e);
    }
}
