package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排班规则分页列表入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttSchedulingRulePageParam extends Paging {

    private Long id;
    /**
     * 排班规则名称
     */
    private String name;
}
