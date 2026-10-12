package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.dto;

import lombok.Data;

/**
 * @author pengjian
 * @date 2024/1/11 20:53
 * @description
 */
@Data
public class DeviceStatusRuntimeAndNumberDTO {
    private Long deviceInstanceId;
    private Integer normalRunNumber;
    private Integer troubleRunNumber;
    private Integer alarmRunNumber;
    private Integer offlineRunNumber;
    private Integer normalRuntime = 0;
    private Integer troubleRuntime = 0;
    private Integer alarmRuntime = 0;
    private Integer offlineRuntime = 0;

}
