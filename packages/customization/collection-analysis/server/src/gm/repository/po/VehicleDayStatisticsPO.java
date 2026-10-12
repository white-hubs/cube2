package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("vehicle_day_statistics")
@EqualsAndHashCode(callSuper = true)
public class VehicleDayStatisticsPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime timeHour;
    private String timeHourFormat;
    private LocalDateTime timeDay;
    private String timeDayFormat;
    /**
     * 来车类型(VehicleNatureEnum)
     * 0：总来车
     * 1：来货车
     * 3：采购车
     * 4：其他
     */
    private String inComingType;
    /**
     * 车辆性质枚举
     */
    private String vehicleNature;
    /**
     * 1：进场
     * 2：出场
     */
    private String bussType;
    private String dateType;
    /**
     * 小时
     */
    private String dateHour;
    /**
     * 车次
     */
    private Integer carCount;
}
