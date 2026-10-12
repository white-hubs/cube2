package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 值班分页列表入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttDutyPageParam extends Paging {
    /**
     * 月份
     */
    @NotBlank
    private String yearMonth;

    /**
     * 员工名称关键字
     */
    private String keyword;
}
