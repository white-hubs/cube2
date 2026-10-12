package com.guhecloud.cube.srv.gen.maintain.server.common.vo.om;

import lombok.Builder;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-04 15:24
 */
@Data
@Builder
public class MaintenanceStatisticsVo {
    /**
     * 天
     */
    private String date;
    /**
     * 维护数量
     */
    private Long num;
}
