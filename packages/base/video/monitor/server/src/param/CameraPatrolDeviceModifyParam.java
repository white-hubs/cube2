package com.guhecloud.cube.biz.vmonitor.server.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 巡逻监控保存参数
 *
 * @author lanchun
 * @date 2021/12/6 19:48
 */
@Data
public class CameraPatrolDeviceModifyParam {

    /**
     * 巡逻监控设备主键
     */
    @NotNull(message = "监控设备主键不能为空")
    private Long deviceId;

    /**
     * 巡逻监控设备编号
     */
    @Size(max = 100, message = "巡逻监控设备编号不能大于100")
    private String deviceCode;

    /**
     * 巡逻监控设备名称
     */
    @NotEmpty(message = "巡逻监控设备名称不能为空")
    @Size(max = 100, message = "巡逻监控设备名称不能大于100")
    private String deviceName;

    /**
     * 巡逻设备区域
     */
    @Size(max = 200, message = "巡逻设备区域不能大于200")
    private String area;

    /**
     * 序号
     */
    @NotNull(message = "序号不能为空")
    @Min(value = 1, message = "序号不能小于1")
    private Integer sort;

}