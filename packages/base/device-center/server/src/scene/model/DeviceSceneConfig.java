package com.guhecloud.cube.base.device.center.scene.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.device.center.scene.enumeration.BusinessSceneConfigTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 设备场景配置
 *
 * @author lanchun
 * @date 2022/5/23 10:27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "center_device_scene_config")
public class DeviceSceneConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 场景类型序列号
     */
    private String sceneTypeSequence;

    /**
     * 业务类型
     */
    private BusinessSceneConfigTypeEnum businessType;

    /**
     * 业务主键
     */
    private String businessId;
}
