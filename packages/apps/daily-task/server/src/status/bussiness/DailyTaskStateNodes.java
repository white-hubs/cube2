package com.guhecloud.cube.biz.daily.task.status.bussiness;

import com.guhecloud.cube.biz.daily.task.status.base.StateNode;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 14:47
 * @Description: 日常任务状态集合
 */
public class DailyTaskStateNodes {

    /**
     * 待处理
     */
    public static final StateNode TASK_STATE_NODE_WAIT = new StateNode("WAIT", "待处理", true, false);

    /**
     * 处理中(废弃)
     */
    @Deprecated
    public static final StateNode TASK_STATE_NODE_PROCESSING = new StateNode("PROCESSING", "处理中", false, false);

    /**
     * 已完成
     */
    public static final StateNode TASK_STATE_NODE_DONE = new StateNode("DONE", "已完成", false, true);

    /**
     * 已终止
     */
    public static final StateNode TASK_STATE_NODE_TERMINATED = new StateNode("TERMINATED", "已终止", false, true);

    /**
     * 草稿
     */
    public static final StateNode TASK_STATE_NODE_DRAFT = new StateNode("DRAFT", "草稿", false, true);

    public static final List<StateNode> STATE_NODE_LIST = Arrays.asList(TASK_STATE_NODE_WAIT, TASK_STATE_NODE_PROCESSING, TASK_STATE_NODE_DONE, TASK_STATE_NODE_TERMINATED, TASK_STATE_NODE_DRAFT);

    public static StateNode getStateNode(String state) {
        return STATE_NODE_LIST.stream().filter(node -> StringUtils.equals(node.getStateCode(), state)).findFirst().orElse(null);
    }

    /**
     * 获取当前任务类型任务状态集合
     */
    public static List<String> getTaskStateList() {
        return STATE_NODE_LIST.stream().map(StateNode::getStateName).toList();
    }

    public static boolean checkTransfer(String status) {
        return TASK_STATE_NODE_DONE.getStateCode().equals(status) || TASK_STATE_NODE_TERMINATED.getStateCode().equals(status);
    }

    public static List<String> getNoticeMessageStatus(Set<String> businessTypes) {

        return STATE_NODE_LIST.stream().filter(t -> !t.isFinal())
                .map(StateNode::getStateCode).distinct().collect(Collectors.toList());
    }
}
