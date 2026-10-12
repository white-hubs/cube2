package com.guhecloud.cube.srv.gen.cabbage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 大白菜车辆
 * 车辆同步过来是新增的，这个对象是记录同步过来的数据，并且记录在古河云新增的id
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dbc_vehicle")
@EqualsAndHashCode(callSuper = true)
public class DbcVehiclePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 车牌号码
     */
    private String vehicleNumber;
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
     * 市场序号
     */
    private Integer marketId;
    /**
     * 市场名称
     */
    private String marketName;

    // 古河云的车辆id
    private Long vehicleId;
}
