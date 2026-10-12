package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo;

import lombok.Data;

@Data
public class MarketMonthlyReportUrlVO {

    private Long id;

    /**
     * 市场配置主键
     */
    private Long marketId;

    /**
     * 市场编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 填报地址
     */
    private String reportUrl;

    /**
     * 开关状态
     */
    private Boolean enabled;
}
