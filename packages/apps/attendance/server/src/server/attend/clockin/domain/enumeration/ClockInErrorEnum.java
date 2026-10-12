package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  15:33
 * @Description: 打卡失败原因
 * @Version: 1.0
 */
@Getter
public enum ClockInErrorEnum {
    NO_CLASS("无班次"),
    NO_GROPU("无考勤组"),
    EXCEED_CHECK_IN_TIME("未在打卡时间内"),
    SYSTEM_ERROR("系统异常"),
    ERROR_MESSAGE("获取异常信息错误"),
    ;
    private final String remarks;

    ClockInErrorEnum(String remarks) {
        this.remarks = remarks;
    }

    public static String getRemarksByName(String name) {
        String remarks = "";
        if (ObjectUtils.isEmpty(name)) {
            return remarks;
        }
        ClockInErrorEnum[] clock = ClockInErrorEnum.values();
        for (ClockInErrorEnum ex : clock) {
            if (name.equals(ex.name())) {
                return ex.getRemarks();
            }
        }
        return remarks;
    }
}
