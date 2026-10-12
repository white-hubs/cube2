package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/8/30 14:39
 * @description
 */
@Data
@AllArgsConstructor
public class DeviceLifeCycleDTO {
    private LocalDateTime createTime;
    private LocalDateTime stopTime;
}
