package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 安防设备报警统计
 *
 * @author lanchun
 * @date 2022/11/22 10:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDeviceAlarmStatisticsVO {

    /**
     * 设备数量
     */
    private Long deviceNum;

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 环比率
     */
    private String ringRatio;
}
