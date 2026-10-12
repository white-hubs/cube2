package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceAvgHealthVO {

    /**
     * 总设备数
     */
    private Integer totalDeviceNumber;

    /**
     * 平均健康度
     */
    private Double avgHealthValue;

    /**
     * 低于60数量
     */
    private Long belowSixtyNumber;

    /**
     * 低于60占比
     */
    private Double belowSixtyRate;
}
