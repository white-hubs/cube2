package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 区域人流量趋势（一行一个区域的一个时间点，与导出结构一致）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionAreaPersonTrendStatVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 时间点（格式由查询参数 type 等决定，与全量趋势接口一致）
     */
    private String period;

    /**
     * 人流量
     */
    private Integer totalPersonNumber;
}
