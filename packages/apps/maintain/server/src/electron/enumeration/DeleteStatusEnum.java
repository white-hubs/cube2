package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

@Getter
public enum DeleteStatusEnum {
    REMOVE("已删除"),
    NO_REMOVE("未删除");
    private String remarks;

    DeleteStatusEnum(String remarks) {
        this.remarks = remarks;
    }
}
