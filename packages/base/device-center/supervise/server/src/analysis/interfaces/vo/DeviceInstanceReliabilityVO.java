package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/8/3 15:45
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInstanceReliabilityVO {
    private Long id;
    private String code;
    private String name;
    private Long workerDay;
    private Integer troubleRunNumber;
}
