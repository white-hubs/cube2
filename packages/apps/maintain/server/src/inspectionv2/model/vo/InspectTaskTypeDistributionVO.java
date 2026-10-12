package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡检类型分布。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectTaskTypeDistributionVO {

    /**
     * 巡检类型 id。
     */
    private Long typeId;

    /**
     * 巡检类型名称。
     */
    private String typeName;

    /**
     * 任务数。
     */
    private Long taskTotal;
}
