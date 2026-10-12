package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务标准项异常率查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskStandardAbnormalRateParam extends BaseInspectStatisticsParam {

    /**
     * 标准名称
     */
    private String standardName;
}
