package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("vehicle_week_statistics")
@EqualsAndHashCode(callSuper = true)
public class VehicleWeekStatisticsPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime timeDay;
    private String timeDayFormat;
    /**
     * 车辆性质
     */
    private String vehicleNature;
    /**
     * 车次数
     */
    private Integer carSum;
    /**
     * 来车类型
     */
    private String inComingCarType;
    /**
     * 日期
     */
    private String days;
}
