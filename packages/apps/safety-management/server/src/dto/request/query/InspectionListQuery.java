package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 安全巡检列表详情查询参数
 * @date 2023/9/7 20:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InspectionListQuery extends Paging {
    /**
     * 查询时间范围
     */
    private Period period;
    /**
     * 所属市场
     */
    private Integer marketTenant;
    /**
     * 状态
     */
    private String state;
    /**
     * 巡检类型
     */
    private String typeName;
    /**
     * 是否异常
     */
    private Boolean abnormal;

    /**
     * 执行时间范围
     */
    private Period execPeriod;
    /**
     * 巡检业务类型
     */

    private String businessType;
}
