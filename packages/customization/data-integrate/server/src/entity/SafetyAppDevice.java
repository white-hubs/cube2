package com.guhecloud.cube.base.dataintegrate.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SafetyAppDevice {

    /**
     * 设备id
     */
    private Long id;

    /**
     * 应用编号
     */
    private String applicationCode;
}
