package com.guhecloud.cube.biz.daily.task.status.service.impl;

import com.guhecloud.cube.biz.daily.task.status.base.BaseState;
import com.guhecloud.cube.biz.daily.task.status.base.BusinessRuleConfigurator;
import com.guhecloud.cube.biz.daily.task.status.base.StateAction;
import com.guhecloud.cube.biz.daily.task.status.base.StateCondition;
import com.guhecloud.cube.biz.daily.task.status.base.StateNode;
import com.guhecloud.cube.biz.daily.task.status.base.StateTransitionRule;
import com.guhecloud.cube.biz.daily.task.status.base.TaskStateListener;
import com.guhecloud.cube.biz.daily.task.status.base.TaskStateProvider;
import com.guhecloud.cube.biz.daily.task.status.base.TransitionContext;
import com.guhecloud.cube.biz.daily.task.status.event.StateChangedEvent;
import com.guhecloud.cube.biz.daily.task.status.service.BusinessStateMachine;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 14:12
 * @Description:
 */

@Service
@Slf4j
public class BusinessStateMachineImpl implements BusinessStateMachine {

    @Autowired
    private BusinessRuleConfigurator ruleConfigurator;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private TaskStateProvider stateProvider;
    @Autowired
    private List<TaskStateListener> globalListeners;
    @Autowired
    private List<StateAction> stateActions;

    /**
     * 执行状态流转
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushEvent(Long taskId, String businessType, String event) {
        pushEvent(taskId, businessType, event, new HashMap<>(0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushEvent(List<Long> taskIds, String businessType, String event) {
        pushEvent(taskIds, businessType, event, new HashMap<>(0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushEvent(List<Long> taskIds, String businessType, String event, Map<String, Object> variables) {
        if (CollectionUtils.isEmpty(taskIds)) {
            return;
        }
        try {
            stateProvider.batchInit(taskIds, ruleConfigurator.getStateProvider(event));
            var context = TransitionContext.create(businessType, taskIds, event);
            context.setInitialState(ruleConfigurator.getInitState(businessType));
            context.setEntity(stateProvider.batchGet());
            context.setParams(variables);
            // 获取当前状态
            var rule = resolveRule(context);
            // 执行流转
            stateProvider.setEntity(context.getEntity());
            executeTransition(context, rule, stateProvider);

        } catch (Exception e) {
            throw new ServiceException("状态流转执行失败: 业务类型[" + businessType + "], 操作[" + event + "]", e);
        } finally {
            stateProvider.clearTaskEntityInfo();
        }
    }

    @Override
    public String getCompleteState(String businessType, String event) {
        return ruleConfigurator.getCompleteState(businessType, event);
    }

    @Override
    public List<String> getNoFinalStatus(String dailyType) {
        return ruleConfigurator.getNoFinalStatus(dailyType);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushEvent(Long taskId, String businessType, String event, Map<String, Object> params) {
        pushEvent(Collections.singletonList(taskId), businessType, event, params);
    }

    private StateTransitionRule resolveRule(TransitionContext context) {
        StateTransitionRule stateTransitionRule = ruleConfigurator.getRules(context.getBusinessType()).stream()
                .filter(rule -> rule.getEvent().equals(context.getEvent()))
                .findFirst().orElse(null);
        if (Objects.isNull(stateTransitionRule)) {
            throw new ServiceException(
                    "未找到匹配的流转规则: 业务类型[" + context.getBusinessType() + "], 操作[" + context.getEvent() + "]");
        }
        // 执行前置监听
        notifyBeforeTransition(context);

        // 执行前置动作
        executeBeforeActions(context, stateTransitionRule);
        context.setBeforeState(context.getEntity().stream()
                                       .filter(t -> StringUtils.isNotBlank(t.getStatus()))
                                       .collect(Collectors.toMap(BaseState::getId, BaseState::getStatus)));
        for (BaseState dailyTaskPO : context.getEntity()) {
            if (Objects.nonNull(stateTransitionRule.getToState())) {
                dailyTaskPO.setStatus(stateTransitionRule.getToState().getStateCode());
                continue;
            }
            if (StringUtils.isBlank(dailyTaskPO.getStatus())) {
                dailyTaskPO.setStatus(context.getInitialState().getStateCode());
                continue;
            }
            context.setCurrentEntity(dailyTaskPO);
            Map<StateCondition, StateNode> toStateMap = stateTransitionRule.getToStateMap();
            for (Map.Entry<StateCondition, StateNode> entry : toStateMap.entrySet()) {
                if (entry.getKey().evaluate(context)) {
                    dailyTaskPO.setStatus(entry.getValue().getStateCode());
                }
            }
        }
        return stateTransitionRule;
    }

    private void executeTransition(TransitionContext context,
                                   StateTransitionRule rule,
                                   TaskStateProvider stateProvider
                                  ) {
        // 验证流转
        if (!validateTransition(context, stateProvider.batchGet())) {
            return;
        }

        stateProvider.updateState();

        // 执行后置动作
        executeAfterActions(context, rule);
        context.setEntity(stateProvider.batchGet());
        // 执行后置监听
        notifyAfterTransition(context);
        // 发布状态转换事件
        eventPublisher.publishEvent(new StateChangedEvent(context));

    }

    private void executeBeforeActions(TransitionContext context, StateTransitionRule rule) {
        for (String action : rule.getBeforeActions()) {
            try {
                stateActions.stream().filter(t -> action.equals(t.getBusinessEvent()))
                        .findFirst().ifPresent(t -> t.beforeActionExecute(context));
            } catch (Exception e) {
                log.error("执行前置动作失败: ", e);
                throw new RuntimeException("前置动作执行失败: ", e);
            }
        }
    }

    private void executeAfterActions(TransitionContext context, StateTransitionRule rule) {
        for (String action : rule.getAfterActions()) {
            try {
                stateActions.stream().filter(t -> action.equals(t.getBusinessEvent()))
                        .findFirst().ifPresent(t -> t.afterActionExecute(context));
            } catch (Exception e) {
                log.error("执行后置动作失败:", e);
            }
        }
    }

    private void notifyBeforeTransition(TransitionContext context) {
        ruleConfigurator.getGlobalListeners().forEach(listener -> {
            try {
                listener.beforeTransition(context);
            } catch (Exception e) {
                log.error("前置监听器执行失败", e);
            }
        });
    }

    private void notifyAfterTransition(TransitionContext context) {
        ruleConfigurator.getGlobalListeners().forEach(listener -> {
            try {
                listener.afterTransition(context);
            } catch (Exception e) {
                log.error("后置监听器执行失败", e);
            }
        });
    }

    private boolean validateTransition(TransitionContext context, List<BaseState> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        for (BaseState object : entityList) {
            ruleConfigurator.checkStateNode(context.getBusinessType(), object.getStatus());
        }
        // 检查条件
        return true;
    }
}
