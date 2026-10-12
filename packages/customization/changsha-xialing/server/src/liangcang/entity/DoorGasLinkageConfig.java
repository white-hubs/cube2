package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 门禁气体联动配置。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("door_gas_linkage_config")
public class DoorGasLinkageConfig extends BasicEnhancedTenantEntity<Long> {
    /** 主键。 */
    private Long id;
    /** 设备中心本地门禁设备 ID。 */
    private Long doorDeviceId;
    /** 是否开启气体联动。 */
    private Boolean gasLinkageEnabled;
    /** 气体数据有效时长，单位秒。 */
    private Integer dataValidSeconds;
    /** O2、CO2、PH3 配置数组。 */
    private String deviceConfigJson;
}
