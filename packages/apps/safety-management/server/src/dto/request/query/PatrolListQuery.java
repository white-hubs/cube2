package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 电子巡更列表详情查询参数
 * @date 2023/9/7 20:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PatrolListQuery extends Paging {
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
     * 巡更结果
     */
    private String resultCode;
}
