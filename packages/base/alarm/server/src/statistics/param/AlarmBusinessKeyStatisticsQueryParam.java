package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class AlarmBusinessKeyStatisticsQueryParam {

    /**
     * 业务主键
     */
    @NotEmpty(message = "业务主键不能为空")
    @Size(max = 20, message = "业务主键不能大于20")
    private Long businessKey;

    /**
     * 时间条件
     */
    private Period period;

    private String businessType;

    /**
     * 应用编号
     */
    private String applicationCode;
}
