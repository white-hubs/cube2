package com.guhecloud.cube.srv.gen.safety.management.dto.request.query.alarm;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import lombok.Getter;

import java.util.List;

@Getter
public class AlarmMetricQuery extends BaseStatisticsQuery {
    private List<String> applicationCodes;

    private List<String> statusCodes;
}
