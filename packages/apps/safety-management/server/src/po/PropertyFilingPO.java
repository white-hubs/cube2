package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 资产建档默认设备信息采集
 * @date 2023/9/13 15:07
 */
@Data
@TableName("property_filing")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyFilingPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 资产名称
     */
    private String propertyName;
    /**
     * 资产编码
     */
    private String serialNumber;
    /**
     * 使用部门
     */
    private String receiveDepName;
    /**
     * 维护部门
     */
    private String maintenanceDepName;
    /**
     * 父级资产类别Code
     */
    private String propertyType;
    /**
     * 资产类别code
     */
    private String propertySubType;
    private String propertySubTypeName;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 入库时间
     */
    private LocalDateTime putInTime;
    /**
     * 维保到期时间
     */
    private LocalDateTime expirationTime;
    /**
     * 资产状态
     */
    private String status;
    /**
     * 管理员id
     */
    private Long managerId;
    /**
     * 管理员名称
     */
    private String managerName;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 使用人id
     */
    private Long userId;
    /**
     * 使用人名称
     */
    private String username;
    /**
     * 所属市场
     */
    private Integer marketTenant;
    private String marketName;
    /**
     * 模型编码
     */
    private String bimCode;
    /**
     * 属性
     */
    private String attribute;
    /**
     * 档位名称
     */
    private String stallName;
    /**
     * 现场点名称
     */
    private String sitePointName;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 电动车类型
     */
    private String electronMobileType;
}
