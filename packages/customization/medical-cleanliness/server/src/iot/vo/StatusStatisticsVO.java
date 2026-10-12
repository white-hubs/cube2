package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-23  15:11
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
public class StatusStatisticsVO {
    /**
     * 总数
     */
    private Long total;
    /**
     * 正常
     */
    private Long normalTotal;
    /**
     * 异常
     */
    private Long abnormalTotal;
    /**
     * 无效
     */
    private Long invalidTotal;
}
