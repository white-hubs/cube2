package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request;

import lombok.Data;

@Data
public class MarketMonthlyReportUrlModifyRequest {

    /**
     * 市场编号
     */
    private Integer marketTenant;

    /**
     * 市场配置主键
     */
    private Long marketId;

    /**
     * 填报地址
     */
    private String reportUrl;

}
