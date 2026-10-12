package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  15:33
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum WorkOvertimeResultEnum {
    OK("成功"),
    ANALYSIS("加班审批通过后,消费异常"),
    WORK_OVERTIME_ANALYSIS("解析加班规则异常"),
    LEAVE_EXCEPTION("计入调休异常"),
    LEAVE_OK("计入调休成功"),
    LEAVE_USER_CHECK("人员、加班时长不能为空"),
    LEAVE_TYPE_CHECK("没有配置假期"),
    ;
    private final String remarks;
    public static final List<String> clockType = new ArrayList<>();
    public static final List<String> clockState = new ArrayList<>();

    WorkOvertimeResultEnum(String remarks) {
        this.remarks = remarks;
    }
}
