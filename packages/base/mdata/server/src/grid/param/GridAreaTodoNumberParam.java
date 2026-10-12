package com.guhecloud.cube.biz.mdata.server.grid.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class GridAreaTodoNumberParam {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务类型(大的类型例如巡检、日常、网格任务)
     */
    private String dailyType;

    private Period period;
}
