package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmFlameSmogNumMetricDTO {

    /**
     * 明火烟烟雾报警数量
     */
    private Integer flameSmogAlarmNum;

    /**
     * 明火烟烟雾报警占比
     */
    private Double flameSmogAlarmRate;
}
