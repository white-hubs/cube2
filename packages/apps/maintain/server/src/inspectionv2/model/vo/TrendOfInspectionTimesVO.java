package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 巡检次数趋势
 * @date 2023/4/18 10:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendOfInspectionTimesVO {
    /**
     * 横坐标
     */
    private String abscissa;
    /**
     * 巡检任务数
     */
    private Long taskNum;
    /**
     * 巡检次数
     */
    private Long numberOfInspection;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TrendOfInspectionTimesVO(LocalDateTime startTime, LocalDateTime endTime, String abscissa) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.abscissa = abscissa;
    }
}
