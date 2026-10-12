package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
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
@TableName("monthly_report")
public class MonthlyReportPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId
    private Long id;
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
}
