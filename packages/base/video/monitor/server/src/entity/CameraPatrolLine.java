package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 视频巡逻路线数据模型
 *
 * @author lanchun
 * @date 2021/12/6 19:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CameraPatrolLine extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 监控数量
     */
    private Integer cameraNum;

    /**
     * 巡逻次数
     */
    private Integer patrolNum = 0;

    /**
     * 巡逻建议时间
     */
    private String patrolProposalTime;

    /**
     * 最后巡逻时间
     */
    private LocalDateTime patrolLastTime;

    /**
     * 是否启用
     */
    private Boolean enabled;

}