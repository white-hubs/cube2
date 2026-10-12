package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectPlanStatisticsParam extends Paging {

    /**
     * 查询时间，按任务生成时间过滤。
     */
    private Period period;

    /**
     * 巡检类型 id。
     */
    private Long typeId;

    /**
     * 计划名称或临时任务名称。
     */
    private String name;
}
