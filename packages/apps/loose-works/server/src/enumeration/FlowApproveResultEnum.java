package com.guhecloud.cube.srv.gen.loose.works.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 流程审批结果
 * @date 2023/9/15 16:42
 */
@Getter
public enum FlowApproveResultEnum {
    PASS("通过"),
    UNDO("撤销"),
    ABNORMAL("异常"),
    REJECT("驳回"),
    ACTIVE("处理中");
    final String describe;

    FlowApproveResultEnum(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}
