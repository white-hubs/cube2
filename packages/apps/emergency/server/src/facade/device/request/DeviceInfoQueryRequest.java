package com.guhecloud.cube.biz.emergency.server.facade.device.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfoQueryRequest {

    /**
     * 设备主键
     */
    private Long deviceId;
}
