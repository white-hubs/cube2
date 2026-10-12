package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.Data;

/**
 * 报警排名查询参数
 *
 * @author lanchun
 * @date 2023/9/8 11:46
 */
@Data
public class AlarmRankStatisticsQuery extends AlarmStatisticsQuery {

    /**
     * 报警数:ALARM_ALL,处理数量:ALARM_HANDLER,响应数量:ALARM_PROGRESS,超时数量:ALARM_TIMEOUT,未处理数量:ALARM_UN_HANDLER
     */
    private String stateRange;
}
