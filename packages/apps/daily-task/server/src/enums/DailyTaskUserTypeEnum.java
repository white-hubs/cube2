package com.guhecloud.cube.biz.daily.task.enums;

import com.guhecloud.cube.biz.daily.task.status.bussiness.DailyTaskStateNodes;
import lombok.Getter;

/**
 * 任务人员类型
 */
@Getter
public enum DailyTaskUserTypeEnum {
    SUPERINTENDENT("负责人"),
    ASSIGNEE("执行人"),
    ;
    private final String value;

    DailyTaskUserTypeEnum(String value) {
        this.value = value;
    }

    public static String getQueryType(String status) {
        if (DailyTaskStateNodes.TASK_STATE_NODE_DRAFT.getStateCode().equals(status)) {
            return SUPERINTENDENT.name();
        }
        return ASSIGNEE.name();
    }
}
