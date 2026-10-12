package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 线损关系(线损节点连接关系)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossConnection extends BasicTenantEntity<Long> {
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
    /**
     * 输入设备id
     */
    private Long fromDeviceId;
    /**
     * 输出设备id
     */
    private Long toDeviceId;
    /**
     * 最近一次的线损率
     */
    private BigDecimal lineLossRate;
    /**
     * 所在位置
     */
    private String location;
}
