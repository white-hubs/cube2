package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadOnlineRateMetricDTO {

    /**
     * 总数
     */
    private Integer deviceTotalNumber;

    /**
     * 离线数量
     */
    private Integer offlineNumber;

    /**
     * 在线数量
     */
    private Integer onlineNum;

    /**
     * 在线率
     */
    private Double onlineRate;

    /**
     * 离线率
     */
    private Double offlineRate;

    public static HeadOnlineRateMetricDTO getDefault() {
        return new HeadOnlineRateMetricDTO(0, 0, 0, 0.0, 0.0);
    }
}
