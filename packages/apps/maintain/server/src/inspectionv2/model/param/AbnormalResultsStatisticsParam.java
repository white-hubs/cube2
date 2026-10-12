package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class AbnormalResultsStatisticsParam {
    @NotNull(message = "时间段不能为空！")
    private Period period;
}
