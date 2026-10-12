package com.guhecloud.cube.biz.vmonitor.server.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 多路视频摄像机保存参数
 *
 * @author lanchun
 * @date 2021/12/6 19:49
 */
@Data
public class MultipleVideoCollectCameraModifyParam {

    /**
     * 摄像机设备主键
     */
    @NotNull(message = "设备主键不能为空")
    @Min(value = 1, message = "设备主键不能小于1")
    private Long deviceId;

    /**
     * 序号
     */
    @Min(value = 0, message = "序号不能小于1")
    private Integer sort;
}