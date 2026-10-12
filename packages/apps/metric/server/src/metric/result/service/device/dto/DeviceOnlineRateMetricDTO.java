package com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceOnlineRateMetricDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * 在线数量
     */
    private Integer onlineNum;

    /**
     * 在线率
     */
    private Double onlineRate;
}
