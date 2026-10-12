package com.guhecloud.cube.srv.gen.safety.management.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-10  11:46
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("safety_report")
public class AlarmSafetyReport extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    private Long fileId;
}
