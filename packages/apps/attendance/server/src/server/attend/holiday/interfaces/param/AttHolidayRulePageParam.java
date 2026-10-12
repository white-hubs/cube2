package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 假期规则分页入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttHolidayRulePageParam extends Paging {
    /**
     * 假期名称
     */
    private String name;
    /**
     * 假期类型
     */
    private String holidayType;
    /**
     * 是否计入缺勤
     */
    private Boolean intoAbsence;
    /**
     * 是否限额
     */
    private Boolean notLimit;
}
