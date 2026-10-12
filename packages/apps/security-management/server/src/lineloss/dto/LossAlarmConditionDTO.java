package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class LossAlarmConditionDTO {
    /**
     * 触发条件,符合和值用逗号隔开，多组条件用分号隔开即：符合,值;符合,值(例如：EQ,10;GE,12)
     */
    @NotNull(message = "触发条件不能为空")
    private String condition;
    @NotEmpty(message = "线路不能为空")
    private List<Long> lineIdList;
}
