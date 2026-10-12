package com.guhecloud.cube.base.device.center.metadata.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设备经纬度与位置信息修改参数（扩展表 {@code lnt_and_lat}、{@code location_des}）
 */
@Data
public class ChangeDeviceLntLatCmd {

    /**
     * 设备 ID
     */
    @NotNull(message = "参数错误，设备不能为空")
    private Long deviceId;

    /**
     * 经纬度字符串，一般为「经度,纬度」；不传则不更新该字段
     */
    private String lntAndLat;

    /**
     * 位置详细描述；不传则不更新该字段
     */
    private String locationDes;
}
