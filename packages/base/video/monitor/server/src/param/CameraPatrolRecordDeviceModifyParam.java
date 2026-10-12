package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceResultEnum;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频巡逻设备记录保存参数
 *
 * @author lanchun
 * @date 2021/12/6 19:48
 */
@Data
public class CameraPatrolRecordDeviceModifyParam {

    /**
     * 巡逻设备主键
     */
    @NotNull(message = "巡逻设备主键不能为空")
    @Min(value = 1, message = "巡逻设备主键不能小于1")
    private Long deviceId;

    /**
     * 设备编号
     */
    @Size(max = 100, message = "设备编号不能大于100")
    private String deviceCode;

    /**
     * 设备名称
     */
    @NotEmpty(message = "设备名称不能为空")
    @Size(max = 100, message = "设备名称不能大于100")
    private String deviceName;

    /**
     * 巡逻设备区域
     */
    @NotEmpty(message = "设备区域不能为空")
    @Size(max = 200, message = "设备区域不能大于200")
    private String area;

    /**
     * 巡逻设备区域名称
     */
    private String areaNames;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 巡逻时间
     */
    private LocalDateTime patrolTime;

    /**
     * 巡逻结果
     */
    private CameraPatrolDeviceResultEnum result = CameraPatrolDeviceResultEnum.CAMERA_PATROL_UNKNOWN;

    /**
     * 巡逻状态
     */
    private CameraPatrolDeviceStatusEnum status = CameraPatrolDeviceStatusEnum.CAMERA_PATROL_UNKNOWN;

    /**
     * 巡逻状态描述
     */
    @Valid
    private List<CameraPatrolRecordDeviceRemarksModifyParam> deviceRemarksModifyParams;
}