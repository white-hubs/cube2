package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 设备管理数据采集
 * @date 2023/9/12 20:06
 */
@Data
@TableName("device_manage")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceManagePO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 设备id
     */
    private Long id;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备品类名称
     */
    private String iotDeviceProdCategoryName;
    /**
     * 设备状态
     */
    private String status;
    /**
     * 设备产品名称
     */
    private String iotDeviceProdName;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域
     */
    private String areaName;
    /**
     * 部门id
     */
    private String departmentId;
    /**
     * 部门
     */
    private String departmentName;
    /**
     * 应用code
     */
    private String applicationCode;
    /**
     * 市场租户
     */
    private String marketTenant;
    /**
     * 市场名称
     */
    private String marketName;
}
