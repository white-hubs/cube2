package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("food_safe_day_statistics")
@EqualsAndHashCode(callSuper = true)
public class FoodSafeDayStatisticsPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime timeDay;
    private String timeDayFormat;
    /**
     * 检测次数
     */
    private Integer testCount;
    /**
     * 合格次数
     */
    private Integer passCount;
    /**
     * 合格次数
     */
    private Integer failCount;
    /**
     * 合格率
     */
    private String passPercent;
}
