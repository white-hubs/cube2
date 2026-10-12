package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("vehicle_load_week_statistics")
@EqualsAndHashCode(callSuper = true)
public class VehicleLoadWeekStatisticsPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime timeDay;
    private String timeDayFormat;
    /**
     * 车次数
     */
    private Integer carCount;
    /**
     * 重量千克
     */
    private BigDecimal weightCount;
    /**
     * 日期
     */
    private String days;
}
