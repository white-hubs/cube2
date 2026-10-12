package com.guhecloud.cube.srv.gen.maintain.param;

import lombok.Data;

import java.util.List;

@Data
public class MaintainTaskTotalParam {
    private String period;
    private List<Long> planIdList;
    /**
     * 是否超时
     */
    private Boolean overTimeBool;
}
