package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("market_monthly_report_url")
public class MarketMonthlyReportUrlPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 市场名称
     */
    private Long marketId;

    /**
     * 市场编号
     */
    @Unique(composites = { "deleted" }, message = "市场已存在")
    private Integer marketTenant;

    /**
     * 填报地址
     */
    private String reportUrl;

    /**
     * 开关状态
     */
    private Boolean enabled;
}
