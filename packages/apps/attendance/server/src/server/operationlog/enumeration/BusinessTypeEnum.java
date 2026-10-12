package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.enumeration;

import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author wql
 * @describe 业务应用枚举
 * @date 2023/6/12 15:23
 */
@Getter
public enum BusinessTypeEnum {
    ATTENDANCE_MANAGER("考勤管理"),
    ALL("全部");

    private final String value;

    BusinessTypeEnum(String value) {
        this.value = value;
    }

    public static String getValueByCode(String code) {
        return Arrays.stream(BusinessTypeEnum.values()).filter(e -> e.name().equals(code)).findFirst().orElseThrow(
                () -> new ResourceNotFoundException("枚举类型错误，不存在 " + code + " 的枚举")
                                                                                                                  ).value;
    }
}
