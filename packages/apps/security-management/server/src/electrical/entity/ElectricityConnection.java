package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供电关系(供电节点连接关系)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElectricityConnection extends BasicTenantEntity<Long> {
    private Long id;
    private String lineName;
    /**
     * 开始节点id
     */
    private Long fromNodeId;
    /**
     * 结束节点id
     */
    private Long toNodeId;
    /**
     * 网页样式(前端定位用)
     */
    private String webStyle;
}
