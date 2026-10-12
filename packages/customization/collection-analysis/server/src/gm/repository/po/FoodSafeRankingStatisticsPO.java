package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("food_safe_ranking_statistics")
@EqualsAndHashCode(callSuper = true)
public class FoodSafeRankingStatisticsPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime timeDay;
    private String timeDayFormat;
    /**
     * 样品名称
     */
    private String sampleName;
    /**
     * 不合格数量
     */
    private Integer failCount;
    /**
     * 时段
     */
    private String timePeriod;
}
