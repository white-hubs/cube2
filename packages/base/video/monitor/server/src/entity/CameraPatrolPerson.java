package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolAuthTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线路巡逻人配置
 *
 * @author lanchun
 * @date 2022/8/4 14:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CameraPatrolPerson extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 巡逻路线主键
     */
    private Long cameraPatrolLineId;

    /**
     * 权限类型
     */
    private CameraPatrolAuthTypeEnum authType;

    /**
     * 权限配置
     */
    private String authConfig;

    /**
     * 权限主键集合(部门,角色,用户)
     */
    private String authIds;
}
