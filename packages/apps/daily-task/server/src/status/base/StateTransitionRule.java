package com.guhecloud.cube.biz.daily.task.status.base;

import com.guhecloud.platform.base.util.MapUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 10:47
 * @Description: 状态流转规则
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StateTransitionRule {
    private String ruleId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 操作
     */
    private String event;

    /**
     * 源状态
     */
    private StateNode fromState;

    /**
     * 目标状态
     */
    private StateNode toState;
    /**
     * 目标状态
     */
    private Map<StateCondition, StateNode> toStateMap;

    /**
     * 初始状态
     */
    private StateNode initStatus;

    /**
     * 结束状态
     */
    private StateNode endStatus;

    private List<String> beforeActions;
    private List<String> afterActions;

    private int priority;

    public static class RuleBuilder {
        private final BusinessRuleConfigurator configurator;
        private final String businessType;
        private String event;
        private StateNode fromState;
        private StateNode toState;
        private StateNode endStatus;
        private final Map<StateCondition, StateNode> toStateMap = new HashMap<>();
        private final List<String> beforeActions = new ArrayList<>();
        private final List<String> afterActions = new ArrayList<>();
        private int priority = 1;

        public RuleBuilder(BusinessRuleConfigurator configurator, String businessType) {
            this.configurator = configurator;
            this.businessType = businessType;
        }

        public RuleBuilder onEvent(String event) {
            this.event = event;
            return this;
        }

        public RuleBuilder fromState(StateNode fromState) {
            this.fromState = fromState;
            return this;
        }

        public RuleBuilder whenToState(StateCondition condition, StateNode toState) {
            this.toStateMap.put(condition, toState);
            return this;
        }

        public RuleBuilder beforeAction(String action) {
            this.beforeActions.add(action);
            return this;
        }

        public RuleBuilder afterAction(String action) {
            this.afterActions.add(action);
            return this;
        }

        public RuleBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public void register() {
            if (event == null && fromState == null && MapUtils.isEmpty(toStateMap)) {
                throw new IllegalArgumentException("状态流转规则配置不完整");
            }
            StateTransitionRule rule = StateTransitionRule.builder()
                    .event(event)
                    .fromState(fromState)
                    .toState(toState)
                    .toStateMap(toStateMap)
                    .endStatus(endStatus)
                    .beforeActions(beforeActions)
                    .afterActions(afterActions)
                    .priority(priority)
                    .build();
            configurator.registerRule(businessType, rule);
        }

        public RuleBuilder toState(StateNode toState) {
            this.toState = toState;
            return this;
        }

        public RuleBuilder completeStatus(StateNode toState) {
            this.endStatus = toState;
            return this;
        }
    }
}
