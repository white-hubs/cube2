package com.guhecloud.cube.biz.daily.task.status.base;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.EVENT_TABLE_MAP;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 14:03
 * @Description:
 */
@Component
@Slf4j
public class BusinessRuleConfigurator {
    private final Map<String, List<StateTransitionRule>> businessRules = new ConcurrentHashMap<>();
    private final Map<String, List<StateNode>> stateNodes = new ConcurrentHashMap<>();
    @Autowired
    private List<TaskStateListener> globalListeners;

    /**
     * 注册状态节点
     */
    public void registerStateNode(String businessType, List<StateNode> stateNodeList) {
        stateNodes.put(businessType, stateNodeList);
    }

    /**
     * 配置流转规则
     */
    public StateTransitionRule.RuleBuilder configureRule(String businessType) {
        return new StateTransitionRule.RuleBuilder(this, businessType);
    }

    /**
     * 添加全局监听器
     */
    public void addGlobalListener(TaskStateListener listener) {
        globalListeners.add(listener);
        log.info("添加全局状态监听器: {}", listener.getClass().getSimpleName());
    }

    /**
     * 获取业务类型的规则
     */
    public List<StateTransitionRule> getRules(String businessType) {
        return businessRules.getOrDefault(businessType, new ArrayList<>());
    }

    /**
     * 获取状态提供器
     */
    public String getStateProvider(String event) {
        if (!EVENT_TABLE_MAP.containsKey(event)) {
            throw new ServiceException("未注册的状态提供器: " + event);
        }
        return EVENT_TABLE_MAP.get(event);
    }

    /**
     * 获取全局监听器
     */
    public List<TaskStateListener> getGlobalListeners() {
        return new ArrayList<>(globalListeners);
    }

    // 内部方法 - 注册规则
    void registerRule(String businessType, StateTransitionRule rule) {
        businessRules.computeIfAbsent(businessType, k -> new ArrayList<>())
                .add(rule);

        // 按优先级排序
        businessRules.get(businessType)
                .sort(Comparator.comparingInt(StateTransitionRule::getPriority));

    }

    /**
     * 状态节点校验
     *
     * @param businessType
     * @param toState
     */
    public void checkStateNode(String businessType, String toState) {
        if (!stateNodes.containsKey(businessType)) {
            throw new IllegalArgumentException("未注册的状态节点: " + businessType);
        }

        if (stateNodes.get(businessType).stream().noneMatch(t -> t.getStateCode().equals(toState))) {
            throw new IllegalArgumentException("未注册的状态节点: " + toState);
        }
    }

    public StateNode getInitState(String businessType) {
        return stateNodes.get(businessType).stream().filter(StateNode::isInitial)
                .findFirst().orElse(null);
    }

    public String getCompleteState(String businessType, String event) {
        Optional<StateTransitionRule> first = getRules(businessType).stream().filter(r -> r.getEvent().equals(event))
                .findFirst();
        if (first.isEmpty() || Objects.isNull(first.get().getEndStatus())) {
            throw new ServiceException("未配置完成状态");
        }
        return first.map(t -> t.getEndStatus().getStateCode()).orElse(null);

    }

    public List<String> getNoFinalStatus(String businessType) {

        return stateNodes.get(businessType).stream().filter(t -> !t.isFinal())
                .map(StateNode::getStateCode).distinct().collect((Collectors.toList()));
    }
}