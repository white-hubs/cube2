package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.device.center.core.config.BooleanToIntTypeHandler;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备实例
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("center_biz_device")
public class BizDevice extends BasicEnhancedTenantEntity<Long> {
    /**
     * 物联网设备ID
     */
    private Long id;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编码(孪生编码)
     */
    private String deviceCode;

    /**
     * 设备产品ID
     */
    private Long deviceIotProdId;

    /**
     * 归属部门
     */
    private Long departmentId;

    /**
     * 归属部门名称
     */
    private String departmentName;

    /**
     * 区域ID,-分割
     */
    private String areaId;

    /**
     * 区域ID名称,--分割
     */
    private String areaName;

    /**
     * 最末级的区域ID
     */
    private Long areaLastId;

    /**
     * 0 离线 1在线
     */
    private Integer deviceState;

    /**
     * 最后通信时间
     */
    private LocalDateTime communicationTime;

    /**
     * 管理人员ids,-分割
     */
    private String managerIds;

    /**
     * 管理人员名称,--分割
     */
    private String managerNames;

    /**
     * 三方设备ID
     */
    @TableField("thirdparty_id")
    private String thirdPartyId;

    /**
     * 三方设备配置
     */
    private String thirdpartyCfg;

    @TableField(typeHandler = BooleanToIntTypeHandler.class)
    private Boolean deleted;
}