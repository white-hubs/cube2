package com.guhecloud.cube.biz.metric.server.metric.result.controller.dto;

import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmEventTypeNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmImpracticalRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmLevelNumberMetricDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmCommonMcpDTO {

    /**
     * 报警总数
     */
    private Integer alarmNumber;

    /**
     * 实报误报数量
     */
    private AlarmImpracticalRateMetricDTO impracticalRate;

    /**
     * 报警类型数量
     */
    private List<AlarmEventTypeNumberMetricDTO> eventTypeNumbers;

    /**
     * 级别数量
     */
    private List<AlarmLevelNumberMetricDTO> levelNumbers;
}
