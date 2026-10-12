package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 假期余额分页列表入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttHolidayBalancePageParam extends Paging {
    /**
     * 人员名称
     */
    private String staffName;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 考勤组id
     */
    private Long groupId;
    /**
     * 是否是覆盖模板(非true为增量模板)
     */
    private Boolean initTemplate;
    /**
     * 下载的模板需要包含哪些假期
     */
    private List<Long> holidayIdList;
}
