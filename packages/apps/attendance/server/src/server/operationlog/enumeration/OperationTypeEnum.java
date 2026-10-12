package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.enumeration;

import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author wql
 * @describe 操作类型
 * @date 2023/6/12 15:20
 */
@Getter
public enum OperationTypeEnum {
    ADD("新增"),
    UPDATE("编辑"),
    DELETE("删除");
    private final String value;

    OperationTypeEnum(String value) {
        this.value = value;
    }

    public static String getValueByCode(String code) {
        return Arrays.stream(OperationTypeEnum.values()).filter(e -> e.name().equals(code)).findFirst().orElseThrow(
                () -> new ResourceNotFoundException("枚举类型错误，不存在 " + code + " 的枚举")
                                                                                                                   ).value;
    }

}
