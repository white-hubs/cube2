package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频巡逻路线分页返回参数
 *
 * @author lanchun
 * @date 2021/12/8 13:52
 */
@Data
public class CameraPatrolLinePageVO {
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
    private Integer patrolNum;

    /**
     * 巡逻人员名称
     */
    private String patrolUserNames;

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

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 创建人类型
     */
    private ActorTypeEnum creatorType;

    /**
     * 创建名称
     */
    private String createByName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
