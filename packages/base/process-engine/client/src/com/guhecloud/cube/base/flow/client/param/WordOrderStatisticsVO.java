package com.guhecloud.cube.base.flow.client.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/18 20:06
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WordOrderStatisticsVO extends Paging {
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 业务集合
     */
    private String businessTypes;
    /**
     * 开始时间-结束时间
     */
    private Period period;
}
