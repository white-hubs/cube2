package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频巡逻记录保存参数
 *
 * @author lanchun
 * @date 2021/12/6 19:49
 */
@Data
public class CameraPatrolRecordModifyParam {

    /**
     * 巡逻线路主键
     */
    @NotNull(message = "巡逻线路主键不能为空")
    @Min(value = 1, message = "巡逻线路主键不能小于1")
    private Long cameraPatrolLineId;

    /**
     * 巡逻线路名称
     */
    @NotEmpty(message = "巡逻路线不能为空")
    @Size(max = 100, message = "巡逻线路名称不能大于100")
    private String cameraPatrolLineName;

    /**
     * 巡逻时间
     */
    private LocalDateTime patrolTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 巡逻状态
     */
    private CameraPatrolStatusEnum status = CameraPatrolStatusEnum.CAMERA_PATROL_COMPLETED;

    /**
     * 巡逻设备集合
     */
    @NotNull(message = "巡逻设备不能为空")
    @Size(min = 1, message = "巡逻设备数量不能小于1")
    @Valid
    private List<CameraPatrolRecordDeviceModifyParam> deviceModifyParams;
}