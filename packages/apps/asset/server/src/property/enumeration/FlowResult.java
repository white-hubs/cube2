package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import lombok.Getter;

@Getter
public enum FlowResult {
    UNDER("审批中"),
    PASS("通过"),
    REJECT("拒绝"),
    UNDO("撤销");
    private final String remarks;

    FlowResult(String remarks) {
        this.remarks = remarks;
    }
}
