package com.guhecloud.cube.srv.prepaid.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 日用水分页条件入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WaterUseDayPageParam extends Paging {
    /**
     * 水表编号
     */
    private String meterId;

    /**
     * 时间段
     */
    private Period period;
}
