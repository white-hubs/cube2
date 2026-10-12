package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 视频巡逻记录数据模型
 *
 * @author lanchun
 * @date 2021/12/6 19:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CameraPatrolRecord extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 巡逻线路主键
     */
    private Long cameraPatrolLineId;

    /**
     * 巡逻线路名称
     */
    private String cameraPatrolLineName;

    /**
     * 巡逻人员
     */
    private Long userId;

    /**
     * 巡逻人员名称
     */
    private String userName;

    /**
     * 巡逻耗时
     */
    private String duration;

    /**
     * 巡逻总数
     */
    private Integer num;

    /**
     * 巡逻完成数量
     */
    private Integer completeNum;

    /**
     * 巡逻空闲数量
     */
    private Integer freeNum;

    /**
     * 巡逻异常数量
     */
    private Integer abnormalNum;

    /**
     * 巡逻正常数量
     */
    private Integer normalNum;

    /**
     * 巡逻时间
     */
    @NotNull(message = "巡逻时间不能为空")
    private LocalDateTime patrolTime;

    /**
     * 完成时间
     */
    @NotNull(message = "完成时间不能为空")
    private LocalDateTime completeTime;

    /**
     * 巡逻状态
     */
    private CameraPatrolStatusEnum status = CameraPatrolStatusEnum.CAMERA_PATROL_COMPLETED;

}