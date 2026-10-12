package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 16:33
 * @Description: 车辆类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("vehicle_type")
public class VehicleTypePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    private String name;
}
