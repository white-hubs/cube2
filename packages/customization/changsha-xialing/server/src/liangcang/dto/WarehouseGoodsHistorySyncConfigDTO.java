package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 粮仓货量历史同步配置（系统字典 variable）。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseGoodsHistorySyncConfigDTO {
    /**
     * 租户 id
     */
    private Integer tenant;
    /**
     * 查询近多少分钟的数据
     */
    private Long queryMinutes;
}
