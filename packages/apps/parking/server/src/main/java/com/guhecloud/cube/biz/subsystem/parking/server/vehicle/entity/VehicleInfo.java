package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity;

import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration.VehicleNoColorEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 车辆信息实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleInfo extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 车牌号码
     */
    private String vehicleNo;
    /**
     * 车辆类型编号
     */
    private String vehicleTypeCode;
    /**
     * 车辆类型名称
     */
    private String vehicleTypeName;
    /**
     * 车主姓名
     */
    private String customerName;
    /**
     * 车主联系手机号
     */
    private String customerMobile;

    /**
     * 用户Id
     */
    private Long customerId;

    /**
     * 车牌颜色
     */
    private VehicleNoColorEnum vehicleNoColor;

    /**
     * 是否加入黑名单
     */
    private Boolean isBlacklist;
}
