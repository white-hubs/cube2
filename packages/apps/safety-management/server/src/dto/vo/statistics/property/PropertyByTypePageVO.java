package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author lijie
 * @describe 资产建档统计
 * @date 2023/9/13 19:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PropertyByTypePageVO {
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
     * 资产类别code
     */
    private String propertySubType;
    private String propertySubTypeName;
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
}
