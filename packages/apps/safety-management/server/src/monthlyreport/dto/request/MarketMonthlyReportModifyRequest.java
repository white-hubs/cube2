package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 安全月报填写记录保存
 *
 * @author lanchun
 * @date 2023/9/20 17:28s
 */
@Data
public class MarketMonthlyReportModifyRequest {

    /**
     * 市场月报主键
     */
    @NotNull(message = "市场月报主键不能小于1")
    private Long id;

    /**
     * 保存的数据JSON
     */
    private String reportData;
}
