package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework;

import lombok.Data;

@Data
public class LooseWorksMarketStatusVO {
    /**
     * 市场租户编号
     */
    private Integer marketTenant;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 占比
     */
    private String status;
    private Integer count;
}
