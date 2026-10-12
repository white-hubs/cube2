package com.guhecloud.cube.srv.prepaid.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 日用能统计入参
 */
@Data
public class UseDayStatParam {
    @NotNull(message = "时间段不能为空")
    private Period period;
    private String type;
}
