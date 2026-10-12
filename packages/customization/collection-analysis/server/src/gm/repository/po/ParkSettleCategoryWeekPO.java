package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-17  09:37
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("park_settle_category_week")
public class ParkSettleCategoryWeekPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String mainCateGory;
    private String mainCateGoryName;
    private String netWeight;
    private LocalDateTime days;
    private String percent;
}
