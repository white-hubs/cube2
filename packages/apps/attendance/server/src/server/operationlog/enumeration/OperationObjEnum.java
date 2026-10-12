package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.enumeration;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author wql
 * @describe 操作对象枚举
 * @date 2023/6/12 15:25
 */
@Getter
public enum OperationObjEnum {
    ATTENDANCE_SECTION("考勤组"),
    CLASSES("班次"),
    ARRANGE_WORK("人员排班"),
    BE_ON_DUTY("人员值班"),
    HOLIDAY_BALANCE("假期余额"),
    HOLIDAY_TYPE("假期类型"),
    SCHEDULING_RULE("排班规则"),
    OVERTIME_RULE("加班规则"),
    SUPPLEMENT_CARD_RULE("补卡规则"),
    AWAY_RULE("出差规则"),
    POST_RULE("离岗规则");
    private final String value;

    OperationObjEnum(String value) {
        this.value = value;
    }

    public static String getValueByCode(String code) {
        return Arrays.stream(OperationObjEnum.values()).filter(e -> e.name().equals(code)).findFirst().orElseThrow(
                () -> new ServiceException("枚举类型错误，不存在 " + code + " 的枚举")
                                                                                                                  ).value;
    }
}
