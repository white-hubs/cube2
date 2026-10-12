package com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatrolPlanPageParam extends Paging {
    /**
     * 计划名称
     */
    private String keyword;
    /**
     * 时间段
     */
    private String executeData;
    /**
     * 周期
     */
    private String patrolCycle;
    /**
     * 状态
     */
    private String status;
}
