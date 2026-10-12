package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengjian
 * @date 2022/11/21 11:02
 * @description 新员工请假限制
 */
@Getter
@AllArgsConstructor
public class NewEmployeeLeaveRule {
    /**
     * 入职时长限制 只支持<=
     */
    private Integer timeOfInduction;

    /**
     * @param currentTimeOfInduction 当前入职时长
     *
     * @return true 满足
     */
    public boolean verify(Integer currentTimeOfInduction) {
        if (this.timeOfInduction == 0) {
            return true;
        } else {
            return this.timeOfInduction <= currentTimeOfInduction;
        }
    }
}
