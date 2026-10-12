package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频巡逻路线返回参数
 *
 * @author lanchun
 * @date 2021/12/6 19:50
 */
@Data
public class CameraPatrolLineVO {

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
     * 巡逻建议时间
     */
    private String patrolProposalTime;

    /**
     * 最后巡逻时间
     */
    private LocalDateTime patrolLastTime;

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
     * 创建人名称
     */
    private String createByName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 监控列表
     */
    private List<CameraPatrolDeviceVO> cameraPatrolDeviceVOS;

    /**
     * 人员配置列表
     */
    private List<CameraPatrolPersonVO> cameraPatrolPersonVOS;
}