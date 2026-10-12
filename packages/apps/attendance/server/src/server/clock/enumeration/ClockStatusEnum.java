package com.guhecloud.cube.srv.gen.attendance.management.server.clock.enumeration;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.Getter;

import java.util.Arrays;

/**
 * 打卡异常枚举
 *
 * @author appet
 * @date 2022/11/28
 */
@Getter
public enum ClockStatusEnum {
    EXP_LATE("迟到"),
    EXP_LEAVE_EARLY("早退"),
    EXP_NOCARD("缺卡"),
    EXP_SKIP_WORK("旷工"),
    EXP_LATE_LEAVE_EARLY("迟到、早退"),
    EXP_LATE_NOCARD("迟到、缺卡"),
    EXP_NOCARD_LEAVE_EARLY("缺卡、早退"),
    NORMAL("正常");

    private final String value;

    ClockStatusEnum(String value) {
        this.value = value;
    }

    public static String getValueByCode(String code) {
        return Arrays.stream(ClockStatusEnum.values()).filter(e -> e.name().equals(code)).findFirst().orElseThrow(
                () -> new ServiceException("枚举类型错误，不存在 " + code + " 的枚举")
                                                                                                                 ).value;
    }
}
