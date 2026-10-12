package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

@Getter
public enum NfcStatusEnum {
    REMOVE("已删除"),
    BOUND("已绑定"),
    UNBOUND("未绑定");
    private String remarks;

    NfcStatusEnum(String remarks) {
        this.remarks = remarks;
    }
}
