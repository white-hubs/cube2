package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 环境检测小时统计表
 */
@Data
@TableName("environment_hour_stat")
@EqualsAndHashCode(callSuper = true)
public class EnvironmentHourStat extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 时间
     */
    private LocalDateTime statTime;
    /**
     * 字符串的日期(yyyy-MM-dd)
     */
    private String statDay;
    /**
     * PM2.5
     */
    private String pmTwoPointFive;
}
