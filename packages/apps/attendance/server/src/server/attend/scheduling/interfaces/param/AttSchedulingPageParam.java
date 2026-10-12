package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 排班列表条件入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttSchedulingPageParam extends Paging {
    /**
     * 考勤组id
     */
    private Long groupId;
    /**
     * 月份
     */
    @NotBlank(message = "月份不能为空")
    private String yearMonth;
    /**
     * 人员名称
     */
    private String keyword;
}
