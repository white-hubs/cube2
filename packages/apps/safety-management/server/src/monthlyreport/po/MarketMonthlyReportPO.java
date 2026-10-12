package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 市场月报
 *
 * @Author: lijie
 * @CreateTime: 2023-09-07  16:36
 * @Description:
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("market_monthly_report")
public class MarketMonthlyReportPO extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 总部月报主键
     */
    private Long monthlyReportId;

    /**
     * 标题
     */
    private String title;

    /**
     * 月报月份
     */
    private String monthly;
    /**
     * 月报时间
     */
    private LocalDateTime monthDate;
    /**
     * 填报状态
     */
    private String status;

    /**
     * 填报数据
     */
    private String reportData;

    /**
     * 上报人
     */
    private Long reportUserId;
    /**
     * 上报时间
     */
    private LocalDateTime reportTime;

    /**
     * 文件地址
     */
    private String reportUrl;

    /**
     * 文件id
     */
    private Long reportId;

    /**
     * 文件名称
     */
    private String reportName;

    /**
     * 市场租户
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 租户编号
     */
    private Integer tenant;
}
