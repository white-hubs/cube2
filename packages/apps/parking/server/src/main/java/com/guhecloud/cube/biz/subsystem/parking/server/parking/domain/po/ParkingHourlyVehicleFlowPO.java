package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 停车场设备小时车流记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("parking_hourly_vehicle_flow")
public class ParkingHourlyVehicleFlowPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 设备 ID
     */
    private Long deviceId;

    /**
     * 记录时间，精确到小时
     */
    private LocalDateTime recordTime;

    /**
     * 车辆数
     */
    private Integer vehicleCount;
}
