package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 现场点关联设备
 */
@Data
@TableName("inspect_scene_point_device")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectScenePointDevicePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 现场点ID
     */
    private Long scenePointId;

    /**
     * 设备ID
     */
    private Long deviceId;
}
