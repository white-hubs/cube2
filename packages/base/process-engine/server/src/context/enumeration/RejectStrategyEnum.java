package com.guhecloud.cube.base.flow.context.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/1 10:58
 * @Description: 驳回策略
 */

@Getter
public enum RejectStrategyEnum {
    TO_INITIATOR("驳回到发起人"),
    TO_PREV_NODE("驳回到上一节点"),
    TO_SELECT_NODE("驳回到指定节点"),
    TO_REJECT("终止流程"),
    ;
    private final String desc;

    public void doReject() {

    }

    RejectStrategyEnum(String desc) {

        this.desc = desc;
    }
}
