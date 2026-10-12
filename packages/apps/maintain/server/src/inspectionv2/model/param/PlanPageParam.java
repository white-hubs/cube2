package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.PlanExecStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡检计划分页入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanPageParam extends Paging {
    /**
     * 关键字
     */
    private String keywords;

    /**
     * 计划状态
     */
    private PlanExecStatusEnum status;

    /**
     * 巡检类型Id
     */
    private Long typeId;

    private Long planId;
    private Long taskId;
    private Long targetId;

    private String targetCategory;
}
