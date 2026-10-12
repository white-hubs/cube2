package com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class MaintainTaskTotalParam {
    private Period period;
    private List<Long> planIdList;
    /**
     * 是否超时
     */
    private Boolean overTimeBool;
}
