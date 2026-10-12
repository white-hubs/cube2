package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 车场对接配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("parking_info_dock_config")
public class ParkingInfoDockConfigPO extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 厂家编码
     */
    private String providerCode;

    /**
     * 车场对接配置
     */
    private String config;
}
