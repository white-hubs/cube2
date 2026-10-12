package com.guhecloud.cube.biz.daily.task.status.bussiness;

import com.guhecloud.cube.biz.daily.task.status.base.BusinessRuleConfigurator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static com.guhecloud.cube.biz.daily.task.commons.constants.DailyConstants.DAILY_TASK;
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
 * @Date: 2025/11/11 15:24
 * @Description: 日常任务状态配置
 * 1、业务端调用接口实现任务状态变换，需要传入业务类型（巡检、巡更、日常任务等），表是相同的，则只需要传入任务id即可，
 * 同时可传参数，规则通过参数变量替换实现状态自动流转逻辑
 * 2、业务端定义不同大类型任务的流转规则
 * eg：1、状态为空、识别规则定义的初始状态
 * 2、状态不为空，获取当前状态，根据condition优先级判断，
 * <p>
 * 下发 ->无condition-> 待处理状态
 * 处理 ->有无对象、 无对象-> 完成
 * -> 有对象 -> 处理中
 * -> 有对象，当处理数等于任务数时，任务完成
 * -> 主任务，当子任务完成数等于子任务数、任务完成
 * -> 父任务完成、子任务完成，
 * 分发 -> 无condition -> 待处理状态
 *
 *
 */
@Configuration
public class DailyTaskStatusConfig {

    @Autowired
    private BusinessRuleConfigurator ruleConfigurator;
    @Autowired
    private DailyTaskStateCondition dailyTaskStateCondition;

    @PostConstruct
    public void configureOrderRules() {
        // 注册状态节点
        ruleConfigurator.registerStateNode(DAILY_TASK, DailyTaskStateNodes.STATE_NODE_LIST);

        // 配置流转规则
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(ISSUE_TASK)
                .afterAction(ISSUE_TASK)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_WAIT)
                .register();
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(DRAFT_TASK)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_DRAFT)
                .register();
        // 处理 ==》完成
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(TASK_HANDLE)
                .fromState(DailyTaskStateNodes.TASK_STATE_NODE_WAIT)
                .beforeAction(TASK_HANDLE)
                .whenToState(dailyTaskStateCondition.completedCondition(), DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .afterAction(TASK_HANDLE)
                .register();

        // 终止
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(TASK_TERMINATE)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_TERMINATED)
                .afterAction(TASK_TERMINATE)
                .register();
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(TASK_COMPLETE)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .afterAction(TASK_COMPLETE)
                .register();
        // 分发 ==》 执行中
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(DISTRIBUTE)
                .fromState(DailyTaskStateNodes.TASK_STATE_NODE_WAIT)
                .whenToState(dailyTaskStateCondition.distributeCompleteStatus(), DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .afterAction(DISTRIBUTE)
                .register();
        //移交 ==》状态不变
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(TRANSFER)
                .afterAction(TRANSFER)
                .register();

        // 对象状态注册
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(CREATE_TARGET)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_WAIT)
                .register();
        ruleConfigurator.configureRule(DAILY_TASK)
                .onEvent(HANDLE_TARGET)
                .toState(DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .completeStatus(DailyTaskStateNodes.TASK_STATE_NODE_DONE)
                .afterAction(HANDLE_TARGET)
                .register();
    }
}
