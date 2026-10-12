package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 事件关联联动设备执行结果数据模型
 *
 * @author lanchun
 * @date 2022/6/21 11:58
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventIotDevicePro extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 事件关联预案主键
     */
    private Long eventLinkageId;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 联动预案主键
     */
    private Long linkageId;

    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 设备产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性值
     */
    private String propValue;

    /**
     * 执行方式
     */
    private ExecuteModeEnum executeMode;
}
