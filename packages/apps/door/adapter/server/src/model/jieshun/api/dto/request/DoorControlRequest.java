package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * author: ht
 * create: 2025-05-22 15:07
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoorControlRequest {
    /**
     * deviceGuid 设备 guid string 设备唯一标识
     * (deviceGuid 和 deviceId 必须
     * 有一个必填)
     * 否
     * deviceId 设备 ID string 设备 ID,用于远程操作的
     * (系统界面运维驾驶舱可查看
     * 到)
     * 否
     * openModel 开门模式 int 开门：1，关门：2
     * 常开：3，取消常开：8
     * 是
     */
    private String deviceGuid;
    private String deviceId;
    private Integer openModel;
}
