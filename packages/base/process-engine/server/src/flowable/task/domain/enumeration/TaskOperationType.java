package com.guhecloud.cube.base.flow.flowable.task.domain.enumeration;

import lombok.Getter;

/**
 * @author pengjian
 * @date 2022/10/28 16:17
 * @description 任务动作
 */
public enum TaskOperationType {
    SUBMIT("发起"),
    UNDO("撤销"),
    REPEAT("再次发起"),
    APPROVAL("审批"),
    PASS("通过"),
    BACK("退回"),
    WITHDRAW("撤回"),
    COPY("抄送"),
    REJECT("驳回"),
    ABNORMAL("异常"),
    TRANSFER("转交");

    @Getter
    String label;

    TaskOperationType(String label) {
        this.label = label;
    }
}
