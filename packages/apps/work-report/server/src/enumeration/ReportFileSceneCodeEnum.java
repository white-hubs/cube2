package com.guhecloud.cube.srv.gen.reportmanagement.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/17 9:39
 * @Description:
 */

@Getter
public enum ReportFileSceneCodeEnum {

    WORK_REPORT("WORK_REPORT", "工作汇报"),
    WORK_PLAN("WORK_PLAN", "工作计划"),
    ;
    private final String code;
    private final String desc;

    ReportFileSceneCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
