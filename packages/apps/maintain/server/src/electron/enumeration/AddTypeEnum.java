package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 巡更记录来源类型
 * @date 2023/5/29 14:52
 */
@Getter
public enum AddTypeEnum {
    PLAN("计划"),
    TASK("任务");
    private String remarks;

    AddTypeEnum(String remarks) {
        this.remarks = remarks;
    }

}
