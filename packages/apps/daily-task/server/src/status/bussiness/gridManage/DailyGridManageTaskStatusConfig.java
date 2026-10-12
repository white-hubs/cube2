package com.guhecloud.cube.biz.daily.task.status.bussiness.gridManage;

import com.guhecloud.cube.biz.daily.task.status.base.BusinessRuleConfigurator;
import com.guhecloud.cube.biz.daily.task.status.bussiness.DailyTaskStateCondition;
import com.guhecloud.cube.biz.daily.task.status.bussiness.DailyTaskStateNodes;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static com.guhecloud.cube.biz.daily.task.commons.constants.DailyConstants.GRID_MANAGE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.CREATE_TARGET;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.DISTRIBUTE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.DRAFT_TASK;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.HANDLE_TARGET;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.ISSUE_TASK;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TASK_COMPLETE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TASK_HANDLE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TASK_TERMINATE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TRANSFER;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/30 16:44
 * @Description:
 */
@Configuration
public class DailyGridManageTaskStatusConfig {

    @Autowired
    private BusinessRuleConfigurator ruleConfigurator;
    @Autowired
    private DailyTaskStateCondition dailyTaskStateCondition;

    @PostConstruct
    public void configureOrderRules() {
        // 注册状态节点
        ruleConfigurator.registerStateNode(GRID_MANAGE, DailyTaskStateNodes.STATE_NODE_LIST);

        // 配置流转规则
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(ISSUE_TASK)
                .afterAction(ISSUE_TASK)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_WAIT)
                .register();
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(DRAFT_TASK)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_DRAFT)
                .register();
        // 处理 ==》完成
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(TASK_HANDLE)
                .fromState(DailyTaskStateNodes.TASK_STATE_NODE_WAIT)
                .beforeAction(TASK_HANDLE)
                .whenToState(dailyTaskStateCondition.completedCondition(), DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .afterAction(TASK_HANDLE)
                .register();
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(TASK_COMPLETE)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .afterAction(TASK_COMPLETE)
                .register();
        // 终止
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(TASK_TERMINATE)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_TERMINATED)
                .afterAction(TASK_TERMINATE)
                .register();
        // 分发 ==》 执行中
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(DISTRIBUTE)
                .fromState(DailyTaskStateNodes.TASK_STATE_NODE_WAIT)
                .whenToState(dailyTaskStateCondition.distributeCompleteStatus(), DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .afterAction(DISTRIBUTE)
                .register();
        //移交 ==》状态不变
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(TRANSFER)
                .afterAction(TRANSFER)
                .register();

        // 对象状态注册
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(CREATE_TARGET)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_WAIT)
                .register();
        ruleConfigurator.configureRule(GRID_MANAGE)
                .onEvent(HANDLE_TARGET)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .completeStatus(DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .afterAction(HANDLE_TARGET)
                .register();
    }
}
