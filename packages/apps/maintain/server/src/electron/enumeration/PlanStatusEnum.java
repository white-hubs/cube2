package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

@Getter
public enum PlanStatusEnum {
    ENABLE("执行中"),
    STOP("已停用"),
    END("已结束"),
    ;
    private String remarks;

    PlanStatusEnum(String remarks) {
        this.remarks = remarks;
    }
}
