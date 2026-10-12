package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 车场设备关联表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("parking_info_device")
public class ParkingInfoDevicePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 车场id
     */
    private Long parkingInfoId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 当前车数量
     */
    private Integer currentCarNumber;
}
