package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.paran;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-20  15:39
 * @Description:
 * @Version: 1.0
 */
@Data
public class FaultTypeStatisticsParam extends Paging {
    /**
     * 故障类型id
     */
    private Long faultTypeId;
    /**
     * 开始时间-结束时间
     */
    private Period period;
    private String applicationCode;
}
