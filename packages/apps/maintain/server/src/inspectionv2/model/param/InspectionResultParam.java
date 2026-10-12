package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 巡检结果统计分析请求参数
 * @date 2023/4/14 14:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InspectionResultParam extends Paging {
    /**
     * 巡检结果
     */
    private String result;
    /**
     * 巡检类型
     */
    private Long typeId;
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 任务名称
     */
    private String taskName;
}
